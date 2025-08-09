package com.pixhawk.gcs.ui.logs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pixhawk.gcs.ui.theme.PixhawkGCSLiteTheme
import java.text.SimpleDateFormat
import java.util.*

data class LogEntry(
    val timestamp: Long,
    val severity: String,
    val message: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {
    var logEntries by remember { 
        mutableStateOf(
            listOf(
                LogEntry(System.currentTimeMillis() - 10000, "INFO", "MAVLink connection established"),
                LogEntry(System.currentTimeMillis() - 8000, "INFO", "GPS lock acquired - 8 satellites"),
                LogEntry(System.currentTimeMillis() - 5000, "WARN", "Low battery warning - 25%"),
                LogEntry(System.currentTimeMillis() - 3000, "INFO", "Flight mode changed to Loiter"),
                LogEntry(System.currentTimeMillis() - 1000, "INFO", "Vehicle armed")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header card with placeholder info
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
                    text = "Flight Logs",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Real-time status messages and basic link logs. Future versions will support downloading and viewing .ulg/.bin flight log files.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Planned Features",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                val todoItems = listOf(
                    "Download .ulg flight logs from PX4",
                    "Download .bin flight logs from ArduPilot",
                    "Basic log analysis and visualization",
                    "Export logs to external storage",
                    "Log filtering and search"
                )
                
                todoItems.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "• ",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* TODO: Implement download */ },
                modifier = Modifier.weight(1f),
                enabled = false
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Download Logs")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live logs section
        Text(
            text = "Live Status Messages",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        if (logEntries.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No log entries yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(logEntries.reversed()) { entry ->
                    LogEntryCard(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun LogEntryCard(entry: LogEntry) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (entry.severity) {
                "ERROR" -> MaterialTheme.colorScheme.errorContainer
                "WARN" -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = dateFormat.format(Date(entry.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.width(60.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = entry.severity,
                style = MaterialTheme.typography.labelSmall,
                color = when (entry.severity) {
                    "ERROR" -> MaterialTheme.colorScheme.onErrorContainer
                    "WARN" -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.width(40.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = entry.message,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogsScreenPreview() {
    PixhawkGCSLiteTheme {
        LogsScreen()
    }
}