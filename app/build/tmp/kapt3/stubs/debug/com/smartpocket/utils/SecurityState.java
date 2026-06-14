package com.smartpocket.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u001d\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B_\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\u0002\u0010\u0011J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0005H\u00c6\u0003J\t\u0010#\u001a\u00020\u0007H\u00c6\u0003J\t\u0010$\u001a\u00020\u0005H\u00c6\u0003J\t\u0010%\u001a\u00020\nH\u00c6\u0003J\t\u0010&\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\'\u001a\u00020\rH\u00c6\u0003J\t\u0010(\u001a\u00020\u0005H\u00c6\u0003J\t\u0010)\u001a\u00020\u0010H\u00c6\u0003Jc\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u00c6\u0001J\u0013\u0010+\u001a\u00020\u00052\b\u0010,\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010-\u001a\u00020.H\u00d6\u0001J\t\u0010/\u001a\u000200H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0015R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0015\u00a8\u00061"}, d2 = {"Lcom/smartpocket/utils/SecurityState;", "", "armState", "Lcom/smartpocket/utils/ArmState;", "bagSensorActive", "", "bagSensorState", "Lcom/smartpocket/utils/BagSensorState;", "faceAuthEnabled", "faceAuthState", "Lcom/smartpocket/utils/FaceAuthState;", "bluetoothGuardActive", "bluetoothGuardState", "Lcom/smartpocket/utils/BluetoothGuardState;", "lockdownEnabled", "lastEventTime", "", "(Lcom/smartpocket/utils/ArmState;ZLcom/smartpocket/utils/BagSensorState;ZLcom/smartpocket/utils/FaceAuthState;ZLcom/smartpocket/utils/BluetoothGuardState;ZJ)V", "getArmState", "()Lcom/smartpocket/utils/ArmState;", "getBagSensorActive", "()Z", "getBagSensorState", "()Lcom/smartpocket/utils/BagSensorState;", "getBluetoothGuardActive", "getBluetoothGuardState", "()Lcom/smartpocket/utils/BluetoothGuardState;", "getFaceAuthEnabled", "getFaceAuthState", "()Lcom/smartpocket/utils/FaceAuthState;", "getLastEventTime", "()J", "getLockdownEnabled", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class SecurityState {
    @org.jetbrains.annotations.NotNull
    private final com.smartpocket.utils.ArmState armState = null;
    private final boolean bagSensorActive = false;
    @org.jetbrains.annotations.NotNull
    private final com.smartpocket.utils.BagSensorState bagSensorState = null;
    private final boolean faceAuthEnabled = false;
    @org.jetbrains.annotations.NotNull
    private final com.smartpocket.utils.FaceAuthState faceAuthState = null;
    private final boolean bluetoothGuardActive = false;
    @org.jetbrains.annotations.NotNull
    private final com.smartpocket.utils.BluetoothGuardState bluetoothGuardState = null;
    private final boolean lockdownEnabled = false;
    private final long lastEventTime = 0L;
    
    public SecurityState(@org.jetbrains.annotations.NotNull
    com.smartpocket.utils.ArmState armState, boolean bagSensorActive, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.BagSensorState bagSensorState, boolean faceAuthEnabled, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.FaceAuthState faceAuthState, boolean bluetoothGuardActive, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.BluetoothGuardState bluetoothGuardState, boolean lockdownEnabled, long lastEventTime) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.ArmState getArmState() {
        return null;
    }
    
    public final boolean getBagSensorActive() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.BagSensorState getBagSensorState() {
        return null;
    }
    
    public final boolean getFaceAuthEnabled() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.FaceAuthState getFaceAuthState() {
        return null;
    }
    
    public final boolean getBluetoothGuardActive() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.BluetoothGuardState getBluetoothGuardState() {
        return null;
    }
    
    public final boolean getLockdownEnabled() {
        return false;
    }
    
    public final long getLastEventTime() {
        return 0L;
    }
    
    public SecurityState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.ArmState component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.BagSensorState component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.FaceAuthState component5() {
        return null;
    }
    
    public final boolean component6() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.BluetoothGuardState component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final long component9() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.smartpocket.utils.SecurityState copy(@org.jetbrains.annotations.NotNull
    com.smartpocket.utils.ArmState armState, boolean bagSensorActive, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.BagSensorState bagSensorState, boolean faceAuthEnabled, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.FaceAuthState faceAuthState, boolean bluetoothGuardActive, @org.jetbrains.annotations.NotNull
    com.smartpocket.utils.BluetoothGuardState bluetoothGuardState, boolean lockdownEnabled, long lastEventTime) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}