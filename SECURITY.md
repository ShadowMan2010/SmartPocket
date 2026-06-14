# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.x     | :white_check_mark: |

## Reporting a Vulnerability

Smart Pocket handles sensitive device permissions (camera, Bluetooth, location) and biometric data for face recognition. If you discover a security vulnerability, please report it privately.

- **Email**: scientistdhrub632@gmail.com
- **Response Time**: Within 48 hours
- **Process**: I will acknowledge receipt, investigate, and release a fix as soon as possible.

Do **not** report security vulnerabilities via public GitHub issues.

## Security Considerations

- Face embeddings are stored locally using Room DB — no data leaves the device
- Camera and Bluetooth permissions are requested at runtime (Android 12+)
- Lockdown mode requires biometric or face authentication to unlock
- The app uses HTTPS for any network communication
- All ML processing (face detection, liveness) runs on-device via ML Kit and TensorFlow Lite
