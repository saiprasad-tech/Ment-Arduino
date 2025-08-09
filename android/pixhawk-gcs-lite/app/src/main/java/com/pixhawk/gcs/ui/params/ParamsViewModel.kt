package com.pixhawk.gcs.ui.params

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import com.pixhawk.gcs.data.mavlink.MAVLinkService
import com.pixhawk.gcs.data.mavlink.MAVLinkServiceImpl
import com.pixhawk.gcs.data.connection.ConnectionManagerImpl
import com.pixhawk.gcs.domain.model.Parameter

class ParamsViewModel : ViewModel() {
    
    private val connectionManager = ConnectionManagerImpl()
    private val mavlinkService: MAVLinkService = MAVLinkServiceImpl(connectionManager)
    
    private val _parameters = MutableStateFlow<List<Parameter>>(emptyList())
    val parameters: StateFlow<List<Parameter>> = _parameters.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _editingParameter = MutableStateFlow<Parameter?>(null)
    val editingParameter: StateFlow<Parameter?> = _editingParameter.asStateFlow()
    
    // Filtered parameters based on search query
    val filteredParameters = combine(_parameters, _searchQuery) { params, query ->
        if (query.isBlank()) {
            params
        } else {
            params.filter { param ->
                param.id.contains(query, ignoreCase = true) ||
                param.description.contains(query, ignoreCase = true)
            }
        }
    }
    
    init {
        // Load sample parameters for demonstration
        loadSampleParameters()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun refreshParameters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                mavlinkService.requestParameterList()
                // In a real implementation, we'd listen for parameter responses
                // For now, just simulate loading
                kotlinx.coroutines.delay(1000)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }
    
    fun startEditing(parameter: Parameter) {
        _editingParameter.value = parameter
    }
    
    fun updateEditValue(value: String) {
        _editingParameter.value?.let { param ->
            value.toFloatOrNull()?.let { floatValue ->
                _editingParameter.value = param.copy(value = floatValue)
            }
        }
    }
    
    fun saveParameter() {
        _editingParameter.value?.let { param ->
            viewModelScope.launch {
                try {
                    val result = mavlinkService.setParameter(param.id, param.value)
                    if (result.isSuccess) {
                        // Update the parameter in our list
                        val updatedList = _parameters.value.map { p ->
                            if (p.id == param.id) param else p
                        }
                        _parameters.value = updatedList
                        _editingParameter.value = null
                    } else {
                        // Handle error - show toast or snackbar
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
    
    fun cancelEditing() {
        _editingParameter.value = null
    }
    
    private fun loadSampleParameters() {
        val sampleParams = listOf(
            Parameter(
                id = "ANGLE_MAX",
                value = 4500f,
                type = 9, // REAL32
                description = "Maximum lean angle in all flight modes"
            ),
            Parameter(
                id = "ALT_HOLD_RTL",
                value = 15.0f,
                type = 9,
                description = "RTL Altitude. This is the altitude the vehicle will move to as the first stage of Return to Launch."
            ),
            Parameter(
                id = "ARMING_CHECK",
                value = 1f,
                type = 6, // INT32
                description = "Arm Pre-Arm Checks to perform (bitmask)"
            ),
            Parameter(
                id = "BATT_CAPACITY",
                value = 5000f,
                type = 9,
                description = "Battery capacity in mAh"
            ),
            Parameter(
                id = "COMPASS_AUTODEC",
                value = 1f,
                type = 1, // UINT8
                description = "Auto Declination. Automatically compute the declination based on GPS location."
            ),
            Parameter(
                id = "FENCE_ENABLE",
                value = 0f,
                type = 1,
                description = "Fence enable/disable"
            ),
            Parameter(
                id = "GPS_TYPE",
                value = 1f,
                type = 1,
                description = "GPS type"
            ),
            Parameter(
                id = "RC1_MAX",
                value = 2000f,
                type = 4, // INT16
                description = "RC max PWM"
            ),
            Parameter(
                id = "RC1_MIN",
                value = 1000f,
                type = 4,
                description = "RC min PWM"
            ),
            Parameter(
                id = "WPNAV_SPEED",
                value = 500f,
                type = 9,
                description = "Waypoint Horizontal Speed Target"
            )
        )
        _parameters.value = sampleParams
    }
}