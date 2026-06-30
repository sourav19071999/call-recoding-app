# Secure Call Archive

A secure, privacy-focused Android application for managing call recordings using **Kotlin + Jetpack Compose**.

## Features

### 🎙️ Call Recording
- Detect incoming and outgoing calls using Android Telecom API
- Smart permission requests with clear limitations
- Recording status indicator
- Graceful fallback on unsupported devices

### 💾 Storage Management
- Storage Access Framework for user-selected folders
- Organized by date: `/CallRecordings/YYYY/MM/DD/`
- File format: `CallerNumber_YYYYMMDD_HHMMSS.mp3`

### 📚 In-App Library
- Browse all recordings with contact/number
- Search and filter functionality
- Playback controls
- Duration display
- Local metadata using Room Database

### 🔐 Security Features
- **Admin Password Protection** for deletion (default: @Vyapar6202@)
- Encrypted password storage using Android Keystore
- Optional biometric authentication
- Encrypted metadata storage
- No cloud uploads

### 🏢 Company Authentication
- Login with company email:
  - `user@vyapar.com`
  - `user@vyaparapp.in`
- Secure session management

### ⚙️ Settings
- Storage folder selection
- Password management
- Biometric configuration
- Recording quality options
- Auto-cleanup rules
- Export/Backup functionality

### 🎯 Background Service
- Foreground service with persistent notification
- Graceful recovery after app restart
- Efficient resource management

## Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Database**: Room + Coroutines
- **Dependency Injection**: Hilt
- **Storage**: DataStore + Android Keystore
- **Authentication**: Biometric + Password

### Design Pattern
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** for data access
- **Flow-based** reactive programming

## Project Structure

```
app/src/main/java/com/vyapar/securecallarchive/
├── data/
│   ├── local/
│   │   ├── database/          # Room database setup
│   │   ├── dao/               # Database access objects
│   │   ├── entity/            # Data entities
│   │   └── preferences/       # DataStore preferences
│   ├── security/              # Encryption & Biometric
│   └── repository/            # Repository implementations
├── domain/
│   └── repository/            # Repository interfaces
├── service/                   # Background services
├── receiver/                  # Broadcast receivers
├── ui/
│   ├── screens/
│   │   ├── login/            # Company login
│   │   ├── firstlaunch/      # Initial setup
│   │   ├── library/          # Recording library
│   │   ├── playback/         # Audio playback
│   │   └── settings/         # App settings
│   ├── theme/                # Material 3 theme
│   ├── MainActivity.kt       # Entry point
│   ├── AppNavigation.kt      # Navigation setup
│   └── MainViewModel.kt      # Main state management
├── di/                       # Hilt dependency injection
└── SecureCallArchiveApp.kt  # Application class
```

## Setup Instructions

### Prerequisites
- Android Studio Giraffe or higher
- Android SDK 26+ (API Level 26)
- Gradle 8.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/secure-call-archive.git
   cd secure-call-archive
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

### First Launch
1. Login with your company email (user@vyapar.com or user@vyaparapp.in)
2. Choose storage folder using Storage Access Framework
3. Set admin password (minimum 6 characters)
4. Grant call recording permissions
5. App is ready to use

## Permissions

### Required Permissions
```xml
<!-- Call Recording -->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />

<!-- Storage -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Foreground Service -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

<!-- Boot Completion -->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

<!-- Internet (for login) -->
<uses-permission android:name="android.permission.INTERNET" />
```

### Permission Flow
- App requests permissions on-demand
- Clear explanation for each permission
- Graceful degradation if permissions denied

## Authentication

### Company Login
- Email validation for Vyapar domains
- Secure credential handling
- Session management via DataStore

### Password Protection
- Default password: `@Vyapar6202@` (must change on first setup)
- Encrypted storage using Android Keystore
- Password hashing for added security

### Biometric Authentication
- Optional fingerprint/face recognition
- Falls back to password if biometric unavailable
- Device credential as secondary auth

## Security Practices

### Data Protection
- Encrypted preferences using EncryptedSharedPreferences
- Android Keystore for key management
- No plain-text password storage

### Recording Privacy
- Recordings stored locally only
- No automatic cloud sync
- User controls export/backup
- Option to export recordings securely

### Session Security
- Automatic logout after inactivity (configurable)
- Secure preference encryption
- Biometric verification for sensitive operations

## Error Handling

### Graceful Degradation
- Detection of unsupported devices
- Fallback behavior when recording unavailable
- Clear user messaging
- Logging for debugging

### Exception Handling
- Try-catch blocks for all risky operations
- Timber logging for troubleshooting
- User-friendly error messages

## Building & Deployment

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Signing Configuration
Configure in `local.properties` or use Android Studio's signing wizard:
```gradle
signingConfigs {
    release {
        storeFile file("keystore.jks")
        storePassword = System.getenv("KEYSTORE_PASSWORD")
        keyAlias = System.getenv("KEY_ALIAS")
        keyPassword = System.getenv("KEY_PASSWORD")
    }
}
```

## Testing

### Unit Tests
```bash
./gradlew testDebugUnitTest
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
- ViewModel tests
- Repository tests
- Database tests
- UI component tests

## Logging

The app uses **Timber** for logging:

```kotlin
Timber.d("Debug message")
Timber.e(exception, "Error message")
Timber.w("Warning message")
```

Logs are visible in Logcat with tag `SecureCallArchive`

## Legal & Compliance

### Call Recording Laws
- App respects local call recording regulations
- User is informed of restrictions
- No recording in prohibited regions
- Recording status always visible

### Privacy
- No personal data collection
- No third-party analytics
- No telemetry
- User controls data retention

## Troubleshooting

### Common Issues

**Recording not working?**
- Check permissions are granted
- Verify device supports call recording (Android 8+)
- Check storage permissions
- Ensure foreground service notification is visible

**Login fails?**
- Verify internet connection
- Use correct company email format
- Check email is registered in system
- Clear app cache and try again

**Storage access denied?**
- Grant storage permissions
- Re-select storage folder in settings
- Verify storage folder still exists

**Biometric not working?**
- Check device has biometric hardware
- Ensure at least one fingerprint/face enrolled
- Verify app has biometric permission
- Try disabling and re-enabling in settings

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, feature requests, or questions:
- Open an issue on GitHub
- Check existing documentation
- Contact: support@vyapar.com

## Version History

### v1.0 (Initial Release)
- Core call recording functionality
- Company authentication
- Recording library with playback
- Security features
- Settings management

---

**Last Updated**: 2024
**Maintained by**: Vyapar Team
