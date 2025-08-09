package com.pixhawk.gcs.ui.params

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixhawk.gcs.domain.model.Parameter
import com.pixhawk.gcs.ui.theme.PixhawkGCSLiteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParamsScreen(
    viewModel: ParamsViewModel = viewModel()
) {
    val parameters by viewModel.parameters.collectAsState()
    val filteredParameters by viewModel.filteredParameters.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val editingParameter by viewModel.editingParameter.collectAsState()

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
                onClick = { viewModel.refreshParameters() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Refresh")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Search parameters...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (filteredParameters.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (parameters.isEmpty()) "No parameters loaded" else "No parameters match search",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (parameters.isEmpty()) "Click Refresh to load parameters from vehicle" else "Try a different search term",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            // Parameters list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredParameters, key = { it.id }) { param ->
                    ParameterCard(
                        parameter = param,
                        isEditing = editingParameter?.id == param.id,
                        editValue = editingParameter?.value?.toString() ?: "",
                        onEditClick = { viewModel.startEditing(param) },
                        onSaveClick = { viewModel.saveParameter() },
                        onCancelClick = { viewModel.cancelEditing() },
                        onValueChange = { viewModel.updateEditValue(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ParameterCard(
    parameter: Parameter,
    isEditing: Boolean,
    editValue: String,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = parameter.id,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (parameter.description.isNotEmpty()) {
                        Text(
                            text = parameter.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Text(
                        text = "Type: ${getParameterTypeName(parameter.type)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = editValue,
                        onValueChange = onValueChange,
                        label = { Text("Value") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onSaveClick) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                    TextButton(onClick = onCancelClick) {
                        Text("Cancel")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Value: ${parameter.value}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }
    }
}

private fun getParameterTypeName(type: Int): String {
    return when (type) {
        1 -> "UINT8"
        2 -> "INT8"
        3 -> "UINT16"
        4 -> "INT16"
        5 -> "UINT32"
        6 -> "INT32"
        7 -> "UINT64"
        8 -> "INT64"
        9 -> "REAL32"
        10 -> "REAL64"
        else -> "Unknown ($type)"
    }
}

@Preview(showBackground = true)
@Composable
private fun ParamsScreenPreview() {
    PixhawkGCSLiteTheme {
        ParamsScreen()
    }
}