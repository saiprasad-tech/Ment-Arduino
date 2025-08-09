package com.pixhawk.gcs.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pixhawk.gcs.ui.theme.PixhawkGCSLiteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf("Normal") }
    var enableHaptics by remember { mutableStateOf(true) }
    var showTooltips by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Information
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Pixhawk GCS Lite",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Version 1.0.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "A lightweight Ground Control Station for MAVLink-compatible autopilots including PX4 and ArduPilot.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // App Settings
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Preferences",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Dark Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Dark Mode",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Use dark theme",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Map Type Selection
                Column {
                    Text(
                        text = "Map Type",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Choose map display style",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val mapTypes = listOf("Normal", "Satellite", "Terrain", "Hybrid")
                    
                    mapTypes.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = mapType == type,
                                onClick = { mapType = type }
                            )
                            Text(
                                text = type,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Haptic Feedback Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Haptic Feedback",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Vibrate on important actions",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Switch(
                        checked = enableHaptics,
                        onCheckedChange = { enableHaptics = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // First-run tooltips toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Show Tooltips",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Display helpful hints for first-time users",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Switch(
                        checked = showTooltips,
                        onCheckedChange = { showTooltips = it }
                    )
                }
            }
        }

        // Safety Notice
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "⚠️ Safety Notice",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "This is experimental software for educational and development purposes. " +
                          "Always maintain visual line of sight with your aircraft and have a safety pilot ready to take manual control. " +
                          "Follow all local regulations and safety guidelines.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Connection Instructions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📶 Connection Help",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "To connect to PX4/ArduPilot SITL:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Text(
                    text = "• Use UDP connection to 127.0.0.1:14550\n" +
                          "• Enable 'Listen mode' for SITL connections\n" +
                          "• For real hardware, use appropriate IP and disable listen mode",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    PixhawkGCSLiteTheme {
        SettingsScreen()
    }
}