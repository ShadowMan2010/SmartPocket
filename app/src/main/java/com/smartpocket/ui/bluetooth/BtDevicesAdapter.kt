package com.smartpocket.ui.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartpocket.R
import com.smartpocket.utils.BluetoothDevice

class BtDevicesAdapter(
    private val onDeviceSelected: (BluetoothDevice) -> Unit
) : ListAdapter<BluetoothDevice, BtDevicesAdapter.DeviceViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bt_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(getItem(position), onDeviceSelected)
    }

    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tv_device_item_name)
        private val tvAddress: TextView = view.findViewById(R.id.tv_device_item_address)
        private val tvRssi: TextView = view.findViewById(R.id.tv_device_item_rssi)
        private val ivSignal: ImageView = view.findViewById(R.id.iv_signal_strength)

        fun bind(device: BluetoothDevice, onSelect: (BluetoothDevice) -> Unit) {
            tvName.text = device.name.ifEmpty { "Unknown Device" }
            tvAddress.text = device.address
            tvRssi.text = "${device.rssi} dBm"

            val ctx = itemView.context
            val signalColor = when {
                device.rssi > -60 -> R.color.sp_safe
                device.rssi > -75 -> R.color.sp_accent_secondary
                device.rssi > -90 -> R.color.sp_warning
                else -> R.color.sp_danger
            }
            tvRssi.setTextColor(ContextCompat.getColor(ctx, signalColor))
            ivSignal.setColorFilter(ContextCompat.getColor(ctx, signalColor))

            itemView.setOnClickListener { onSelect(device) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(a: BluetoothDevice, b: BluetoothDevice) = a.address == b.address
        override fun areContentsTheSame(a: BluetoothDevice, b: BluetoothDevice) = a == b
    }
}
