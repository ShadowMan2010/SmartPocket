# Smart Pocket — Anti-Theft Security App

A modern, minimalist Android security application built in Kotlin, providing multi-layered device and bag protection with fully offline operation.

---

## Architecture Overview

```
com.smartpocket/
├── ui/
│   ├── home/
│   │   ├── MainActivity.kt          — Nav host, permission orchestration
│   │   ├── HomeFragment.kt          — Dashboard with shield, status cards
│   │   ├── SecurityViewModel.kt     — Shared state + sensor logic
│   │   ├── EventsAdapter.kt         — Security event log list
│   │   └── LockdownActivity.kt      — Fullscreen lockdown overlay
│   ├── faceauth/
│   │   ├── FaceAuthFragment.kt      — Enrollment + verification UI
│   │   └── FaceAuthActivity.kt      — Standalone verify (called from lockdown)
│   ├── bluetooth/
│   │   ├── BluetoothFragment.kt     — BLE scan, RSSI monitor, range slider
│   │   └── BtDevicesAdapter.kt      — Discovered devices list
│   └── settings/
│       └── SettingsFragment.kt      — Preferences UI
├── ml/
│   └── FaceEmbeddingManager.kt      — TFLite face embedding + cosine compare
├── service/
│   ├── SmartPocketService.kt        — Foreground service + notifications
│   └── BootReceiver.kt              — Auto-start after reboot
└── utils/
    └── SecurityModels.kt            — State enums + data classes
```

---

## Features

### 1. Bag Breach Detection
Uses the **Proximity Sensor** and **Light Sensor** in tandem:

| Condition | Interpretation |
|---|---|
| Low light (`< 10 lux`) | Bag is closed/covered |
| High light (`> 100 lux`) transition | Bag has been opened |
| Proximity near + dark | Normal pocket/bag state |

The state machine detects the **dark → bright light transition** as a breach trigger, giving a configurable alarm delay (0–30 seconds) before escalating to lockdown.

**Sensitivity tuning** — adjust `LIGHT_THRESHOLD_DARK` and `LIGHT_THRESHOLD_BRIGHT` constants in `SecurityViewModel.kt` for different bag materials and ambient conditions.

---

### 2. Face Recognition (Offline)

**Pipeline:**
```
CameraX (front camera)
    └─► ML Kit Face Detection (landmark + blink/smile classification)
            └─► Liveness Check (4-step: blink → turn left → turn right → smile)
                    └─► TFLite MobileFaceNet (112×112 input → 128-d L2-normalized embedding)
                            └─► Cosine Similarity (threshold: 0.65)
                                    └─► Enrolled face stored in internal storage (binary)
```

**Liveness Detection Steps:**
1. **Blink** — `leftEyeOpenProbability < 0.15 && rightEyeOpenProbability < 0.15`
2. **Turn Left** — `headEulerAngleY > 15°`
3. **Turn Right** — `headEulerAngleY < -15°`
4. **Smile** — `smilingProbability > 0.7`

**Adding the TFLite model:**
1. Download [MobileFaceNet](https://github.com/sirius-ai/MobileFaceNet_TF) or FaceNet Lite
2. Convert to `.tflite` format
3. Place in `app/src/main/assets/facenet_mobile.tflite`
4. The app falls back to a pixel-statistics pseudo-embedding for dev builds without the model

**Enrolled face storage:**
- Saved as raw binary float array to `filesDir/enrolled_face.bin`
- Excluded from cloud backup in `backup_rules.xml`
- Never transmitted off-device

---

### 3. Bluetooth Proximity Guard

**BLE scanning** discovers nearby devices. User selects a **Guardian Device** (e.g., smartwatch, earbuds, key tag).

**RSSI monitoring:**
```
rssi > -60 dBm  →  Strong  (device nearby, all good)
rssi > -75 dBm  →  Medium  (slight distance)
rssi > -90 dBm  →  Weak    (moving away — warn user)
rssi ≤ -90 dBm  →  LOST    (trigger alert → lockdown)
```

**Alert range slider:** User sets dBm threshold via slider (`-100` to `-40`). The app triggers an alarm when RSSI drops below this threshold for a sustained period.

**GATT RSSI polling** — uses `BluetoothGatt.readRemoteRssi()` on a transient connection every 3 seconds, avoiding battery-draining continuous scan.

---

### 4. Lockdown Mode

Triggered when:
- Bag breach confirmed after alarm delay
- Bluetooth guardian signal lost after alarm delay
- User manually triggers via long-press on Lockdown card

**Lockdown sequence:**
1. `LockdownActivity` launches with `FLAG_SHOW_WHEN_LOCKED | FLAG_TURN_SCREEN_ON`
2. Displays pulsing warning UI with timestamp
3. Back button disabled
4. User must pass **Face Verification** to clear lockdown
5. Up to 3 failed attempts; after that, emergency contact option remains

**Device Admin (optional):**
- Register `SmartPocketDeviceAdmin` in device settings for `lockNow()` capability
- Go to: Settings → Security → Device Admin Apps → Smart Pocket

---

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1+) or later
- Android SDK 34
- Physical Android device (sensors not available on emulator)
- Front-facing camera

### Steps

```bash
# 1. Clone / open project
# 2. Sync Gradle
# 3. Add TFLite model (optional, for full face recognition):
mkdir -p app/src/main/assets
cp your_facenet_mobile.tflite app/src/main/assets/facenet_mobile.tflite

# 4. Build & run on device
```

### Required Permissions (auto-requested)
| Permission | Purpose |
|---|---|
| `CAMERA` | Face recognition |
| `BLUETOOTH_SCAN` / `BLUETOOTH_CONNECT` | BLE device discovery & RSSI |
| `ACCESS_FINE_LOCATION` | BLE scan (Android < 12) |
| `POST_NOTIFICATIONS` | Security alerts (Android 13+) |
| `FOREGROUND_SERVICE` | Background monitoring |

---

## Design System

**Color Palette:**
| Token | Hex | Usage |
|---|---|---|
| `sp_background` | `#0F1117` | App background |
| `sp_surface` | `#1A1D27` | Cards, bottom nav |
| `sp_accent_primary` | `#4A7FBD` | Interactive elements |
| `sp_safe` | `#4CAF8A` | Armed / verified state |
| `sp_warning` | `#E8A838` | Proximity warnings |
| `sp_danger` | `#D95F5F` | Breaches / lockdown |

**Typography:** System sans-serif with weight hierarchy (light, regular, medium). No third-party fonts required.

**Motion:** Subtle scale pulses for armed state, slide transitions between fragments (280ms decelerate cubic), entrance stagger for cards.

---

## Customisation

### Adjust Bag Sensor Sensitivity
In `SecurityViewModel.kt`:
```kotlin
private val LIGHT_THRESHOLD_DARK = 10f   // lux — below this = bag closed
private val LIGHT_THRESHOLD_BRIGHT = 100f // lux — above this = bag opened
```

### Adjust Face Verification Threshold
In `FaceEmbeddingManager.kt`:
```kotlin
const val VERIFICATION_THRESHOLD = 0.65f  // 0.0–1.0, higher = stricter
```

### Adjust Alarm Delay
Settings screen slider: 0–30 seconds before lockdown escalation.

### Emergency Contact
In `LockdownActivity.kt`, replace:
```kotlin
val emergencyNumber = "112"
```
with user-configured contact from DataStore preferences.

---

## Extending the App

### Add DataStore Preferences
```kotlin
// In any ViewModel:
val Context.dataStore by preferencesDataStore("sp_prefs")
val ALARM_DELAY = intPreferencesKey("alarm_delay")
```

### Add Room DB for Event History
```kotlin
@Entity data class EventEntity(
    @PrimaryKey val id: Long,
    val type: String,
    val message: String,
    val timestamp: Long
)
```

### Add Twilio SMS Alert
```kotlin
// In SmartPocketService, on breach:
// POST to your backend → Twilio sends SMS to emergency contact
```

---

## Security Notes

- Face embeddings are stored **locally only** as raw binary in `filesDir`
- No biometric data ever leaves the device
- Enrolled face excluded from Android cloud backup
- TFLite inference runs fully on-device (no network required)
- HMAC-based token verification recommended for WebSocket integration (see NEXUS pattern)

---

## Known Limitations

- Bag sensor accuracy varies by sensor quality and bag material; calibration via the Settings sensitivity slider is recommended
- BLE RSSI is affected by obstacles and interference; treat as approximate range indicator
- The fallback pseudo-embedding (when TFLite model is absent) is **not production-grade** — always include the `.tflite` model for real deployments
- Lockdown via Device Admin requires explicit user activation in system settings

---

*Smart Pocket — Protecting your belongings, silently.*
