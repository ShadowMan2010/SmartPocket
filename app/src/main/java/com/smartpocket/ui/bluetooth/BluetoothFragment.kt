package com.smartpocket.ui.bluetooth

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartpocket.R
import com.smartpocket.databinding.FragmentBluetoothBinding
import com.smartpocket.ui.home.SecurityViewModel
import com.smartpocket.utils.BluetoothDevice
import com.smartpocket.utils.BluetoothGuardState

class BluetoothFragment : Fragment() {

    private var _binding: FragmentBluetoothBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SecurityViewModel by activityViewModels()

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private lateinit var devicesAdapter: BtDevicesAdapter

    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private var guardianDevice: BluetoothDevice? = null
    private var isScanning = false
    private val scanHandler = Handler(Looper.getMainLooper())
    private val rssiHandler = Handler(Looper.getMainLooper())
    private val SCAN_DURATION_MS = 12_000L
    private val RSSI_POLL_INTERVAL_MS = 3_000L

    private val bleScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val address = result.device.address
            val name = result.device.name ?: address.takeLast(8)
            val rssi = result.rssi

            upsertDevice(BluetoothDevice(name = name, address = address, rssi = rssi, isPaired = result.device.bondState != android.bluetooth.BluetoothDevice.BOND_NONE))
        }

        override fun onScanFailed(errorCode: Int) {
            activity?.runOnUiThread {
                binding.tvDeviceName.text = "BLE scan failed (code $errorCode)"
            }
        }
    }

    private val classicDiscoveryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                android.bluetooth.BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<android.bluetooth.BluetoothDevice>(android.bluetooth.BluetoothDevice.EXTRA_DEVICE) ?: return
                    val name = device.name ?: device.address.takeLast(8)
                    upsertDevice(BluetoothDevice(name = name, address = device.address, rssi = 0, isPaired = device.bondState != android.bluetooth.BluetoothDevice.BOND_NONE))
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    activity?.runOnUiThread { binding.tvDeviceName.text = getString(R.string.bt_scan_done) }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBluetoothBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBluetooth()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        startSignalRingAnimation()
        loadBondedDevices()
    }

    private fun setupBluetooth() {
        val btManager = requireContext().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = btManager.adapter
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    }

    private fun setupRecyclerView() {
        devicesAdapter = BtDevicesAdapter { device ->
            setGuardianDevice(device)
        }
        binding.rvDevices.apply {
            adapter = devicesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
        }
    }

    private fun setupClickListeners() {
        binding.btnScan.setOnClickListener {
            if (isScanning) stopScan() else startScan()
        }

        binding.sliderRange.addOnChangeListener { _, value, _ ->
            binding.tvRangeValue.text = "${value.toInt()} dBm"
        }
    }

    private fun observeViewModel() {
        viewModel.securityState.observe(viewLifecycleOwner) { state ->
            updateSignalUI(state.bluetoothGuardState)
        }
    }

    // ─────────────────────────── DEVICE MERGING ───────────────────────────

    private fun upsertDevice(device: BluetoothDevice) {
        val existing = discoveredDevices.indexOfFirst { it.address == device.address }
        if (existing >= 0) {
            discoveredDevices[existing] = device
        } else {
            discoveredDevices.add(device)
        }
        discoveredDevices.sortWith(compareByDescending<BluetoothDevice> { it.isPaired }.thenByDescending { it.rssi })
        activity?.runOnUiThread {
            devicesAdapter.submitList(discoveredDevices.toList())
        }
    }

    // ─────────────────────────── SCANNING ───────────────────────────

    private fun loadBondedDevices() {
        if (!hasBluetoothPermissions()) return
        val bonded = bluetoothAdapter?.bondedDevices ?: return
        for (device in bonded) {
            val name = device.name ?: device.address.takeLast(8)
            upsertDevice(BluetoothDevice(name = name, address = device.address, rssi = 0, isPaired = true))
        }
    }

    private fun startScan() {
        if (!hasBluetoothPermissions()) return
        if (bluetoothAdapter?.isEnabled != true) {
            binding.tvDeviceName.text = "Bluetooth is off"
            return
        }

        isScanning = true
        discoveredDevices.clear()
        loadBondedDevices()
        devicesAdapter.submitList(discoveredDevices.toList())
        binding.btnScan.text = "Stop Scan"
        binding.tvDeviceName.text = getString(R.string.bt_scanning)

        // Start BLE scan
        if (bluetoothLeScanner != null) {
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
            try { bluetoothLeScanner?.startScan(null, settings, bleScanCallback) } catch (_: Exception) {}
        }

        // Start classic Bluetooth discovery
        try { bluetoothAdapter?.startDiscovery() } catch (_: Exception) {}

        // Register classic discovery receiver
        val filter = IntentFilter().apply {
            addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        requireContext().registerReceiver(classicDiscoveryReceiver, filter)

        // Auto-stop scan after duration
        scanHandler.postDelayed({ stopScan() }, SCAN_DURATION_MS)
    }

    private fun stopScan() {
        if (!isScanning) return
        isScanning = false
        binding.btnScan.text = getString(R.string.bt_scan)
        scanHandler.removeCallbacksAndMessages(null)
        try { bluetoothLeScanner?.stopScan(bleScanCallback) } catch (_: Exception) {}
        try { bluetoothAdapter?.cancelDiscovery() } catch (_: Exception) {}
        try { requireContext().unregisterReceiver(classicDiscoveryReceiver) } catch (_: Exception) {}
    }

    private fun setGuardianDevice(device: BluetoothDevice) {
        guardianDevice = device
        binding.tvDeviceName.text = device.name
        binding.tvDeviceAddress.text = device.address
        viewModel.setBluetoothGuardEnabled(true)
        startRssiPolling()
        stopScan()
    }

    // ─────────────────────────── RSSI POLLING ───────────────────────────

    private fun startRssiPolling() {
        rssiHandler.post(rssiRunnable)
    }

    private fun stopRssiPolling() {
        rssiHandler.removeCallbacks(rssiRunnable)
    }

    private val rssiRunnable = object : Runnable {
        override fun run() {
            val device = guardianDevice ?: return
            pollRssiFromDevice(device)
            rssiHandler.postDelayed(this, RSSI_POLL_INTERVAL_MS)
        }
    }

    private fun pollRssiFromDevice(device: BluetoothDevice) {
        if (!hasBluetoothPermissions()) return
        try {
            val btDevice = bluetoothAdapter?.getRemoteDevice(device.address) ?: return
            val gatt = btDevice.connectGatt(requireContext(), false, object :
                android.bluetooth.BluetoothGattCallback() {
                override fun onReadRemoteRssi(gatt: android.bluetooth.BluetoothGatt?, rssi: Int, status: Int) {
                    if (status == android.bluetooth.BluetoothGatt.GATT_SUCCESS) {
                        activity?.runOnUiThread {
                            updateRssiDisplay(rssi)
                            viewModel.onBluetoothRssiUpdate(rssi)
                            val threshold = binding.sliderRange.value.toInt()
                            if (rssi < threshold) {
                                onBeyondRange(rssi)
                            }
                        }
                    }
                    gatt?.disconnect()
                    gatt?.close()
                }

                override fun onConnectionStateChange(gatt: android.bluetooth.BluetoothGatt?, status: Int, newState: Int) {
                    if (newState == android.bluetooth.BluetoothProfile.STATE_CONNECTED) {
                        gatt?.readRemoteRssi()
                    }
                }
            })
        } catch (e: Exception) {
            android.util.Log.e("BT", "RSSI poll failed", e)
        }
    }

    private fun updateRssiDisplay(rssi: Int) {
        binding.tvSignalStrength.text = "$rssi dBm"
        binding.tvSignalLabel.text = when {
            rssi > -60 -> getString(R.string.bt_signal_strong)
            rssi > -75 -> getString(R.string.bt_signal_medium)
            rssi > -90 -> getString(R.string.bt_signal_weak)
            else -> getString(R.string.bt_signal_lost)
        }
        val color = when {
            rssi > -60 -> R.color.sp_safe
            rssi > -75 -> R.color.sp_accent_secondary
            rssi > -90 -> R.color.sp_warning
            else -> R.color.sp_danger
        }
        binding.tvSignalStrength.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

    private fun onBeyondRange(rssi: Int) {
    }

    // ─────────────────────────── UI / ANIMATIONS ───────────────────────────

    private fun updateSignalUI(state: BluetoothGuardState) {
        val ringColor = when (state) {
            BluetoothGuardState.SIGNAL_STRONG -> R.color.sp_safe
            BluetoothGuardState.SIGNAL_MEDIUM -> R.color.sp_accent_primary
            BluetoothGuardState.SIGNAL_WEAK -> R.color.sp_warning
            BluetoothGuardState.SIGNAL_LOST -> R.color.sp_danger
            else -> R.color.sp_border
        }
        val color = ContextCompat.getColor(requireContext(), ringColor)
        for (ring in listOf(binding.ring1, binding.ring2, binding.ring3)) {
            ring.backgroundTintList = android.content.res.ColorStateList.valueOf(color)
        }
    }

    private fun startSignalRingAnimation() {
        listOf(
            binding.ring1 to 0L,
            binding.ring2 to 400L,
            binding.ring3 to 800L
        ).forEach { (ring, delay) ->
            ValueAnimator.ofFloat(0.85f, 1.05f, 0.85f).apply {
                duration = 3000
                startDelay = delay
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                addUpdateListener {
                    val v = it.animatedValue as Float
                    ring.scaleX = v
                    ring.scaleY = v
                }
                start()
            }
        }
    }

    private fun hasBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) ==
                    PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onStop() {
        super.onStop()
        stopScan()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopScan()
        stopRssiPolling()
        _binding = null
    }
}
