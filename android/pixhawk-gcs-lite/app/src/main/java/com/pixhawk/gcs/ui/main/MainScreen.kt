package com.pixhawk.gcs.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pixhawk.gcs.R
import com.pixhawk.gcs.ui.connect.ConnectScreen
import com.pixhawk.gcs.ui.fly.FlyScreen
import com.pixhawk.gcs.ui.logs.LogsScreen
import com.pixhawk.gcs.ui.missions.MissionsScreen
import com.pixhawk.gcs.ui.params.ParamsScreen
import com.pixhawk.gcs.ui.settings.SettingsScreen

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val titleResId: Int
) {
    object Connect : BottomNavItem("connect", Icons.Default.Link, R.string.nav_connect)
    object Fly : BottomNavItem("fly", Icons.Default.Map, R.string.nav_fly)
    object Missions : BottomNavItem("missions", Icons.Default.FlightTakeoff, R.string.nav_missions)
    object Params : BottomNavItem("params", Icons.Default.List, R.string.nav_params)
    object Logs : BottomNavItem("logs", Icons.Default.TextSnippet, R.string.nav_logs)
    object Settings : BottomNavItem("settings", Icons.Default.Settings, R.string.nav_settings)
}

val bottomNavItems = listOf(
    BottomNavItem.Connect,
    BottomNavItem.Fly,
    BottomNavItem.Missions,
    BottomNavItem.Params,
    BottomNavItem.Logs,
    BottomNavItem.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.titleResId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Connect.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Connect.route) { ConnectScreen() }
            composable(BottomNavItem.Fly.route) { FlyScreen() }
            composable(BottomNavItem.Missions.route) { MissionsScreen() }
            composable(BottomNavItem.Params.route) { ParamsScreen() }
            composable(BottomNavItem.Logs.route) { LogsScreen() }
            composable(BottomNavItem.Settings.route) { SettingsScreen() }
        }
    }
}