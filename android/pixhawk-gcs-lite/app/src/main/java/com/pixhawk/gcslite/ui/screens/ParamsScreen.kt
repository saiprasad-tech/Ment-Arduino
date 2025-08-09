package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamsScreen() {
    val sampleParams = remember {
        listOf(
            Parameter("ACCEL_Z_D", "0.010", "Accelerometer Z-axis D term"),
            Parameter("ACCEL_Z_I", "0.100", "Accelerometer Z-axis I term"),
            Parameter("ACCEL_Z_P", "0.200", "Accelerometer Z-axis P term"),
            Parameter("ANGLE_MAX", "4500", "Maximum lean angle in all flight modes"),
            Parameter("ARMING_CHECK", "1", "Arming checks enabled"),
            Parameter("BATT_CAPACITY", "5000", "Battery capacity in mAh"),
            Parameter("COMPASS_ENABLE", "1", "Compass enabled"),
            Parameter("FENCE_ENABLE", "0", "Fence disabled"),
            Parameter("GPS_TYPE", "1", "GPS type auto"),
            Parameter("LAND_SPEED", "50", "Landing speed"),
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
                text = "Parameters",
                style = MaterialTheme.typography.headlineMedium
            )
            
            IconButton(
                onClick = { /* TODO: Refresh parameters */ }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh Parameters")
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
                    text = "Parameter Management",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Connect to vehicle to load parameters\n" +
                            "• Double-tap parameter to edit\n" +
                            "• Changes require confirmation\n" +
                            "• Some parameters require reboot",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(sampleParams) { param ->
                ParameterItem(
                    parameter = param,
                    onClick = { /* TODO: Edit parameter */ }
                )
            }
        }
    }
}

data class Parameter(
    val name: String,
    val value: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParameterItem(
    parameter: Parameter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Build,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = parameter.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = parameter.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = parameter.value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}