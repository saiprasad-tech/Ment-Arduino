package com.pixhawk.gcs.ui.missions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixhawk.gcs.domain.model.MissionItem
import com.pixhawk.gcs.ui.theme.PixhawkGCSLiteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionsScreen(
    viewModel: MissionsViewModel = viewModel()
) {
    val missionItems by viewModel.missionItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.downloadMission() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Download")
            }
            
            Button(
                onClick = { viewModel.uploadMission() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading && missionItems.isNotEmpty()
            ) {
                Icon(Icons.Default.Upload, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload")
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.startMission() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading && missionItems.isNotEmpty()
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Start Mission")
            }
            
            OutlinedButton(
                onClick = { viewModel.addSampleMission() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Sample")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (missionItems.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No mission items loaded",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Download existing mission or add sample waypoints",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            // Mission items list
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn {
                    itemsIndexed(missionItems) { index, item ->
                        MissionItemCard(
                            item = item,
                            index = index,
                            onRemove = { viewModel.removeMissionItem(index) }
                        )
                        if (index < missionItems.size - 1) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MissionItemCard(
    item: MissionItem,
    index: Int,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "#${item.sequence} - ${getCommandName(item.command)}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Lat: ${String.format("%.6f", item.x)}, " +
                      "Lon: ${String.format("%.6f", item.y)}, " +
                      "Alt: ${String.format("%.1f", item.z)}m",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            if (item.current) {
                Text(
                    text = "CURRENT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        TextButton(onClick = onRemove) {
            Text("Remove")
        }
    }
}

private fun getCommandName(command: Int): String {
    return when (command) {
        16 -> "Waypoint"
        22 -> "Takeoff"
        21 -> "Land"
        20 -> "Return to Launch"
        else -> "Command $command"
    }
}

@Preview(showBackground = true)
@Composable
private fun MissionsScreenPreview() {
    PixhawkGCSLiteTheme {
        MissionsScreen()
    }
}