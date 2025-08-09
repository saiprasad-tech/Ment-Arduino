package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pixhawk.gcslite.data.ConnectionState
import com.pixhawk.gcslite.mavlink.ConnectionManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FlyScreen() {
    val connectionManager = remember { ConnectionManager() }
    val connectionState by connectionManager.connectionState.collectAsState()
    val flightData by connectionManager.flightData.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Background map placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Google Maps\n(Add API Key to Enable)",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // HUD Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top HUD Panel
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HudItem("MODE", flightData.flightMode)
                    HudItem("ARM", if (flightData.isArmed) "ARMED" else "DISARMED")
                    HudItem("LINK", "${flightData.linkQuality}%")
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom HUD Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HudItem("BATTERY", "${flightData.batteryVoltage}V")
                        HudItem("SATS", "${flightData.satelliteCount}")
                        HudItem(
                            "STATUS",
                            when (connectionState) {
                                ConnectionState.CONNECTED -> "ONLINE"
                                ConnectionState.CONNECTING -> "CONN..."
                                ConnectionState.DISCONNECTED -> "OFFLINE"
                                ConnectionState.ERROR -> "ERROR"
                            }
                        )
                    }
                    
                    if (flightData.lastHeartbeat > 0) {
                        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        Text(
                            text = "Last Heartbeat: ${formatter.format(Date(flightData.lastHeartbeat))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
        
        // Connection warning overlay
        if (connectionState != ConnectionState.CONNECTED) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No Vehicle Connection",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Go to Connect tab to establish connection",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HudItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}