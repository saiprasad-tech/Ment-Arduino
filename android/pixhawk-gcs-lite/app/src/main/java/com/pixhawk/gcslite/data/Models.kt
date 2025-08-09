package com.pixhawk.gcslite.data

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

data class UdpSettings(
    val host: String = "127.0.0.1",
    val port: Int = 14550
)

data class FlightData(
    val flightMode: String = "UNKNOWN",
    val isArmed: Boolean = false,
    val linkQuality: Int = 0,
    val batteryVoltage: Float = 0f,
    val satelliteCount: Int = 0,
    val lastHeartbeat: Long = 0
)