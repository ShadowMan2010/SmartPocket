package com.smartpocket.utils

import java.time.LocalDateTime

enum class ArmState {
    DISARMED,
    ARMED,
    ALERT,
    LOCKDOWN
}

enum class BagSensorState {
    INACTIVE,
    MONITORING,
    BREACHED
}

enum class BluetoothGuardState {
    INACTIVE,
    MONITORING,
    SIGNAL_STRONG,
    SIGNAL_MEDIUM,
    SIGNAL_WEAK,
    SIGNAL_LOST
}

enum class FaceAuthState {
    NOT_ENROLLED,
    ENROLLED,
    VERIFYING,
    VERIFIED,
    FAILED
}

enum class SnatchState {
    INACTIVE,
    MONITORING,
    TRIGGERED
}

data class SecurityState(
    val armState: ArmState = ArmState.DISARMED,
    val bagSensorActive: Boolean = false,
    val bagSensorState: BagSensorState = BagSensorState.INACTIVE,
    val faceAuthEnabled: Boolean = false,
    val faceAuthState: FaceAuthState = FaceAuthState.NOT_ENROLLED,
    val bluetoothGuardActive: Boolean = false,
    val bluetoothGuardState: BluetoothGuardState = BluetoothGuardState.INACTIVE,
    val snatchActive: Boolean = false,
    val snatchState: SnatchState = SnatchState.INACTIVE,
    val lockdownEnabled: Boolean = true,
    val lastEventTime: Long = 0L
)

data class SecurityEvent(
    val id: Long = System.currentTimeMillis(),
    val type: EventType,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val severity: Severity = Severity.INFO
) {
    enum class EventType {
        BAG_BREACH, FACE_VERIFIED, FACE_FAILED,
        BT_LOST, BT_RECONNECTED, ARMED, DISARMED,
        LOCKDOWN_ACTIVATED, LOCKDOWN_CLEARED,
        SNATCH_DETECTED
    }

    enum class Severity {
        INFO, WARNING, CRITICAL
    }
}

data class BluetoothDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val isPaired: Boolean = false
)

// Liveness check steps
enum class LivenessStep {
    BLINK,
    TURN_LEFT,
    TURN_RIGHT,
    SMILE
}
