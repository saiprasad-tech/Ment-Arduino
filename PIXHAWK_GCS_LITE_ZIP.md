# Pixhawk GCS Lite Android Project

This repository contains a ZIP archive with a complete Android Studio project for **Pixhawk GCS Lite**, a lightweight Ground Control Station application for Pixhawk autopilots.

## Download and Setup

### Download the Project
- **File**: [android/pixhawk-gcs-lite.zip](android/pixhawk-gcs-lite.zip)
- **Size**: ~150KB (compressed Android Studio project)

### Opening in Android Studio

1. **Download the ZIP file** from this repository:
   ```
   https://github.com/saiprasad-tech/Ment-Arduino/blob/main/android/pixhawk-gcs-lite.zip
   ```

2. **Extract the ZIP file** to your desired location

3. **Open Android Studio** (latest version recommended)

4. **Import the project**:
   - Select "Open an existing Android Studio project"
   - Navigate to the extracted `pixhawk-gcs-lite` folder
   - Click "OK"

5. **Sync the project** (Android Studio will prompt you to sync Gradle files)

6. **Build and run** the app on your device or emulator

## App Features

- ✅ **UDP Connection**: Connect to Pixhawk via UDP (default: 127.0.0.1:14550)
- ✅ **MAVLink Integration**: Real-time vehicle communication using MAVLink protocol
- ✅ **Flight Display**: HUD showing flight mode, armed status, battery, GPS, and link quality
- ✅ **Google Maps**: Map view with graceful fallback when API key is not configured
- ✅ **Material 3 UI**: Modern Android interface with light/dark theme support
- ✅ **Navigation**: Bottom navigation bar with 6 tabs (Connect, Fly, Missions, Params, Logs, Settings)

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Architecture**: MVVM with StateFlow
- **Networking**: UDP Sockets + Kotlin Coroutines
- **MAVLink**: `io.dronefleet:mavlink` library
- **Maps**: Google Maps Compose (optional)
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

## Google Maps Setup (Optional)

The app runs without a Google Maps API key, but for full map functionality:

1. Get an API key from [Google Cloud Console](https://console.cloud.google.com/)
2. Enable "Maps SDK for Android"
3. Add to your `local.properties` file:
   ```
   MAPS_API_KEY=your_api_key_here
   ```

## Usage

1. **Connect**: Configure UDP host/port and connect to your vehicle
2. **Fly**: View real-time flight data and vehicle status
3. **Other tabs**: Placeholder screens for future features (missions, params, logs, settings)

## Development Notes

This is a **local build only** project - no CI/CD pipeline is included. The project is designed to be opened, built, and run locally in Android Studio.

The app includes:
- Connection management with state handling
- MAVLink message processing (basic heartbeat parsing)
- Simulated telemetry data for testing without a real vehicle
- Graceful error handling for missing Google Maps API key

## Project Structure

```
android/pixhawk-gcs-lite/
├── app/
│   ├── src/main/
│   │   ├── java/com/pixhawk/gcslite/
│   │   │   ├── data/           # Connection & MAVLink handling
│   │   │   ├── ui/             # Compose UI components
│   │   │   └── MainActivity.kt
│   │   ├── res/                # Android resources
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   ├── wrapper/               # Gradle wrapper files
│   └── libs.versions.toml     # Version catalog
├── settings.gradle.kts
├── build.gradle.kts
└── README.md
```

## Requirements

- Android Studio (latest version)
- JDK 17+
- Android SDK API 34
- Physical device or emulator running Android 8.0+ (API 26)

---

**Note**: This is a demonstration project showing Android development with Jetpack Compose, MAVLink integration, and Google Maps. It provides a foundation for building more advanced Ground Control Station features.