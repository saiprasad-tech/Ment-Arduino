package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class LogEntry(
    val timestamp: Date,
    val level: String,
    val source: String,
    val message: String
)

@Composable
fun LogsScreen() {
    var logEntries by remember {
        mutableStateOf(
            listOf(
                LogEntry(
                    Date(),
                    "INFO",
                    "CONNECTION",
                    "UDP connection established to 127.0.0.1:14550"
                ),
                LogEntry(
                    Date(System.currentTimeMillis() - 5000),
                    "WARN",
                    "MAVLINK",
                    "No heartbeat received from vehicle"
                ),
                LogEntry(
                    Date(System.currentTimeMillis() - 10000),
                    "INFO",
                    "SYSTEM",
                    "Application started"
                ),
                LogEntry(
                    Date(System.currentTimeMillis() - 15000),
                    "DEBUG",
                    "GPS",
                    "GPS fix acquired: 12 satellites"
                ),
                LogEntry(
                    Date(System.currentTimeMillis() - 20000),
                    "ERROR",
                    "BATTERY",
                    "Low battery warning: 15% remaining"
                )
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "System Logs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { logEntries = emptyList() }
                ) {
                    Text("Clear")
                }
                Button(
                    onClick = { /* TODO: Export logs */ }
                ) {
                    Text("Export")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(logEntries.reversed()) { logEntry ->
                    LogEntryItem(logEntry)
                }
            }
        }
    }
}

@Composable
private fun LogEntryItem(logEntry: LogEntry) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (logEntry.level) {
                "ERROR" -> MaterialTheme.colorScheme.errorContainer
                "WARN" -> MaterialTheme.colorScheme.secondaryContainer
                "INFO" -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = timeFormat.format(logEntry.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = logEntry.level,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = logEntry.source,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = logEntry.message,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}