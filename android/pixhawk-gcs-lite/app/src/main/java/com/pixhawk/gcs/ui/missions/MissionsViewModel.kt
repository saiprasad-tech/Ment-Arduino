package com.pixhawk.gcs.ui.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.pixhawk.gcs.data.mavlink.MAVLinkService
import com.pixhawk.gcs.data.mavlink.MAVLinkServiceImpl
import com.pixhawk.gcs.data.connection.ConnectionManagerImpl
import com.pixhawk.gcs.domain.model.MissionItem
import io.dronefleet.mavlink.common.MavCmd

class MissionsViewModel : ViewModel() {
    
    private val connectionManager = ConnectionManagerImpl()
    private val mavlinkService: MAVLinkService = MAVLinkServiceImpl(connectionManager)
    
    private val _missionItems = MutableStateFlow<List<MissionItem>>(emptyList())
    val missionItems: StateFlow<List<MissionItem>> = _missionItems.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun downloadMission() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                mavlinkService.requestMissionList()
                // In a real implementation, we'd listen for mission items and populate the list
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }
    
    fun uploadMission() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // In a real implementation, we'd upload the mission items
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }
    
    fun startMission() {
        viewModelScope.launch {
            try {
                mavlinkService.sendCommand(MavCmd.MAV_CMD_MISSION_START)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun addSampleMission() {
        val sampleMission = listOf(
            MissionItem(
                sequence = 0,
                frame = 3, // MAV_FRAME_GLOBAL_RELATIVE_ALT
                command = 22, // MAV_CMD_NAV_TAKEOFF
                current = false,
                autocontinue = true,
                param1 = 0f,
                param2 = 0f,
                param3 = 0f,
                param4 = 0f,
                x = 0f,
                y = 0f,
                z = 10f // 10m takeoff altitude
            ),
            MissionItem(
                sequence = 1,
                frame = 3,
                command = 16, // MAV_CMD_NAV_WAYPOINT
                current = true,
                autocontinue = true,
                param1 = 0f,
                param2 = 0f,
                param3 = 0f,
                param4 = 0f,
                x = 37.7749f, // San Francisco latitude
                y = -122.4194f, // San Francisco longitude
                z = 50f
            ),
            MissionItem(
                sequence = 2,
                frame = 3,
                command = 16, // MAV_CMD_NAV_WAYPOINT
                current = false,
                autocontinue = true,
                param1 = 0f,
                param2 = 0f,
                param3 = 0f,
                param4 = 0f,
                x = 37.7849f,
                y = -122.4094f,
                z = 50f
            ),
            MissionItem(
                sequence = 3,
                frame = 3,
                command = 20, // MAV_CMD_NAV_RETURN_TO_LAUNCH
                current = false,
                autocontinue = true,
                param1 = 0f,
                param2 = 0f,
                param3 = 0f,
                param4 = 0f,
                x = 0f,
                y = 0f,
                z = 0f
            )
        )
        _missionItems.value = sampleMission
    }
    
    fun removeMissionItem(index: Int) {
        val currentList = _missionItems.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            // Re-sequence the mission items
            val resequenced = currentList.mapIndexed { newIndex, item ->
                item.copy(sequence = newIndex)
            }
            _missionItems.value = resequenced
        }
    }
}