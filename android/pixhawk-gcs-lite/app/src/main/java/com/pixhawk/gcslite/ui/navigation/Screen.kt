package com.pixhawk.gcslite.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Connect : Screen("connect", "Connect", Icons.Default.Wifi)
    object Fly : Screen("fly", "Fly", Icons.Default.FlightTakeoff)
    object Missions : Screen("missions", "Missions", Icons.Default.LocationOn)
    object Params : Screen("params", "Params", Icons.Default.Build)
    object Logs : Screen("logs", "Logs", Icons.Default.List)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Connect,
    Screen.Fly,
    Screen.Missions,
    Screen.Params,
    Screen.Logs,
    Screen.Settings
)