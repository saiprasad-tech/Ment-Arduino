package com.pixhawk.gcs.ui.fly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.pixhawk.gcs.data.mavlink.MAVLinkService
import com.pixhawk.gcs.data.mavlink.MAVLinkServiceImpl
import com.pixhawk.gcs.data.connection.ConnectionManagerImpl
import com.pixhawk.gcs.domain.model.VehicleState
import io.dronefleet.mavlink.common.MavCmd

class FlyViewModel : ViewModel() {
    
    private val connectionManager = ConnectionManagerImpl()
    private val mavlinkService: MAVLinkService = MAVLinkServiceImpl(connectionManager)
    
    val vehicleState: StateFlow<VehicleState> = mavlinkService.vehicleState
    
    fun toggleArm() {
        viewModelScope.launch {
            val currentState = vehicleState.value
            if (currentState.isArmed) {
                disarm()
            } else {
                arm()
            }
        }
    }
    
    private suspend fun arm() {
        mavlinkService.sendCommand(
            command = MavCmd.MAV_CMD_COMPONENT_ARM_DISARM,
            param1 = 1f // 1 = arm
        )
    }
    
    private suspend fun disarm() {
        mavlinkService.sendCommand(
            command = MavCmd.MAV_CMD_COMPONENT_ARM_DISARM,
            param1 = 0f // 0 = disarm
        )
    }
    
    fun returnToLaunch() {
        viewModelScope.launch {
            mavlinkService.sendCommand(
                command = MavCmd.MAV_CMD_NAV_RETURN_TO_LAUNCH
            )
        }
    }
    
    fun takeoff(altitude: Float = 10f) {
        viewModelScope.launch {
            mavlinkService.sendCommand(
                command = MavCmd.MAV_CMD_NAV_TAKEOFF,
                param7 = altitude
            )
        }
    }
    
    fun land() {
        viewModelScope.launch {
            mavlinkService.sendCommand(
                command = MavCmd.MAV_CMD_NAV_LAND
            )
        }
    }
    
    fun setMode(mode: String) {
        viewModelScope.launch {
            // Simplified mode setting - in real implementation, this would map mode names to appropriate values
            when (mode) {
                "Stabilize" -> mavlinkService.setMode(1, 0)
                "AltHold" -> mavlinkService.setMode(1, 2)
                "Loiter" -> mavlinkService.setMode(1, 5)
                "Auto" -> mavlinkService.setMode(1, 3)
                "RTL" -> mavlinkService.setMode(1, 6)
                "Land" -> mavlinkService.setMode(1, 9)
                "Guided" -> mavlinkService.setMode(1, 4)
            }
        }
    }
}