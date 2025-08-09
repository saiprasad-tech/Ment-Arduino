package com.pixhawk.gcs.domain.model

data class VehicleState(
    val isConnected: Boolean = false,
    val isArmed: Boolean = false,
    val flightMode: String = "Unknown",
    val gpsFixType: Int = 0,
    val satelliteCount: Int = 0,
    val batteryPercentage: Float = 0f,
    val batteryVoltage: Float = 0f,
    val groundSpeed: Float = 0f,
    val airSpeed: Float = 0f,
    val altitudeRelative: Float = 0f,
    val altitudeAMSL: Float = 0f,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val roll: Float = 0f,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val homeLatitude: Double = 0.0,
    val homeLongitude: Double = 0.0,
    val linkQuality: Int = 0,
    val autopilotType: String = "Unknown",
    val systemId: Int = 0,
    val componentId: Int = 0
)

enum class ConnectionType {
    UDP, TCP, BLUETOOTH
}

data class ConnectionConfig(
    val type: ConnectionType = ConnectionType.UDP,
    val host: String = "127.0.0.1",
    val port: Int = 14550,
    val bluetoothAddress: String = "",
    val bluetoothName: String = "",
    val isServer: Boolean = true // For UDP: true = listen, false = send
)

enum class ConnectionStatus {
    DISCONNECTED, CONNECTING, CONNECTED, ERROR
}

data class ConnectionState(
    val status: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val config: ConnectionConfig = ConnectionConfig(),
    val errorMessage: String = "",
    val lastHeartbeat: Long = 0L
)

data class MissionItem(
    val sequence: Int,
    val frame: Int,
    val command: Int,
    val current: Boolean,
    val autocontinue: Boolean,
    val param1: Float,
    val param2: Float,
    val param3: Float,
    val param4: Float,
    val x: Float, // latitude or x coordinate
    val y: Float, // longitude or y coordinate  
    val z: Float, // altitude or z coordinate
    val missionType: Int = 0
)

data class Parameter(
    val id: String,
    val value: Float,
    val type: Int,
    val description: String = ""
)