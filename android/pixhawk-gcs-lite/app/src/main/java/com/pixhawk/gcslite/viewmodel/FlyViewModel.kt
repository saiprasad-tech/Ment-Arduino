package com.pixhawk.gcslite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixhawk.gcslite.repository.VehicleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlyViewModel : ViewModel() {
    private val repository = VehicleRepository()
    
    val vehicleState = repository.vehicleState
    val connectionState = repository.connectionState
    
    private val _actionResult = MutableSharedFlow<String>()
    val actionResult: SharedFlow<String> = _actionResult.asSharedFlow()
    
    fun armVehicle() {
        viewModelScope.launch {
            try {
                repository.armVehicle()
                _actionResult.emit("Arm command sent")
            } catch (e: Exception) {
                _actionResult.emit("Arm command failed: ${e.message}")
            }
        }
    }
    
    fun disarmVehicle() {
        viewModelScope.launch {
            try {
                repository.disarmVehicle()
                _actionResult.emit("Disarm command sent")
            } catch (e: Exception) {
                _actionResult.emit("Disarm command failed: ${e.message}")
            }
        }
    }
    
    fun returnToLaunch() {
        viewModelScope.launch {
            try {
                repository.returnToLaunch()
                _actionResult.emit("RTL command sent")
            } catch (e: Exception) {
                _actionResult.emit("RTL command failed: ${e.message}")
            }
        }
    }
    
    fun takeoff() {
        viewModelScope.launch {
            try {
                repository.takeoff()
                _actionResult.emit("Takeoff command sent")
            } catch (e: Exception) {
                _actionResult.emit("Takeoff command failed: ${e.message}")
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        repository.cleanup()
    }
}