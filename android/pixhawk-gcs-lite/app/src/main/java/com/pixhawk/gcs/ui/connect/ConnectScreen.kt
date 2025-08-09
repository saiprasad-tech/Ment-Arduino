package com.pixhawk.gcs.ui.connect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixhawk.gcs.domain.model.ConnectionStatus
import com.pixhawk.gcs.domain.model.ConnectionType
import com.pixhawk.gcs.ui.theme.PixhawkGCSLiteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen(
    viewModel: ConnectViewModel = viewModel()
) {
    val connectionState by viewModel.connectionState.collectAsState()
    val connectionConfig by viewModel.connectionConfig.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Connection Status Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Connection Status",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusColor = when (connectionState.status) {
                        ConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.primary
                        ConnectionStatus.CONNECTING -> MaterialTheme.colorScheme.secondary
                        ConnectionStatus.ERROR -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.outline
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(statusColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = connectionState.status.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                if (connectionState.errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = connectionState.errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Connection Configuration Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Connection Configuration",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Connection Type Selection
                Text(
                    text = "Connection Type",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Column(modifier = Modifier.selectableGroup()) {
                    ConnectionType.values().forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (connectionConfig.type == type),
                                    onClick = { viewModel.updateConnectionType(type) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (connectionConfig.type == type),
                                onClick = null
                            )
                            Text(
                                text = type.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Connection-specific settings
                when (connectionConfig.type) {
                    ConnectionType.UDP -> {
                        UDPConnectionSettings(
                            host = connectionConfig.host,
                            port = connectionConfig.port,
                            isServer = connectionConfig.isServer,
                            onHostChange = viewModel::updateHost,
                            onPortChange = viewModel::updatePort,
                            onServerModeChange = viewModel::updateServerMode
                        )
                    }
                    ConnectionType.TCP -> {
                        TCPConnectionSettings(
                            host = connectionConfig.host,
                            port = connectionConfig.port,
                            onHostChange = viewModel::updateHost,
                            onPortChange = viewModel::updatePort
                        )
                    }
                    ConnectionType.BLUETOOTH -> {
                        BluetoothConnectionSettings()
                    }
                }
            }
        }

        // Connection Button
        Button(
            onClick = {
                if (connectionState.status == ConnectionStatus.CONNECTED) {
                    viewModel.disconnect()
                } else {
                    viewModel.connect()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = connectionState.status != ConnectionStatus.CONNECTING
        ) {
            Text(
                text = when (connectionState.status) {
                    ConnectionStatus.CONNECTED -> "Disconnect"
                    ConnectionStatus.CONNECTING -> "Connecting..."
                    else -> "Connect"
                }
            )
        }
    }
}

@Composable
private fun UDPConnectionSettings(
    host: String,
    port: Int,
    isServer: Boolean,
    onHostChange: (String) -> Unit,
    onPortChange: (Int) -> Unit,
    onServerModeChange: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isServer,
                onCheckedChange = onServerModeChange
            )
            Text(
                text = "Listen mode (Server)",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        if (!isServer) {
            OutlinedTextField(
                value = host,
                onValueChange = onHostChange,
                label = { Text("Host") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        OutlinedTextField(
            value = port.toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.let { onPortChange(it) }
            },
            label = { Text("Port") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TCPConnectionSettings(
    host: String,
    port: Int,
    onHostChange: (String) -> Unit,
    onPortChange: (Int) -> Unit
) {
    Column {
        OutlinedTextField(
            value = host,
            onValueChange = onHostChange,
            label = { Text("Host") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = port.toString(),
            onValueChange = { value ->
                value.toIntOrNull()?.let { onPortChange(it) }
            },
            label = { Text("Port") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BluetoothConnectionSettings() {
    Column {
        Text(
            text = "Bluetooth SPP Connection",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bluetooth connection is not yet implemented.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectScreenPreview() {
    PixhawkGCSLiteTheme {
        ConnectScreen()
    }
}