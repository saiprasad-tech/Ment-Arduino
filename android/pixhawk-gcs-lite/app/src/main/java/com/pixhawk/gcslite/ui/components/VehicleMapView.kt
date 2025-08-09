package com.pixhawk.gcslite.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.pixhawk.gcslite.data.VehicleState

@Composable
fun VehicleMapView(
    vehicleState: VehicleState,
    modifier: Modifier = Modifier
) {
    var hasApiKey by remember { mutableStateOf(true) }
    
    try {
        val vehiclePosition = LatLng(vehicleState.latitude, vehicleState.longitude)
        val homePosition = if (vehicleState.homeLatitude != null && vehicleState.homeLongitude != null) {
            LatLng(vehicleState.homeLatitude, vehicleState.homeLongitude)
        } else null
        
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                if (vehicleState.latitude != 0.0 && vehicleState.longitude != 0.0) {
                    vehiclePosition
                } else {
                    LatLng(37.7749, -122.4194) // Default to San Francisco
                },
                15f
            )
        }
        
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = false
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true
            )
        ) {
            // Vehicle marker
            if (vehicleState.latitude != 0.0 && vehicleState.longitude != 0.0) {
                Marker(
                    state = MarkerState(position = vehiclePosition),
                    title = "Vehicle",
                    snippet = "Mode: ${vehicleState.mode}, Alt: ${vehicleState.altitude}m"
                )
            }
            
            // Home marker
            homePosition?.let { home ->
                Marker(
                    state = MarkerState(position = home),
                    title = "Home",
                    snippet = "Launch location"
                )
            }
        }
        
    } catch (e: Exception) {
        // Fallback when Google Maps API is not available
        hasApiKey = false
    }
    
    if (!hasApiKey) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Map View\n(Google Maps integration)\n\nAdd MAPS_API_KEY to local.properties to enable maps\n\nVehicle: ${vehicleState.latitude}, ${vehicleState.longitude}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}