package com.pixhawk.gcs.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixhawk.gcs.domain.model.VehicleState
import com.pixhawk.gcs.ui.theme.*
import kotlin.math.*

@Composable
fun HUDOverlay(
    vehicleState: VehicleState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Top status bar
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(
                    Color.Black.copy(alpha = 0.7f),
                    RoundedCornerShape(8.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.8f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flight Mode
                StatusChip(
                    label = "MODE",
                    value = vehicleState.flightMode,
                    color = MaterialTheme.colorScheme.primary
                )
                
                // Armed Status
                StatusChip(
                    label = "ARM",
                    value = if (vehicleState.isArmed) "ARMED" else "DISARMED",
                    color = if (vehicleState.isArmed) StatusArmed else StatusDisarmed
                )
                
                // GPS Status
                StatusChip(
                    label = "GPS",
                    value = "${vehicleState.satelliteCount} SAT",
                    color = when {
                        vehicleState.satelliteCount >= 6 -> GPSGood
                        vehicleState.satelliteCount >= 3 -> GPSWarning
                        else -> GPSNoFix
                    }
                )
            }
        }

        // Left side - Speed and Altitude tapes
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
        ) {
            // Speed tape
            SpeedTape(
                groundSpeed = vehicleState.groundSpeed,
                airSpeed = vehicleState.airSpeed,
                modifier = Modifier.width(80.dp).height(200.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
        ) {
            // Altitude tape
            AltitudeTape(
                altitude = vehicleState.altitudeRelative,
                modifier = Modifier.width(80.dp).height(200.dp)
            )
        }

        // Center - Attitude indicator
        AttitudeIndicator(
            pitch = vehicleState.pitch,
            roll = vehicleState.roll,
            modifier = Modifier
                .align(Alignment.Center)
                .size(120.dp)
        )

        // Bottom status bar
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .background(
                    Color.Black.copy(alpha = 0.7f),
                    RoundedCornerShape(8.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black.copy(alpha = 0.8f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Battery
                BatteryIndicator(
                    percentage = vehicleState.batteryPercentage,
                    voltage = vehicleState.batteryVoltage
                )
                
                // Coordinates
                StatusChip(
                    label = "LAT",
                    value = String.format("%.6f", vehicleState.latitude),
                    color = MaterialTheme.colorScheme.secondary
                )
                
                StatusChip(
                    label = "LON", 
                    value = String.format("%.6f", vehicleState.longitude),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun BatteryIndicator(
    percentage: Float,
    voltage: Float,
    modifier: Modifier = Modifier
) {
    val batteryColor = when {
        percentage > 50f -> BatteryGood
        percentage > 20f -> BatteryWarning
        else -> BatteryCritical
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = batteryColor.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BATTERY",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "${percentage.toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${String.format("%.1f", voltage)}V",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun SpeedTape(
    groundSpeed: Float,
    airSpeed: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GS",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "${groundSpeed.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "AS",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "${airSpeed.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AltitudeTape(
    altitude: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ALT",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "${altitude.toInt()}m",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AttitudeIndicator(
    pitch: Float,
    roll: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawAttitudeIndicator(pitch, roll, size.width, size.height)
            }
        }
    }
}

private fun DrawScope.drawAttitudeIndicator(
    pitch: Float,
    roll: Float,
    width: Float,
    height: Float
) {
    val center = Offset(width / 2, height / 2)
    val radius = minOf(width, height) / 2 - 10.dp.toPx()

    // Draw sky (blue upper half)
    drawArc(
        color = Color(0xFF4FC3F7),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
    )

    // Draw ground (brown lower half)
    drawArc(
        color = Color(0xFF8D6E63),
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
    )

    // Draw horizon line
    drawLine(
        color = Color.White,
        start = Offset(center.x - radius, center.y),
        end = Offset(center.x + radius, center.y),
        strokeWidth = 3.dp.toPx()
    )

    // Draw aircraft symbol (center cross)
    val aircraftSize = 20.dp.toPx()
    drawLine(
        color = Color.Yellow,
        start = Offset(center.x - aircraftSize, center.y),
        end = Offset(center.x + aircraftSize, center.y),
        strokeWidth = 4.dp.toPx()
    )
    drawLine(
        color = Color.Yellow,
        start = Offset(center.x, center.y - aircraftSize / 2),
        end = Offset(center.x, center.y + aircraftSize / 2),
        strokeWidth = 4.dp.toPx()
    )

    // Draw outer circle
    drawCircle(
        color = Color.White,
        radius = radius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
    )
}

@Preview(showBackground = true)
@Composable
private fun HUDOverlayPreview() {
    PixhawkGCSLiteTheme {
        HUDOverlay(
            vehicleState = VehicleState(
                isConnected = true,
                isArmed = true,
                flightMode = "Loiter",
                satelliteCount = 8,
                batteryPercentage = 75f,
                batteryVoltage = 12.6f,
                groundSpeed = 5.2f,
                airSpeed = 12.1f,
                altitudeRelative = 15.3f,
                pitch = 5f,
                roll = -2f,
                latitude = 37.7749,
                longitude = -122.4194
            )
        )
    }
}