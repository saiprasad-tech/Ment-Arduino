package com.pixhawk.gcslite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixhawk.gcslite.data.ConnectionSettings
import com.pixhawk.gcslite.repository.VehicleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConnectViewModel : ViewModel() {
    private val repository = VehicleRepository()
    
    private val _host = MutableStateFlow("127.0.0.1")
    val host: StateFlow<String> = _host.asStateFlow()
    
    private val _port = MutableStateFlow("14550")
    val port: StateFlow<String> = _port.asStateFlow()
    
    val connectionState = repository.connectionState
    
    fun updateHost(newHost: String) {
        _host.value = newHost
    }
    
    fun updatePort(newPort: String) {
        _port.value = newPort
    }
    
    fun connect() {
        viewModelScope.launch {
            try {
                val settings = ConnectionSettings(
                    host = _host.value,
                    port = _port.value.toIntOrNull() ?: 14550
                )
                repository.connect(settings)
            } catch (e: Exception) {
                // Handle connection error
                println("Connection error: ${e.message}")
            }
        }
    }
    
    fun disconnect() {
        repository.disconnect()
    }
    
    override fun onCleared() {
        super.onCleared()
        repository.cleanup()
    }
}