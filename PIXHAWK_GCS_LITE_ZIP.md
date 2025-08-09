# Pixhawk GCS Lite - Download & Build Instructions

## Quick Start

The complete Android Studio project is available as a ZIP archive for easy download and local development.

### Download

📦 **Download:** [android/pixhawk-gcs-lite.zip](./android/pixhawk-gcs-lite.zip)

### Extract and Open

1. Download the ZIP archive from this repository
2. Extract `pixhawk-gcs-lite.zip` to your desired location
3. Open Android Studio (Arctic Fox or newer recommended)
4. Choose **"Open an existing Android Studio project"**
5. Navigate to the extracted `pixhawk-gcs-lite` folder and click **"Open"**

### Build

1. Android Studio will automatically sync the project with Gradle
2. Wait for indexing and sync to complete
3. Build the project: **Build → Make Project** (Ctrl+F9)
4. Run on device/emulator: **Run → Run 'app'** (Shift+F10)

### Optional: Google Maps Setup

To enable the map view in the Fly tab:

1. Get a Google Maps API key from the [Google Cloud Console](https://console.cloud.google.com/)
2. In the project's `local.properties` file, replace:
   ```
   MAPS_API_KEY=YOUR_API_KEY_HERE
   ```
   with your actual API key
3. Rebuild the project

The app will work without a Maps API key, showing a placeholder in the map area.

## Features Included

✅ **Complete Android Studio Project** - Ready to build and run  
✅ **Material 3 UI** - Modern, polished interface with dark/light themes  
✅ **MAVLink Integration** - UDP telemetry communication (127.0.0.1:14550)  
✅ **Six Main Tabs** - Connect, Fly, Missions, Params, Logs, Settings  
✅ **Real-time Telemetry** - Vehicle status, GPS, attitude, battery monitoring  
✅ **Flight Controls** - Arm/Disarm, RTL, Takeoff commands  
✅ **Google Maps** - Vehicle tracking with markers (API key optional)  
✅ **Parameter Management** - Browse and edit vehicle parameters  
✅ **Mission Support** - Download, upload, and start missions  
✅ **System Logging** - Real-time log monitoring with filtering  

## Requirements

- **Android Studio:** Arctic Fox (2020.3.1) or newer
- **Android SDK:** Min API 26, Target API 34  
- **Java:** JDK 8 or newer
- **Gradle:** 8.4 (included via wrapper)

## Architecture

- **MVVM Pattern** with Jetpack Compose
- **Repository Pattern** for data management  
- **Kotlin Coroutines** for asynchronous operations
- **StateFlow/Flow** for reactive UI updates
- **MAVLink Java Library** (io.dronefleet:mavlink) for communication

The project follows Android development best practices and is ready for production use or further customization.