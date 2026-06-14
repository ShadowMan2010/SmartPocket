package com.smartpocket.ui.bluetooth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010$\u001a\u00020\u0016H\u0002J\b\u0010%\u001a\u00020&H\u0002J\u0010\u0010\'\u001a\u00020&2\u0006\u0010(\u001a\u00020)H\u0002J$\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\b\u0010.\u001a\u0004\u0018\u00010/2\b\u00100\u001a\u0004\u0018\u000101H\u0016J\b\u00102\u001a\u00020&H\u0016J\b\u00103\u001a\u00020&H\u0016J\u001a\u00104\u001a\u00020&2\u0006\u00105\u001a\u00020+2\b\u00100\u001a\u0004\u0018\u000101H\u0016J\u0010\u00106\u001a\u00020&2\u0006\u00107\u001a\u00020\u0013H\u0002J\u0010\u00108\u001a\u00020&2\u0006\u00107\u001a\u00020\u0013H\u0002J\b\u00109\u001a\u00020&H\u0002J\b\u0010:\u001a\u00020&H\u0002J\b\u0010;\u001a\u00020&H\u0002J\b\u0010<\u001a\u00020&H\u0002J\b\u0010=\u001a\u00020&H\u0002J\b\u0010>\u001a\u00020&H\u0002J\b\u0010?\u001a\u00020&H\u0002J\b\u0010@\u001a\u00020&H\u0002J\u0010\u0010A\u001a\u00020&2\u0006\u0010(\u001a\u00020)H\u0002J\u0010\u0010B\u001a\u00020&2\u0006\u0010C\u001a\u00020DH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001e\u001a\u00020\u001f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\"\u0010#\u001a\u0004\b \u0010!\u00a8\u0006E"}, d2 = {"Lcom/smartpocket/ui/bluetooth/BluetoothFragment;", "Landroidx/fragment/app/Fragment;", "()V", "RSSI_POLL_INTERVAL_MS", "", "SCAN_DURATION_MS", "_binding", "Lcom/smartpocket/databinding/FragmentBluetoothBinding;", "binding", "getBinding", "()Lcom/smartpocket/databinding/FragmentBluetoothBinding;", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "bluetoothLeScanner", "Landroid/bluetooth/le/BluetoothLeScanner;", "devicesAdapter", "Lcom/smartpocket/ui/bluetooth/BtDevicesAdapter;", "discoveredDevices", "", "Lcom/smartpocket/utils/BluetoothDevice;", "guardianDevice", "isScanning", "", "rssiHandler", "Landroid/os/Handler;", "rssiRunnable", "Ljava/lang/Runnable;", "scanCallback", "Landroid/bluetooth/le/ScanCallback;", "scanHandler", "viewModel", "Lcom/smartpocket/ui/home/SecurityViewModel;", "getViewModel", "()Lcom/smartpocket/ui/home/SecurityViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "hasBluetoothPermissions", "observeViewModel", "", "onBeyondRange", "rssi", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onStop", "onViewCreated", "view", "pollRssiFromDevice", "device", "setGuardianDevice", "setupBluetooth", "setupClickListeners", "setupRecyclerView", "startRssiPolling", "startScan", "startSignalRingAnimation", "stopRssiPolling", "stopScan", "updateRssiDisplay", "updateSignalUI", "state", "Lcom/smartpocket/utils/BluetoothGuardState;", "app_debug"})
public final class BluetoothFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable
    private com.smartpocket.databinding.FragmentBluetoothBinding _binding;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable
    private android.bluetooth.BluetoothAdapter bluetoothAdapter;
    @org.jetbrains.annotations.Nullable
    private android.bluetooth.le.BluetoothLeScanner bluetoothLeScanner;
    private com.smartpocket.ui.bluetooth.BtDevicesAdapter devicesAdapter;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.smartpocket.utils.BluetoothDevice> discoveredDevices = null;
    @org.jetbrains.annotations.Nullable
    private com.smartpocket.utils.BluetoothDevice guardianDevice;
    private boolean isScanning = false;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler scanHandler = null;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler rssiHandler = null;
    private final long SCAN_DURATION_MS = 12000L;
    private final long RSSI_POLL_INTERVAL_MS = 3000L;
    @org.jetbrains.annotations.NotNull
    private final android.bluetooth.le.ScanCallback scanCallback = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.Runnable rssiRunnable = null;
    
    public BluetoothFragment() {
        super();
    }
    
    private final com.smartpocket.databinding.FragmentBluetoothBinding getBinding() {
        return null;
    }
    
    private final com.smartpocket.ui.home.SecurityViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupBluetooth() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupClickListeners() {
    }
    
    private final void observeViewModel() {
    }
    
    private final void startScan() {
    }
    
    private final void stopScan() {
    }
    
    private final void setGuardianDevice(com.smartpocket.utils.BluetoothDevice device) {
    }
    
    private final void startRssiPolling() {
    }
    
    private final void stopRssiPolling() {
    }
    
    private final void pollRssiFromDevice(com.smartpocket.utils.BluetoothDevice device) {
    }
    
    private final void updateRssiDisplay(int rssi) {
    }
    
    private final void onBeyondRange(int rssi) {
    }
    
    private final void updateSignalUI(com.smartpocket.utils.BluetoothGuardState state) {
    }
    
    private final void startSignalRingAnimation() {
    }
    
    private final boolean hasBluetoothPermissions() {
        return false;
    }
    
    @java.lang.Override
    public void onStop() {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
}