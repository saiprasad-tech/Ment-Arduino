package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixhawk.gcslite.R
import com.pixhawk.gcslite.data.ConnectionState
import com.pixhawk.gcslite.viewmodel.FlyViewModel
import com.pixhawk.gcslite.ui.components.VehicleMapView

@Composable
fun FlyScreen(
    viewModel: FlyViewModel = viewModel()
) {
    val vehicleState by viewModel.vehicleState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    
    // Collect action results for user feedback
    LaunchedEffect(Unit) {
        viewModel.actionResult.collect { result ->
            // In a real app, you'd show a snackbar here
            println("Action result: $result")
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // HUD Section
        HudOverlay(
            vehicleState = vehicleState,
            connectionState = connectionState,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        
        // Map section
        VehicleMapView(
            vehicleState = vehicleState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        )
        
        // Action buttons
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(
                    onClick = { 
                        if (vehicleState.armed) {
                            viewModel.disarmVehicle()
                        } else {
                            viewModel.armVehicle()
                        }
                    }
                ) {
                    Text(if (vehicleState.armed) stringResource(R.string.action_disarm) else stringResource(R.string.action_arm))
                }
            }
            item {
                OutlinedButton(
                    onClick = { viewModel.returnToLaunch() }
                ) {
                    Text(stringResource(R.string.action_rtl))
                }
            }
            item {
                OutlinedButton(
                    onClick = { viewModel.takeoff() }
                ) {
                    Text(stringResource(R.string.action_takeoff))
                }
            }
        }
    }
}

@Composable
private fun HudOverlay(
    vehicleState: com.pixhawk.gcslite.data.VehicleState,
    connectionState: ConnectionState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HudItem("Mode", vehicleState.mode)
                    HudItem(
                        "Armed", 
                        if (vehicleState.armed) "ARMED" else "DISARMED", 
                        isWarning = !vehicleState.armed
                    )
                    HudItem("Link", if (connectionState == ConnectionState.CONNECTED) "100%" else "0%")
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HudItem("Sats", "${vehicleState.satellites}")
                    HudItem("Battery", "${vehicleState.batteryPercent}%")
                    HudItem("GS", "%.1f m/s".format(vehicleState.groundSpeed))
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HudItem("Alt", "%.1f m".format(vehicleState.altitude))
                    HudItem("Yaw", "%.0f°".format(vehicleState.yaw))
                    HudItem("Pitch", "%.0f°".format(vehicleState.pitch))
                }
            }
        }
    }
}

@Composable
private fun HudItem(
    label: String,
    value: String,
    isWarning: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (isWarning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}