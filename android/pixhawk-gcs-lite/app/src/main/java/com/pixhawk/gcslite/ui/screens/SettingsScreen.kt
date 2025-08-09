package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )
        
        // Connection Settings
        SettingsSection(
            title = "Connection",
            icon = Icons.Default.Wifi
        ) {
            SettingsItem(
                title = "Auto-connect on startup",
                description = "Automatically connect to the last used configuration",
                icon = Icons.Default.PowerSettingsNew
            ) {
                var checked by remember { mutableStateOf(false) }
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
            
            SettingsItem(
                title = "Connection timeout",
                description = "Timeout for connection attempts (seconds)",
                icon = Icons.Default.Timer
            ) {
                Text("30s")
            }
        }
        
        // Display Settings
        SettingsSection(
            title = "Display",
            icon = Icons.Default.DisplaySettings
        ) {
            SettingsItem(
                title = "Dark theme",
                description = "Use dark theme for the application",
                icon = Icons.Default.DarkMode
            ) {
                var checked by remember { mutableStateOf(false) }
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
            
            SettingsItem(
                title = "HUD transparency",
                description = "Adjust HUD overlay transparency",
                icon = Icons.Default.Opacity
            ) {
                Text("90%")
            }
        }
        
        // Map Settings
        SettingsSection(
            title = "Map",
            icon = Icons.Default.Map
        ) {
            SettingsItem(
                title = "Map type",
                description = "Default map type for flight screen",
                icon = Icons.Default.Terrain
            ) {
                Text("Satellite")
            }
            
            SettingsItem(
                title = "Show flight path",
                description = "Display vehicle flight path on map",
                icon = Icons.Default.Timeline
            ) {
                var checked by remember { mutableStateOf(true) }
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }
        
        // Data Settings
        SettingsSection(
            title = "Data",
            icon = Icons.Default.Storage
        ) {
            SettingsItem(
                title = "Log level",
                description = "Minimum log level to record",
                icon = Icons.Default.BugReport
            ) {
                Text("INFO")
            }
            
            SettingsItem(
                title = "Data recording",
                description = "Automatically record flight data",
                icon = Icons.Default.Save
            ) {
                var checked by remember { mutableStateOf(true) }
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }
        
        // About Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Pixhawk GCS Lite v1.0",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Lightweight ground control station for ArduPilot vehicles",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    action: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            action()
        }
    }
}