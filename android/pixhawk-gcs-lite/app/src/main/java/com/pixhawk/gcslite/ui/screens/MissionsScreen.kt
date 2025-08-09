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

data class Mission(
    val id: Int,
    val name: String,
    val waypointCount: Int,
    val isActive: Boolean = false
)

@Composable
fun MissionsScreen() {
    var missions by remember {
        mutableStateOf(
            listOf(
                Mission(1, "Test Mission 1", 5),
                Mission(2, "Survey Pattern", 12, isActive = true),
                Mission(3, "Return Home", 2)
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* TODO: Download mission */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Download")
            }
            OutlinedButton(
                onClick = { /* TODO: Upload mission */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Upload")
            }
            OutlinedButton(
                onClick = { /* TODO: Start mission */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Start")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Mission list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(missions) { mission ->
                MissionItem(
                    mission = mission,
                    onMissionClick = { /* TODO: Handle mission selection */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionItem(
    mission: Mission,
    onMissionClick: (Mission) -> Unit
) {
    Card(
        onClick = { onMissionClick(mission) },
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (mission.isActive) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (mission.isActive) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mission.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (mission.isActive) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Active") }
                    )
                }
            }
            Text(
                text = "${mission.waypointCount} waypoints",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}