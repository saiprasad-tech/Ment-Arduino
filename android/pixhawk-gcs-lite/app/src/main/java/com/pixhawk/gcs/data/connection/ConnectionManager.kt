package com.pixhawk.gcs.data.connection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.pixhawk.gcs.domain.model.ConnectionConfig
import com.pixhawk.gcs.domain.model.ConnectionState
import com.pixhawk.gcs.domain.model.ConnectionStatus
import com.pixhawk.gcs.domain.model.ConnectionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

interface ConnectionManager {
    val connectionState: StateFlow<ConnectionState>
    val incomingData: Flow<ByteArray>
    
    suspend fun connect(config: ConnectionConfig): Result<Unit>
    suspend fun disconnect()
    suspend fun sendData(data: ByteArray): Result<Unit>
}

@Singleton
class ConnectionManagerImpl @Inject constructor() : ConnectionManager {
    
    private val _connectionState = MutableStateFlow(ConnectionState())
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _incomingData = MutableStateFlow<ByteArray>(byteArrayOf())
    override val incomingData: Flow<ByteArray> = _incomingData.asStateFlow()
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var connectionJob: Job? = null
    private var udpSocket: DatagramSocket? = null
    private var tcpSocket: Socket? = null
    
    override suspend fun connect(config: ConnectionConfig): Result<Unit> {
        return try {
            disconnect() // Clean up any existing connection
            
            _connectionState.value = _connectionState.value.copy(
                status = ConnectionStatus.CONNECTING,
                config = config
            )
            
            when (config.type) {
                ConnectionType.UDP -> connectUDP(config)
                ConnectionType.TCP -> connectTCP(config)
                ConnectionType.BLUETOOTH -> connectBluetooth(config)
            }
            
            _connectionState.value = _connectionState.value.copy(
                status = ConnectionStatus.CONNECTED,
                errorMessage = ""
            )
            
            Result.success(Unit)
        } catch (e: Exception) {
            _connectionState.value = _connectionState.value.copy(
                status = ConnectionStatus.ERROR,
                errorMessage = e.message ?: "Unknown connection error"
            )
            Result.failure(e)
        }
    }
    
    override suspend fun disconnect() {
        connectionJob?.cancel()
        udpSocket?.close()
        tcpSocket?.close()
        udpSocket = null
        tcpSocket = null
        
        _connectionState.value = _connectionState.value.copy(
            status = ConnectionStatus.DISCONNECTED,
            errorMessage = ""
        )
    }
    
    override suspend fun sendData(data: ByteArray): Result<Unit> {
        return try {
            when (_connectionState.value.config.type) {
                ConnectionType.UDP -> sendUDP(data)
                ConnectionType.TCP -> sendTCP(data)
                ConnectionType.BLUETOOTH -> sendBluetooth(data)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun connectUDP(config: ConnectionConfig) {
        udpSocket = DatagramSocket(if (config.isServer) config.port else null).apply {
            if (!config.isServer) {
                connect(InetSocketAddress(config.host, config.port))
            }
        }
        
        connectionJob = coroutineScope.launch {
            val buffer = ByteArray(1024)
            val packet = java.net.DatagramPacket(buffer, buffer.size)
            
            while (udpSocket?.isClosed == false) {
                try {
                    udpSocket?.receive(packet)
                    val data = packet.data.copyOf(packet.length)
                    _incomingData.value = data
                } catch (e: IOException) {
                    if (udpSocket?.isClosed == false) {
                        throw e
                    }
                    break
                }
            }
        }
    }
    
    private suspend fun connectTCP(config: ConnectionConfig) {
        tcpSocket = Socket(config.host, config.port)
        
        connectionJob = coroutineScope.launch {
            val buffer = ByteArray(1024)
            val inputStream = tcpSocket?.getInputStream()
            
            while (tcpSocket?.isClosed == false && inputStream != null) {
                try {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead > 0) {
                        val data = buffer.copyOf(bytesRead)
                        _incomingData.value = data
                    } else if (bytesRead == -1) {
                        break // End of stream
                    }
                } catch (e: IOException) {
                    if (tcpSocket?.isClosed == false) {
                        throw e
                    }
                    break
                }
            }
        }
    }
    
    private suspend fun connectBluetooth(config: ConnectionConfig) {
        // Bluetooth implementation would go here
        // For now, throw an exception to indicate it's not implemented
        throw UnsupportedOperationException("Bluetooth connection not yet implemented")
    }
    
    private suspend fun sendUDP(data: ByteArray) {
        val config = _connectionState.value.config
        val packet = java.net.DatagramPacket(
            data, data.size,
            java.net.InetAddress.getByName(config.host),
            config.port
        )
        udpSocket?.send(packet)
    }
    
    private suspend fun sendTCP(data: ByteArray) {
        tcpSocket?.getOutputStream()?.write(data)
    }
    
    private suspend fun sendBluetooth(data: ByteArray) {
        // Bluetooth send implementation would go here
        throw UnsupportedOperationException("Bluetooth connection not yet implemented")
    }
}