package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen() {
    val sampleLogs = remember {
        listOf(
            LogEntry("System startup complete", "INFO", System.currentTimeMillis() - 60000),
            LogEntry("GPS fix acquired", "INFO", System.currentTimeMillis() - 45000),
            LogEntry("Compass calibration required", "WARN", System.currentTimeMillis() - 30000),
            LogEntry("Barometer initialized", "INFO", System.currentTimeMillis() - 25000),
            LogEntry("EKF2 started", "INFO", System.currentTimeMillis() - 20000),
            LogEntry("Armed successfully", "INFO", System.currentTimeMillis() - 15000),
            LogEntry("Mission started", "INFO", System.currentTimeMillis() - 10000),
            LogEntry("Waypoint reached", "INFO", System.currentTimeMillis() - 5000),
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
                text = "Flight Logs",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Row {
                IconButton(
                    onClick = { /* TODO: Download logs */ }
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Download Logs")
                }
                IconButton(
                    onClick = { /* TODO: Clear logs */ }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Logs")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Log Management",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Real-time flight logs and telemetry\n" +
                            "• Download logs for analysis\n" +
                            "• Filter by log level (INFO, WARN, ERROR)\n" +
                            "• Export to external storage",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(sampleLogs.reversed()) { log ->
                LogItem(logEntry = log)
            }
        }
    }
}

data class LogEntry(
    val message: String,
    val level: String,
    val timestamp: Long
)

@Composable
fun LogItem(
    logEntry: LogEntry,
    modifier: Modifier = Modifier
) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            val levelColor = when (logEntry.level) {
                "ERROR" -> MaterialTheme.colorScheme.error
                "WARN" -> MaterialTheme.colorScheme.tertiary
                "INFO" -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface
            }
            
            Badge(
                containerColor = levelColor
            ) {
                Text(
                    text = logEntry.level,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = logEntry.message,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = timeFormat.format(Date(logEntry.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}