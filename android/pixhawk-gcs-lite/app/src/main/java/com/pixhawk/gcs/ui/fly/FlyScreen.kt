package com.pixhawk.gcs.ui.fly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.pixhawk.gcs.ui.components.HUDOverlay
import com.pixhawk.gcs.ui.theme.PixhawkGCSLiteTheme

@Composable
fun FlyScreen(
    viewModel: FlyViewModel = viewModel()
) {
    val vehicleState by viewModel.vehicleState.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map
        val vehiclePosition = LatLng(vehicleState.latitude, vehicleState.longitude)
        val homePosition = LatLng(vehicleState.homeLatitude, vehicleState.homeLongitude)
        
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(vehiclePosition, 15f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { /* Handle map click */ }
        ) {
            // Vehicle marker
            if (vehicleState.latitude != 0.0 && vehicleState.longitude != 0.0) {
                Marker(
                    state = MarkerState(position = vehiclePosition),
                    title = "Vehicle",
                    snippet = "Mode: ${vehicleState.flightMode}"
                )
            }
            
            // Home marker
            if (vehicleState.homeLatitude != 0.0 && vehicleState.homeLongitude != 0.0) {
                Marker(
                    state = MarkerState(position = homePosition),
                    title = "Home",
                    snippet = "Launch point"
                )
            }
        }

        // HUD Overlay
        HUDOverlay(
            vehicleState = vehicleState,
            modifier = Modifier.fillMaxSize()
        )

        // Action buttons
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Arm/Disarm button
            FloatingActionButton(
                onClick = { viewModel.toggleArm() },
                containerColor = if (vehicleState.isArmed) 
                    MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (vehicleState.isArmed) 
                        Icons.Default.FlightLand else Icons.Default.FlightTakeoff,
                    contentDescription = if (vehicleState.isArmed) "Disarm" else "Arm"
                )
            }

            // RTL button
            FloatingActionButton(
                onClick = { viewModel.returnToLaunch() },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Return to Launch"
                )
            }

            // Re-center map button
            FloatingActionButton(
                onClick = { 
                    if (vehicleState.latitude != 0.0 && vehicleState.longitude != 0.0) {
                        val newPosition = CameraPosition.fromLatLngZoom(vehiclePosition, 15f)
                        // Note: In real implementation, we'd update the camera position
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Center on Vehicle"
                )
            }
        }

        // Connection status indicator
        if (!vehicleState.isConnected) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Not Connected",
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlyScreenPreview() {
    PixhawkGCSLiteTheme {
        FlyScreen()
    }
}