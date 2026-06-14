package com.smartpocket.ui.home

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartpocket.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SecurityViewModel(application: Application) : AndroidViewModel(application),
    SensorEventListener {

    private val _securityState = MutableLiveData(SecurityState())
    val securityState: LiveData<SecurityState> = _securityState

    private val _events = MutableLiveData<List<SecurityEvent>>(emptyList())
    val events: LiveData<List<SecurityEvent>> = _events

    private val _alertTrigger = MutableLiveData<SecurityEvent?>()
    val alertTrigger: LiveData<SecurityEvent?> = _alertTrigger

    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    private var proximityValue = Float.MAX_VALUE
    private var lightValue = Float.MAX_VALUE
    private var bagBreachJob: Job? = null
    private var alarmDelay = 5 // seconds

    // Thresholds
    private val PROXIMITY_THRESHOLD = 3f  // cm - phone is near something
    private val LIGHT_THRESHOLD_DARK = 10f // lux - very low light = bag is closed
    private val LIGHT_THRESHOLD_BRIGHT = 100f // lux - bright = bag opened

    private var previousLightState = LightState.UNKNOWN

    private enum class LightState { UNKNOWN, DARK, BRIGHT }

    init {
        // Load saved preferences
    }

    // ─────────────────────────── ARM / DISARM ───────────────────────────

    fun toggleArm() {
        val current = _securityState.value ?: return
        if (current.armState == ArmState.DISARMED) {
            arm()
        } else {
            disarm()
        }
    }

    fun arm() {
        updateState { copy(armState = ArmState.ARMED) }
        addEvent(SecurityEvent(
            type = SecurityEvent.EventType.ARMED,
            message = "Security armed",
            severity = SecurityEvent.Severity.INFO
        ))
        startSensorMonitoring()
    }

    fun disarm() {
        updateState {
            copy(
                armState = ArmState.DISARMED,
                bagSensorState = BagSensorState.INACTIVE,
                bluetoothGuardState = BluetoothGuardState.INACTIVE
            )
        }
        addEvent(SecurityEvent(
            type = SecurityEvent.EventType.DISARMED,
            message = "Security disarmed",
            severity = SecurityEvent.Severity.INFO
        ))
        stopSensorMonitoring()
        bagBreachJob?.cancel()
    }

    // ─────────────────────────── BAG SENSOR ───────────────────────────

    fun setBagSensorEnabled(enabled: Boolean) {
        val current = _securityState.value ?: return
        updateState { copy(bagSensorActive = enabled) }
        if (enabled && current.armState == ArmState.ARMED) {
            startBagSensor()
        } else {
            stopBagSensor()
        }
    }

    private fun startSensorMonitoring() {
        val current = _securityState.value ?: return
        if (current.bagSensorActive) startBagSensor()
    }

    private fun stopSensorMonitoring() {
        sensorManager.unregisterListener(this)
        updateState { copy(bagSensorState = BagSensorState.INACTIVE) }
    }

    private fun startBagSensor() {
        updateState { copy(bagSensorState = BagSensorState.MONITORING) }
        proximitySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    private fun stopBagSensor() {
        sensorManager.unregisterListener(this)
        updateState { copy(bagSensorState = BagSensorState.INACTIVE) }
    }

    // ─────────────────────────── SENSOR EVENTS ───────────────────────────

    override fun onSensorChanged(event: SensorEvent) {
        val current = _securityState.value ?: return
        if (current.armState != ArmState.ARMED || !current.bagSensorActive) return

        when (event.sensor.type) {
            Sensor.TYPE_PROXIMITY -> {
                proximityValue = event.values[0]
            }
            Sensor.TYPE_LIGHT -> {
                lightValue = event.values[0]
                evaluateBagState()
            }
        }
    }

    private fun evaluateBagState() {
        val current = _securityState.value ?: return
        if (current.bagSensorState != BagSensorState.MONITORING) return

        val newLightState = when {
            lightValue < LIGHT_THRESHOLD_DARK -> LightState.DARK
            lightValue > LIGHT_THRESHOLD_BRIGHT -> LightState.BRIGHT
            else -> previousLightState
        }

        // Bag breach: was dark (closed) → now bright (opened)
        if (previousLightState == LightState.DARK && newLightState == LightState.BRIGHT) {
            onBagBreachDetected()
        }

        previousLightState = newLightState
    }

    private fun onBagBreachDetected() {
        val current = _securityState.value ?: return
        if (current.bagSensorState == BagSensorState.BREACHED) return // already triggered

        updateState { copy(bagSensorState = BagSensorState.BREACHED) }

        bagBreachJob?.cancel()
        bagBreachJob = viewModelScope.launch {
            val event = SecurityEvent(
                type = SecurityEvent.EventType.BAG_BREACH,
                message = "Bag breach detected!",
                severity = SecurityEvent.Severity.CRITICAL
            )
            addEvent(event)
            _alertTrigger.postValue(event)

            // Delay before lockdown
            delay(alarmDelay * 1000L)

            val refreshed = _securityState.value ?: return@launch
            if (refreshed.lockdownEnabled && refreshed.armState == ArmState.ARMED) {
                triggerLockdown(SecurityEvent.EventType.BAG_BREACH)
            }
        }
    }

    // ─────────────────────────── LOCKDOWN ───────────────────────────

    fun triggerLockdown(cause: SecurityEvent.EventType = SecurityEvent.EventType.LOCKDOWN_ACTIVATED) {
        updateState { copy(armState = ArmState.LOCKDOWN) }
        val event = SecurityEvent(
            type = SecurityEvent.EventType.LOCKDOWN_ACTIVATED,
            message = "Lockdown activated — ${cause.name.replace('_', ' ').lowercase()}",
            severity = SecurityEvent.Severity.CRITICAL
        )
        addEvent(event)
        _alertTrigger.postValue(event)
    }

    fun clearLockdown() {
        updateState { copy(armState = ArmState.ARMED, bagSensorState = BagSensorState.MONITORING) }
        addEvent(SecurityEvent(
            type = SecurityEvent.EventType.LOCKDOWN_CLEARED,
            message = "Lockdown cleared — identity verified",
            severity = SecurityEvent.Severity.INFO
        ))
    }

    // ─────────────────────────── FACE AUTH ───────────────────────────

    fun setFaceAuthEnabled(enabled: Boolean) {
        updateState { copy(faceAuthEnabled = enabled) }
    }

    fun onFaceEnrolled() {
        updateState { copy(faceAuthState = FaceAuthState.ENROLLED) }
    }

    fun onFaceVerified() {
        updateState { copy(faceAuthState = FaceAuthState.VERIFIED) }
        addEvent(SecurityEvent(
            type = SecurityEvent.EventType.FACE_VERIFIED,
            message = "Face identity verified",
            severity = SecurityEvent.Severity.INFO
        ))
        // Auto-clear lockdown on face verify
        val current = _securityState.value
        if (current?.armState == ArmState.LOCKDOWN) {
            clearLockdown()
        }
    }

    fun onFaceVerifyFailed() {
        updateState { copy(faceAuthState = FaceAuthState.FAILED) }
        addEvent(SecurityEvent(
            type = SecurityEvent.EventType.FACE_FAILED,
            message = "Face verification failed",
            severity = SecurityEvent.Severity.WARNING
        ))
    }

    // ─────────────────────────── BLUETOOTH ───────────────────────────

    fun setBluetoothGuardEnabled(enabled: Boolean) {
        updateState {
            copy(
                bluetoothGuardActive = enabled,
                bluetoothGuardState = if (enabled) BluetoothGuardState.MONITORING else BluetoothGuardState.INACTIVE
            )
        }
    }

    fun onBluetoothRssiUpdate(rssi: Int) {
        val current = _securityState.value ?: return
        if (!current.bluetoothGuardActive) return

        val newState = when {
            rssi > -60 -> BluetoothGuardState.SIGNAL_STRONG
            rssi > -75 -> BluetoothGuardState.SIGNAL_MEDIUM
            rssi > -90 -> BluetoothGuardState.SIGNAL_WEAK
            else -> BluetoothGuardState.SIGNAL_LOST
        }
        updateState { copy(bluetoothGuardState = newState) }

        if (newState == BluetoothGuardState.SIGNAL_LOST && current.bluetoothGuardState != BluetoothGuardState.SIGNAL_LOST) {
            onBluetoothSignalLost()
        }
    }

    private fun onBluetoothSignalLost() {
        val event = SecurityEvent(
            type = SecurityEvent.EventType.BT_LOST,
            message = "Guardian device out of range!",
            severity = SecurityEvent.Severity.CRITICAL
        )
        addEvent(event)
        _alertTrigger.postValue(event)

        viewModelScope.launch {
            delay(alarmDelay * 1000L)
            val refreshed = _securityState.value ?: return@launch
            if (refreshed.bluetoothGuardState == BluetoothGuardState.SIGNAL_LOST
                && refreshed.lockdownEnabled && refreshed.armState == ArmState.ARMED) {
                triggerLockdown(SecurityEvent.EventType.BT_LOST)
            }
        }
    }

    // ─────────────────────────── SETTINGS ───────────────────────────

    fun setLockdownEnabled(enabled: Boolean) {
        updateState { copy(lockdownEnabled = enabled) }
    }

    fun setAlarmDelay(seconds: Int) {
        alarmDelay = seconds
    }

    // ─────────────────────────── HELPERS ───────────────────────────

    private fun updateState(transform: SecurityState.() -> SecurityState) {
        _securityState.value = (_securityState.value ?: SecurityState()).transform()
    }

    private fun addEvent(event: SecurityEvent) {
        val current = _events.value?.toMutableList() ?: mutableListOf()
        current.add(0, event)
        _events.value = current.take(50) // keep last 50 events
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
        bagBreachJob?.cancel()
    }
}
