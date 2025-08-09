package com.pixhawk.gcslite.repository

import com.pixhawk.gcslite.data.ConnectionSettings
import com.pixhawk.gcslite.data.ConnectionState
import com.pixhawk.gcslite.data.VehicleState
import com.pixhawk.gcslite.network.MavlinkConnection
import com.pixhawk.gcslite.network.MavlinkParser
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class VehicleRepository {
    private val connection = MavlinkConnection()
    private val parser = MavlinkParser()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    val connectionState: StateFlow<ConnectionState> = connection.connectionState
    val vehicleState: StateFlow<VehicleState> = parser.vehicleState
    
    init {
        // Connect parser to connection data flow
        scope.launch {
            connection.receivedData.collect { data ->
                parser.parseMessage(data)
            }
        }
        
        // Monitor connection health
        scope.launch {
            vehicleState.collect { state ->
                if (state.connected && System.currentTimeMillis() - state.lastHeartbeat > 5000) {
                    // No heartbeat for 5 seconds, consider disconnected
                    val disconnectedState = state.copy(connected = false)
                    parser._vehicleState.value = disconnectedState
                }
            }
        }
    }
    
    suspend fun connect(settings: ConnectionSettings) {
        connection.connect(settings)
    }
    
    fun disconnect() {
        connection.disconnect()
    }
    
    suspend fun armVehicle() {
        if (connection.isConnected()) {
            val command = parser.createArmDisarmCommand(true)
            connection.sendData(command)
        }
    }
    
    suspend fun disarmVehicle() {
        if (connection.isConnected()) {
            val command = parser.createArmDisarmCommand(false)
            connection.sendData(command)
        }
    }
    
    suspend fun returnToLaunch() {
        if (connection.isConnected()) {
            val command = parser.createRtlCommand()
            connection.sendData(command)
        }
    }
    
    suspend fun takeoff(altitude: Float = 10f) {
        if (connection.isConnected()) {
            val command = parser.createTakeoffCommand(altitude)
            connection.sendData(command)
        }
    }
    
    fun cleanup() {
        scope.cancel()
        connection.disconnect()
    }
}