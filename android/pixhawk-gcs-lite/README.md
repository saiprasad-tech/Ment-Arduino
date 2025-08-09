# Pixhawk GCS Lite

A lightweight, modern Ground Control Station (GCS) app for Android built with Kotlin and Jetpack Compose, designed to work with MAVLink-compatible autopilots including PX4 and ArduPilot.

## 🚁 Features

### Core Functionality
- **Multi-Connection Support**: UDP (listen/unicast), TCP client, Bluetooth SPP
- **MAVLink Integration**: Full message parsing with support for heartbeat, telemetry, commands, parameters, and missions
- **Real-time HUD**: Live attitude indicator, flight data display, status indicators
- **Interactive Map**: Google Maps integration with vehicle tracking, home marker, and breadcrumb trail
- **Flight Control**: Arm/Disarm, mode changes, takeoff/land, Return-to-Launch (RTL)
- **Mission Management**: Download, upload, and execute waypoint missions
- **Parameter Editor**: Browse, search, and modify autopilot parameters
- **Live Logging**: Real-time status messages and link quality monitoring

### User Experience
- **Material 3 Design**: Modern, polished UI with dynamic color theming
- **Bottom Navigation**: Intuitive tab-based navigation between major functions
- **Guided Setup**: Connection wizard with inline help and validation
- **Status Indicators**: Color-coded connection, GPS, battery, and arming states
- **Contextual Tooltips**: First-run guidance for new users
- **Dark/Light Themes**: Automatic theme switching based on system preference

### Technical Specifications
- **Architecture**: MVVM pattern with Kotlin Coroutines and Flow
- **Minimum SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)
- **MAVLink Library**: io.dronefleet:mavlink for robust protocol handling
- **Maps**: Google Maps with Compose integration
- **Networking**: Native UDP/TCP socket implementation with Bluetooth Classic SPP

## 📱 Screenshots

*Screenshots will be added once the app is built and tested*

- [ ] Connection screen with UDP/TCP/Bluetooth options
- [ ] Fly screen with map and HUD overlay
- [ ] Mission planning interface
- [ ] Parameter editor
- [ ] Settings and about screen

## 🛠 Building the App

### Prerequisites
- **Android Studio**: Electric Eel (2022.1.1) or later
- **JDK**: Version 17 or higher
- **Android SDK**: API 34 (compile) and minimum API 26
- **Gradle**: 8.4+ (included via wrapper)

### Build Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/saiprasad-tech/Ment-Arduino.git
   cd Ment-Arduino/android/pixhawk-gcs-lite
   ```

2. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

3. Find the APK in `app/build/outputs/apk/debug/`

### CI Build
The project includes GitHub Actions CI that automatically builds the APK and creates artifacts:
- **Debug APK**: Ready to install on Android devices
- **Project Archive**: Complete source code zip for distribution

## 🗺 Google Maps Setup

The app is designed to work without a Google Maps API key, displaying a basic map view. For full functionality:

### Debug/Development Setup
1. Get a Google Maps API key from the [Google Cloud Console](https://console.cloud.google.com/)
2. Enable the "Maps SDK for Android" API
3. Add the key to your `local.properties` file:
   ```properties
   MAPS_API_KEY=your_api_key_here
   ```

### Release Setup
For production builds, configure the API key in your build system or CI/CD pipeline.

### Running Without API Key
The app will function normally without an API key, showing:
- Basic map tiles (may be limited)
- All vehicle tracking and marker functionality
- Complete HUD and telemetry display

## 🔌 Connection Setup

### Connecting to PX4/ArduPilot SITL
1. Start your SITL instance (PX4 or ArduPilot)
2. In the app, go to **Connect** tab
3. Select **UDP** connection type
4. Set configuration:
   - **Host**: 127.0.0.1
   - **Port**: 14550
   - **Listen Mode**: ✅ Enabled (for SITL)
5. Tap **Connect**

### Connecting to Real Hardware
1. Configure your telemetry radio or WiFi module
2. In the app, select appropriate connection type:
   - **UDP**: For WiFi modules (set correct IP, disable listen mode)
   - **TCP**: For TCP telemetry bridges
   - **Bluetooth**: For Bluetooth telemetry radios (coming soon)
3. Enter connection details and connect

### Connection Types
- **UDP Listen**: Waits for incoming packets (SITL, some telemetry modules)
- **UDP Unicast**: Sends to specific host/port (WiFi modules)
- **TCP Client**: Connects to TCP server (mavlink-router, TCP bridges)
- **Bluetooth SPP**: Classic Bluetooth serial profile (planned feature)

## 🎮 Using the App

### Main Navigation
- **Connect**: Configure and establish MAVLink connection
- **Fly**: Primary flight screen with map and HUD
- **Missions**: Waypoint mission planning and execution
- **Params**: Parameter browsing and editing
- **Logs**: Live status messages and log management
- **Settings**: App preferences and information

### Flight Operations
1. **Connection**: Establish link using Connect tab
2. **Pre-flight**: Check status indicators (GPS, battery, link quality)
3. **Arming**: Use ARM button in Fly screen (safety checks apply)
4. **Flight Modes**: Long-press mode indicator to change modes
5. **Actions**: Use floating action buttons for takeoff, RTL, emergency land

### Safety Features
- **Pre-arm Checks**: Respects autopilot safety checks
- **Visual Indicators**: Clear status for GPS, battery, link quality
- **Confirmation Dialogs**: Critical actions require confirmation
- **Connection Monitoring**: Automatic detection of link loss

## ⚠️ Safety Notice and Disclaimer

**IMPORTANT**: This is experimental software intended for educational and development purposes.

### Safety Guidelines
- **Always maintain visual line of sight** with your aircraft
- **Have a safety pilot ready** to take manual control at all times
- **Test thoroughly in simulation** before using with real aircraft
- **Follow all local regulations** and safety guidelines for UAV operation
- **Understand your autopilot** and its safety features before flight

### Limitations
- This app is not certified for commercial or critical operations
- MAVLink implementation may not cover all edge cases
- Real-time performance depends on device capabilities and connection quality
- Maps require internet connection and may have limited offline capability

### Disclaimer
The developers are not responsible for any damage, injury, or legal issues arising from the use of this software. Use at your own risk and comply with all applicable laws and regulations.

## 🧪 Testing

### Manual Testing Checklist
- [ ] App launches successfully without crashes
- [ ] Connection wizard guides through UDP/TCP/Bluetooth setup
- [ ] UDP connection to 127.0.0.1:14550 establishes with SITL
- [ ] HUD displays live telemetry data (attitude, speed, altitude, GPS)
- [ ] Map shows vehicle position and home marker
- [ ] Arm/Disarm commands work correctly
- [ ] Mode changes are sent and acknowledged
- [ ] Mission download retrieves existing waypoints
- [ ] Simple mission upload works (takeoff, waypoints, RTL)
- [ ] Parameter list loads and editing attempts succeed/fail appropriately
- [ ] App handles connection loss gracefully
- [ ] App works without Google Maps API key (degraded maps)

### Automated Testing
Run the included unit tests:
```bash
./gradlew test
```

## 🔧 Development

### Project Structure
```
android/pixhawk-gcs-lite/
├── app/src/main/java/com/pixhawk/gcs/
│   ├── ui/                     # Compose UI screens and components
│   │   ├── main/              # Main navigation and app structure
│   │   ├── connect/           # Connection configuration
│   │   ├── fly/               # Flight screen with map and HUD
│   │   ├── missions/          # Mission planning
│   │   ├── params/            # Parameter management
│   │   ├── logs/              # Logging and status
│   │   ├── settings/          # App settings
│   │   ├── components/        # Reusable UI components
│   │   └── theme/             # Material 3 theming
│   ├── data/                  # Data layer
│   │   ├── connection/        # Connection management
│   │   ├── mavlink/           # MAVLink protocol handling
│   │   └── repository/        # Data repositories
│   └── domain/                # Domain models and use cases
│       ├── model/             # Data models
│       └── usecase/           # Business logic
├── app/src/main/res/          # Android resources
└── gradle/                    # Gradle wrapper and configuration
```

### Key Dependencies
- **Jetpack Compose**: Modern declarative UI framework
- **Material 3**: Latest Material Design components
- **Navigation Compose**: Type-safe navigation
- **Lifecycle ViewModel**: MVVM architecture support
- **Kotlin Coroutines**: Asynchronous programming
- **MAVLink Java**: Protocol implementation (io.dronefleet:mavlink)
- **Google Maps Compose**: Map integration
- **Accompanist**: Additional Compose utilities

### Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Acknowledgments

- [PX4 Autopilot](https://px4.io/) - Advanced open source autopilot
- [ArduPilot](https://ardupilot.org/) - Versatile open source autopilot
- [MAVLink](https://mavlink.io/) - Lightweight messaging protocol
- [QGroundControl](http://qgroundcontrol.com/) - Full-featured reference GCS
- [DroneFleet MAVLink](https://github.com/dronefleet/mavlink) - Excellent Java MAVLink library

---

**Note**: This README represents the complete implementation plan. Some features may be in various stages of development. Check the CI status and recent commits for current implementation status.