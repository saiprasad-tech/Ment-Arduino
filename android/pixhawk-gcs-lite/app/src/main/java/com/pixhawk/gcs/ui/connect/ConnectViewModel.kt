package com.pixhawk.gcs.ui.connect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.pixhawk.gcs.data.connection.ConnectionManager
import com.pixhawk.gcs.data.connection.ConnectionManagerImpl
import com.pixhawk.gcs.domain.model.ConnectionConfig
import com.pixhawk.gcs.domain.model.ConnectionState
import com.pixhawk.gcs.domain.model.ConnectionType

class ConnectViewModel : ViewModel() {
    
    private val connectionManager: ConnectionManager = ConnectionManagerImpl()
    
    val connectionState: StateFlow<ConnectionState> = connectionManager.connectionState
    
    private val _connectionConfig = MutableStateFlow(ConnectionConfig())
    val connectionConfig: StateFlow<ConnectionConfig> = _connectionConfig.asStateFlow()
    
    fun updateConnectionType(type: ConnectionType) {
        _connectionConfig.value = _connectionConfig.value.copy(type = type)
    }
    
    fun updateHost(host: String) {
        _connectionConfig.value = _connectionConfig.value.copy(host = host)
    }
    
    fun updatePort(port: Int) {
        _connectionConfig.value = _connectionConfig.value.copy(port = port)
    }
    
    fun updateServerMode(isServer: Boolean) {
        _connectionConfig.value = _connectionConfig.value.copy(isServer = isServer)
    }
    
    fun connect() {
        viewModelScope.launch {
            connectionManager.connect(_connectionConfig.value)
        }
    }
    
    fun disconnect() {
        viewModelScope.launch {
            connectionManager.disconnect()
        }
    }
}