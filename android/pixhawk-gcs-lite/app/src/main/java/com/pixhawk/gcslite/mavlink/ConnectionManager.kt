package com.pixhawk.gcslite.mavlink

import com.pixhawk.gcslite.data.ConnectionState
import com.pixhawk.gcslite.data.FlightData
import com.pixhawk.gcslite.data.UdpSettings
import io.dronefleet.mavlink.MavlinkConnection
import io.dronefleet.mavlink.MavlinkMessage
import io.dronefleet.mavlink.common.Heartbeat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

class ConnectionManager {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _flightData = MutableStateFlow(FlightData())
    val flightData: StateFlow<FlightData> = _flightData.asStateFlow()
    
    private var connectionJob: Job? = null
    private var socket: DatagramSocket? = null
    private var mavlinkConnection: MavlinkConnection? = null
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    fun connect(settings: UdpSettings) {
        if (_connectionState.value == ConnectionState.CONNECTING || 
            _connectionState.value == ConnectionState.CONNECTED) {
            return
        }
        
        _connectionState.value = ConnectionState.CONNECTING
        
        connectionJob = coroutineScope.launch {
            try {
                // Create UDP socket
                socket = DatagramSocket()
                val address = InetSocketAddress(InetAddress.getByName(settings.host), settings.port)
                socket?.connect(address)
                
                // Create MAVLink connection
                mavlinkConnection = MavlinkConnection.create(socket?.inputStream, socket?.outputStream)
                
                _connectionState.value = ConnectionState.CONNECTED
                
                // Start listening for messages
                listenForMessages()
                
            } catch (e: Exception) {
                _connectionState.value = ConnectionState.ERROR
                disconnect()
            }
        }
    }
    
    fun disconnect() {
        connectionJob?.cancel()
        connectionJob = null
        
        try {
            mavlinkConnection?.close()
            socket?.close()
        } catch (e: IOException) {
            // Ignore
        }
        
        mavlinkConnection = null
        socket = null
        _connectionState.value = ConnectionState.DISCONNECTED
        
        // Reset flight data
        _flightData.value = FlightData()
    }
    
    private suspend fun listenForMessages() {
        try {
            mavlinkConnection?.let { connection ->
                while (_connectionState.value == ConnectionState.CONNECTED) {
                    try {
                        val message = connection.next()
                        processMessage(message)
                    } catch (e: Exception) {
                        // Connection error
                        break
                    }
                    
                    // Small delay to prevent busy loop
                    delay(10)
                }
            }
        } catch (e: Exception) {
            _connectionState.value = ConnectionState.ERROR
        } finally {
            if (_connectionState.value != ConnectionState.DISCONNECTED) {
                disconnect()
            }
        }
    }
    
    private fun processMessage(message: MavlinkMessage<*>) {
        when (val payload = message.payload) {
            is Heartbeat -> {
                val currentData = _flightData.value
                _flightData.value = currentData.copy(
                    flightMode = getFlightModeString(payload.customMode()),
                    isArmed = (payload.baseMode().value() and 128) != 0, // MAV_MODE_FLAG_SAFETY_ARMED
                    lastHeartbeat = System.currentTimeMillis()
                )
            }
            // Add other message types as needed
        }
    }
    
    private fun getFlightModeString(customMode: Int): String {
        // This is a simplified mapping for ArduPilot
        return when (customMode) {
            0 -> "STABILIZE"
            1 -> "ACRO"
            2 -> "ALT_HOLD"
            3 -> "AUTO"
            4 -> "GUIDED"
            5 -> "LOITER"
            6 -> "RTL"
            7 -> "CIRCLE"
            9 -> "LAND"
            else -> "MODE_$customMode"
        }
    }
}