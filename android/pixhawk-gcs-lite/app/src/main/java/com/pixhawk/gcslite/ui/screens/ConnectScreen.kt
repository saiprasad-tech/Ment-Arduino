package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixhawk.gcslite.R
import com.pixhawk.gcslite.data.ConnectionState
import com.pixhawk.gcslite.viewmodel.ConnectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen(
    viewModel: ConnectViewModel = viewModel()
) {
    val host by viewModel.host.collectAsState()
    val port by viewModel.port.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    
    val connectionStatusText = when (connectionState) {
        ConnectionState.DISCONNECTED -> stringResource(R.string.connection_status_disconnected)
        ConnectionState.CONNECTING -> stringResource(R.string.connection_status_connecting)
        ConnectionState.CONNECTED -> stringResource(R.string.connection_status_connected)
        ConnectionState.ERROR -> stringResource(R.string.connection_status_error)
    }
    
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
                    onValueChange = { viewModel.updateHost(it) },
                    label = { Text(stringResource(R.string.settings_connection_host)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = port,
                    onValueChange = { viewModel.updatePort(it) },
                    label = { Text(stringResource(R.string.settings_connection_port)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Connection status
                Text(
                    text = "Status: $connectionStatusText",
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (connectionState) {
                        ConnectionState.CONNECTED -> MaterialTheme.colorScheme.primary
                        ConnectionState.CONNECTING -> MaterialTheme.colorScheme.secondary
                        ConnectionState.ERROR -> MaterialTheme.colorScheme.error
                        ConnectionState.DISCONNECTED -> MaterialTheme.colorScheme.onSurface
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        when (connectionState) {
                            ConnectionState.DISCONNECTED, ConnectionState.ERROR -> viewModel.connect()
                            ConnectionState.CONNECTING, ConnectionState.CONNECTED -> viewModel.disconnect()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (connectionState) {
                            ConnectionState.DISCONNECTED, ConnectionState.ERROR -> stringResource(R.string.action_connect)
                            else -> stringResource(R.string.action_disconnect)
                        }
                    )
                }
            }
        }
    }
}