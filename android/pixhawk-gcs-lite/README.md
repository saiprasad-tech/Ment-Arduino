# Pixhawk GCS Lite - Android Studio Project

A lightweight ground control station application for ArduPilot vehicles built with Kotlin and Jetpack Compose.

## Features

- **Modern UI**: Built with Jetpack Compose and Material 3 design
- **Bottom Navigation**: Six main tabs for different functionality
  - **Connect**: UDP connection configuration and management
  - **Fly**: Real-time HUD with flight data and map integration
  - **Missions**: Mission planning and management (placeholder)
  - **Params**: Parameter viewing and editing (placeholder)
  - **Logs**: Flight log viewing and management (placeholder)
  - **Settings**: Application configuration and preferences
- **MAVLink Integration**: Uses io.dronefleet:mavlink library for communication
- **Google Maps**: Integrated maps with Google Maps SDK (requires API key)
- **UDP Communication**: Connect to SITL or real vehicles via UDP

## Prerequisites

- Android Studio (latest version recommended)
- Minimum SDK: API 26 (Android 8.0)
- Target SDK: API 34 (Android 14)
- Kotlin 1.9.x
- Gradle 8.4

## Setup Instructions

1. **Open in Android Studio**:
   - Extract this ZIP file
   - Open Android Studio
   - Choose "Open an Existing Project"
   - Navigate to the extracted `pixhawk-gcs-lite` folder and select it

2. **Google Maps API Key** (Optional but recommended):
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing one
   - Enable Maps SDK for Android API
   - Create an API key for Android
   - Open `app/src/main/res/values/strings.xml`
   - Replace `YOUR_API_KEY_HERE` with your actual API key

3. **Build the Project**:
   ```bash
   ./gradlew build
   ```

4. **Run the App**:
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## Project Structure

```
app/src/main/java/com/pixhawk/gcslite/
├── MainActivity.kt                 # Main activity
├── data/                          # Data models
│   └── Models.kt                  # Connection state and flight data models
├── mavlink/                       # MAVLink communication
│   └── ConnectionManager.kt       # UDP connection and MAVLink handling
├── ui/
│   ├── navigation/               # Navigation components
│   │   ├── AppNavigation.kt      # Main navigation setup
│   │   └── Screen.kt             # Screen definitions
│   ├── screens/                  # Screen implementations
│   │   ├── ConnectScreen.kt      # UDP connection configuration
│   │   ├── FlyScreen.kt          # HUD and flight display
│   │   ├── MissionsScreen.kt     # Mission planning (placeholder)
│   │   ├── ParamsScreen.kt       # Parameter management (placeholder)
│   │   ├── LogsScreen.kt         # Log viewing (placeholder)
│   │   └── SettingsScreen.kt     # Application settings
│   └── theme/                    # Material 3 theming
│       ├── Color.kt              # Color definitions
│       ├── Theme.kt              # Theme setup
│       └── Type.kt               # Typography
```

## Dependencies

- **Jetpack Compose**: Modern Android UI toolkit
- **Material 3**: Google's latest Material Design
- **Navigation Compose**: Type-safe navigation
- **MAVLink**: `io.dronefleet:mavlink:2.2.1` for vehicle communication
- **Google Maps**: Maps SDK and Compose integration
- **Coroutines**: Asynchronous programming

## Usage

1. **Connect to Vehicle**:
   - Go to Connect tab
   - Configure UDP settings (default: 127.0.0.1:14550 for SITL)
   - Tap "Connect" button

2. **View Flight Data**:
   - Switch to Fly tab
   - See real-time HUD data when connected
   - Map shows vehicle location (with API key configured)

3. **Testing with SITL**:
   ```bash
   # Start ArduCopter SITL
   sim_vehicle.py -v ArduCopter --console --map
   
   # In the app, connect to 127.0.0.1:14550
   ```

## Known Limitations

- Mission planning is placeholder (UI only)
- Parameter editing is placeholder (UI only)
- Log management is placeholder (UI only)
- Maps require Google Maps API key to function
- Only UDP communication is implemented
- Basic MAVLink message handling (heartbeat only)

## Next Steps for Development

1. Implement full MAVLink message handling
2. Add mission planning functionality
3. Implement parameter read/write operations
4. Add log file management
5. Implement Bluetooth/Serial communication
6. Add flight mode switching controls
7. Enhance map features (flight path, waypoints)
8. Add telemetry logging and export

## License

This project is part of the Pixhawk GCS Lite initiative.

## Contributing

This is a starter project template. Contributions and enhancements are welcome.