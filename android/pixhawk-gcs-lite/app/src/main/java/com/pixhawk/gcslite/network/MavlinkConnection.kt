package com.pixhawk.gcslite.network

import com.pixhawk.gcslite.data.ConnectionSettings
import com.pixhawk.gcslite.data.ConnectionState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MavlinkConnection {
    private var socket: DatagramSocket? = null
    private var connectionJob: Job? = null
    
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _receivedData = MutableSharedFlow<ByteArray>()
    val receivedData: SharedFlow<ByteArray> = _receivedData.asSharedFlow()
    
    suspend fun connect(settings: ConnectionSettings) {
        disconnect()
        
        _connectionState.value = ConnectionState.CONNECTING
        
        try {
            val address = InetAddress.getByName(settings.host)
            socket = DatagramSocket().apply {
                connect(address, settings.port)
                soTimeout = 1000 // 1 second timeout for blocking operations
            }
            
            _connectionState.value = ConnectionState.CONNECTED
            
            // Start listening for incoming data
            connectionJob = CoroutineScope(Dispatchers.IO).launch {
                listenForData()
            }
            
        } catch (e: Exception) {
            _connectionState.value = ConnectionState.ERROR
            socket?.close()
            socket = null
            throw e
        }
    }
    
    fun disconnect() {
        connectionJob?.cancel()
        socket?.close()
        socket = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }
    
    private suspend fun listenForData() {
        val buffer = ByteArray(1024)
        
        while (socket?.isConnected == true && !Thread.currentThread().isInterrupted) {
            try {
                val packet = DatagramPacket(buffer, buffer.size)
                socket?.receive(packet)
                
                val data = packet.data.copyOfRange(0, packet.length)
                _receivedData.emit(data)
                
            } catch (e: Exception) {
                if (socket?.isClosed == false) {
                    _connectionState.value = ConnectionState.ERROR
                }
                break
            }
        }
    }
    
    suspend fun sendData(data: ByteArray) {
        socket?.let { socket ->
            if (socket.isConnected) {
                val packet = DatagramPacket(data, data.size)
                withContext(Dispatchers.IO) {
                    socket.send(packet)
                }
            }
        }
    }
    
    fun isConnected(): Boolean {
        return socket?.isConnected == true && _connectionState.value == ConnectionState.CONNECTED
    }
}