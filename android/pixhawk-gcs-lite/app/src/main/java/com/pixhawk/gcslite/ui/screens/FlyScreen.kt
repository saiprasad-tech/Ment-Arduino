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
import com.pixhawk.gcslite.R

@Composable
fun FlyScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // HUD Section
        HudOverlay(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        
        // Map placeholder (would contain Google Maps)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Map View\n(Google Maps integration)\n\nAdd MAPS_API_KEY to enable",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        
        // Action buttons
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Button(
                    onClick = { /* TODO: Implement arm/disarm */ }
                ) {
                    Text(stringResource(R.string.action_arm))
                }
            }
            item {
                OutlinedButton(
                    onClick = { /* TODO: Implement RTL */ }
                ) {
                    Text(stringResource(R.string.action_rtl))
                }
            }
            item {
                OutlinedButton(
                    onClick = { /* TODO: Implement takeoff */ }
                ) {
                    Text(stringResource(R.string.action_takeoff))
                }
            }
        }
    }
}

@Composable
private fun HudOverlay(modifier: Modifier = Modifier) {
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
                    HudItem("Mode", "STABILIZE")
                    HudItem("Armed", "DISARMED", isWarning = true)
                    HudItem("Link", "100%")
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HudItem("Sats", "12")
                    HudItem("Battery", "85%")
                    HudItem("GS", "0.0 m/s")
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HudItem("Alt", "0.0 m")
                    HudItem("Yaw", "0°")
                    HudItem("Pitch", "0°")
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