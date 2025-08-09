package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pixhawk.gcslite.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen() {
    var host by remember { mutableStateOf("127.0.0.1") }
    var port by remember { mutableStateOf("14550") }
    var connectionStatus by remember { mutableStateOf("Disconnected") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Connection Settings",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = host,
                    onValueChange = { host = it },
                    label = { Text(stringResource(R.string.settings_connection_host)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text(stringResource(R.string.settings_connection_port)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Connection status
                Text(
                    text = "Status: $connectionStatus",
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (connectionStatus) {
                        "Connected" -> MaterialTheme.colorScheme.primary
                        "Connecting" -> MaterialTheme.colorScheme.secondary
                        "Error" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        connectionStatus = when (connectionStatus) {
                            "Disconnected", "Error" -> {
                                "Connecting"
                                // TODO: Implement actual connection logic
                            }
                            "Connecting", "Connected" -> {
                                "Disconnected"
                                // TODO: Implement disconnection logic
                            }
                            else -> connectionStatus
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (connectionStatus) {
                            "Disconnected", "Error" -> stringResource(R.string.action_connect)
                            else -> stringResource(R.string.action_disconnect)
                        }
                    )
                }
            }
        }
    }
}