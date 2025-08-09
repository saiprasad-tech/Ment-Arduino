package com.pixhawk.gcslite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Parameter(
    val name: String,
    val value: String,
    val type: String,
    val description: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamsScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var parameters by remember {
        mutableStateOf(
            listOf(
                Parameter("ARMING_CHECK", "1", "Int32", "Arming check enable"),
                Parameter("BATT_CAPACITY", "5000", "Float", "Battery capacity in mAh"),
                Parameter("COMPASS_AUTODEC", "1", "Int32", "Auto compass declination"),
                Parameter("GPS_TYPE", "1", "Int32", "1st GPS type"),
                Parameter("RC1_MAX", "2000", "Int32", "RC channel 1 maximum"),
                Parameter("WPNAV_SPEED", "500", "Float", "Waypoint navigation speed")
            )
        )
    }
    
    val filteredParameters = parameters.filter { 
        it.name.contains(searchQuery, ignoreCase = true) 
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search parameters...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Parameters (${filteredParameters.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Parameters list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredParameters) { parameter ->
                ParameterItem(
                    parameter = parameter,
                    onEditClick = { /* TODO: Handle parameter edit */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParameterItem(
    parameter: Parameter,
    onEditClick: (Parameter) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = parameter.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Value: ${parameter.value} (${parameter.type})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (parameter.description.isNotEmpty()) {
                    Text(
                        text = parameter.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = { onEditClick(parameter) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit parameter")
            }
        }
    }
}