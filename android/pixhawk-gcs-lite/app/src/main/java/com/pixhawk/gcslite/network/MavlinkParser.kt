package com.pixhawk.gcslite.network

import io.dronefleet.mavlink.MavlinkMessage
import io.dronefleet.mavlink.common.*
import io.dronefleet.mavlink.protocol.MavlinkPacket
import io.dronefleet.mavlink.serialization.MavlinkSerialization
import com.pixhawk.gcslite.data.VehicleState
import kotlinx.coroutines.flow.*

class MavlinkParser {
    private val mavlink = MavlinkSerialization.builder()
        .registerAll(io.dronefleet.mavlink.common.MavlinkCommonDialect())
        .build()
    
    internal val _vehicleState = MutableStateFlow(VehicleState())
    val vehicleState: StateFlow<VehicleState> = _vehicleState.asStateFlow()
    
    fun parseMessage(data: ByteArray) {
        try {
            val packet = mavlink.parse(data)
            if (packet is MavlinkPacket) {
                processMessage(packet.payload)
            }
        } catch (e: Exception) {
            // Log parsing error, but don't crash
            println("MAVLink parsing error: ${e.message}")
        }
    }
    
    private fun processMessage(message: MavlinkMessage<*>) {
        val currentState = _vehicleState.value
        
        when (message) {
            is Heartbeat -> {
                val newState = currentState.copy(
                    connected = true,
                    armed = (message.baseMode() and MavModeFlag.MAV_MODE_FLAG_SAFETY_ARMED.value) != 0,
                    mode = getModeString(message.customMode()),
                    lastHeartbeat = System.currentTimeMillis()
                )
                _vehicleState.value = newState
            }
            
            is SysStatus -> {
                val batteryPercent = (message.batteryRemaining() / 255.0 * 100).toInt()
                val newState = currentState.copy(
                    batteryPercent = batteryPercent,
                    batteryVoltage = message.voltageBattery() / 1000f
                )
                _vehicleState.value = newState
            }
            
            is GlobalPositionInt -> {
                val newState = currentState.copy(
                    latitude = message.lat() / 1e7,
                    longitude = message.lon() / 1e7,
                    altitude = message.relativeAlt() / 1000f
                )
                _vehicleState.value = newState
            }
            
            is GpsRawInt -> {
                val newState = currentState.copy(
                    satellites = message.satellitesVisible()
                )
                _vehicleState.value = newState
            }
            
            is Attitude -> {
                val newState = currentState.copy(
                    yaw = Math.toDegrees(message.yaw().toDouble()).toFloat(),
                    pitch = Math.toDegrees(message.pitch().toDouble()).toFloat(),
                    roll = Math.toDegrees(message.roll().toDouble()).toFloat()
                )
                _vehicleState.value = newState
            }
            
            is VfrHud -> {
                val newState = currentState.copy(
                    groundSpeed = message.groundspeed(),
                    airSpeed = message.airspeed(),
                    altitude = message.alt()
                )
                _vehicleState.value = newState
            }
            
            is HomePosition -> {
                val newState = currentState.copy(
                    homeLatitude = message.latitude() / 1e7,
                    homeLongitude = message.longitude() / 1e7
                )
                _vehicleState.value = newState
            }
        }
    }
    
    private fun getModeString(customMode: Long): String {
        // This is a simplified mode mapping for ArduPilot
        // In a real implementation, you'd need to check the autopilot type
        return when (customMode.toInt()) {
            0 -> "STABILIZE"
            1 -> "ACRO"
            2 -> "ALT_HOLD"
            3 -> "AUTO"
            4 -> "GUIDED"
            5 -> "LOITER"
            6 -> "RTL"
            7 -> "CIRCLE"
            9 -> "LAND"
            16 -> "POSHOLD"
            17 -> "BRAKE"
            18 -> "THROW"
            19 -> "AVOID_ADSB"
            20 -> "GUIDED_NOGPS"
            21 -> "SMART_RTL"
            22 -> "FLOWHOLD"
            23 -> "FOLLOW"
            24 -> "ZIGZAG"
            25 -> "SYSTEMID"
            26 -> "AUTOROTATE"
            else -> "UNKNOWN"
        }
    }
    
    fun createArmDisarmCommand(arm: Boolean): ByteArray {
        val command = CommandLong.builder()
            .command(MavCmd.MAV_CMD_COMPONENT_ARM_DISARM)
            .param1(if (arm) 1f else 0f)
            .param2(0f)
            .param3(0f)
            .param4(0f)
            .param5(0f)
            .param6(0f)
            .param7(0f)
            .targetSystem(1)
            .targetComponent(1)
            .build()
            
        return serializeMessage(command)
    }
    
    fun createRtlCommand(): ByteArray {
        val command = CommandLong.builder()
            .command(MavCmd.MAV_CMD_NAV_RETURN_TO_LAUNCH)
            .param1(0f)
            .param2(0f)
            .param3(0f)
            .param4(0f)
            .param5(0f)
            .param6(0f)
            .param7(0f)
            .targetSystem(1)
            .targetComponent(1)
            .build()
            
        return serializeMessage(command)
    }
    
    fun createTakeoffCommand(altitude: Float): ByteArray {
        val command = CommandLong.builder()
            .command(MavCmd.MAV_CMD_NAV_TAKEOFF)
            .param1(0f) // pitch
            .param2(0f)
            .param3(0f)
            .param4(0f) // yaw
            .param5(0f) // latitude
            .param6(0f) // longitude  
            .param7(altitude)
            .targetSystem(1)
            .targetComponent(1)
            .build()
            
        return serializeMessage(command)
    }
    
    private fun serializeMessage(message: MavlinkMessage<*>): ByteArray {
        val packet = MavlinkPacket.create(1, 1, message)
        return mavlink.serialize(packet)
    }
}