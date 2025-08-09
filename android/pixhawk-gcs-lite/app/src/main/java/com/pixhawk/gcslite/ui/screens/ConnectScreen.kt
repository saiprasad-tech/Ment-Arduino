package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pixhawk.gcslite.data.ConnectionState
import com.pixhawk.gcslite.data.UdpSettings
import com.pixhawk.gcslite.mavlink.ConnectionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen() {
    val connectionManager = remember { ConnectionManager() }
    val connectionState by connectionManager.connectionState.collectAsState()
    
    var host by remember { mutableStateOf("127.0.0.1") }
    var port by remember { mutableStateOf("14550") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Connection Settings",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = host,
                    onValueChange = { host = it },
                    label = { Text("UDP Host") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = connectionState == ConnectionState.DISCONNECTED
                )
                
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("UDP Port") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = connectionState == ConnectionState.DISCONNECTED
                )
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Connection Status",
                    style = MaterialTheme.typography.titleMedium
                )
                
                val statusText = when (connectionState) {
                    ConnectionState.DISCONNECTED -> "Disconnected"
                    ConnectionState.CONNECTING -> "Connecting..."
                    ConnectionState.CONNECTED -> "Connected"
                    ConnectionState.ERROR -> "Error"
                }
                
                val statusColor = when (connectionState) {
                    ConnectionState.DISCONNECTED -> MaterialTheme.colorScheme.onSurface
                    ConnectionState.CONNECTING -> MaterialTheme.colorScheme.primary
                    ConnectionState.CONNECTED -> MaterialTheme.colorScheme.primary
                    ConnectionState.ERROR -> MaterialTheme.colorScheme.error
                }
                
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = statusColor
                )
                
                Button(
                    onClick = {
                        if (connectionState == ConnectionState.CONNECTED) {
                            connectionManager.disconnect()
                        } else {
                            val settings = UdpSettings(
                                host = host.takeIf { it.isNotBlank() } ?: "127.0.0.1",
                                port = port.toIntOrNull() ?: 14550
                            )
                            connectionManager.connect(settings)
                        }
                    },
                    enabled = connectionState != ConnectionState.CONNECTING,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (connectionState == ConnectionState.CONNECTED) "Disconnect" else "Connect"
                    )
                }
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    text = "• Start SITL or connect to real Pixhawk via UDP\n" +
                            "• Default SITL address is 127.0.0.1:14550\n" +
                            "• For remote connection, use vehicle's IP address\n" +
                            "• Make sure UDP port is not blocked by firewall",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}