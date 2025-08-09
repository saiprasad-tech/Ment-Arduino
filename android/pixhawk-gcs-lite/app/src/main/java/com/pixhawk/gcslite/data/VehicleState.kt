package com.pixhawk.gcslite.data

data class VehicleState(
    val connected: Boolean = false,
    val armed: Boolean = false,
    val mode: String = "UNKNOWN",
    val satellites: Int = 0,
    val batteryPercent: Int = 0,
    val batteryVoltage: Float = 0f,
    val groundSpeed: Float = 0f,
    val airSpeed: Float = 0f,
    val altitude: Float = 0f,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val roll: Float = 0f,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val homeLatitude: Double? = null,
    val homeLongitude: Double? = null,
    val linkQuality: Int = 0,
    val lastHeartbeat: Long = 0L
)

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

data class ConnectionSettings(
    val host: String = "127.0.0.1",
    val port: Int = 14550
)