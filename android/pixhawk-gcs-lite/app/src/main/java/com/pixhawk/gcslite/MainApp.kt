package com.pixhawk.gcslite

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pixhawk.gcslite.ui.screens.ConnectScreen
import com.pixhawk.gcslite.ui.screens.FlyScreen
import com.pixhawk.gcslite.ui.screens.LogsScreen
import com.pixhawk.gcslite.ui.screens.MissionsScreen
import com.pixhawk.gcslite.ui.screens.ParamsScreen
import com.pixhawk.gcslite.ui.screens.SettingsScreen

sealed class MainDestination(
    val route: String,
    val titleRes: Int,
    val icon: ImageVector
) {
    object Connect : MainDestination("connect", R.string.nav_connect, Icons.Default.Link)
    object Fly : MainDestination("fly", R.string.nav_fly, Icons.Default.Map)
    object Missions : MainDestination("missions", R.string.nav_missions, Icons.Default.FlightTakeoff)
    object Params : MainDestination("params", R.string.nav_params, Icons.Default.Tune)
    object Logs : MainDestination("logs", R.string.nav_logs, Icons.Default.List)
    object Settings : MainDestination("settings", R.string.nav_settings, Icons.Default.Settings)
}

val mainDestinations = listOf(
    MainDestination.Connect,
    MainDestination.Fly,
    MainDestination.Missions,
    MainDestination.Params,
    MainDestination.Logs,
    MainDestination.Settings
)

@Composable
fun MainApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun MainBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    NavigationBar {
        mainDestinations.forEach { destination ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.titleRes)
                    )
                },
                label = { Text(stringResource(destination.titleRes)) },
                selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                onClick = {
                    navController.navigate(destination.route) {
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

@Composable
private fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainDestination.Connect.route,
        modifier = modifier
    ) {
        composable(MainDestination.Connect.route) {
            ConnectScreen()
        }
        composable(MainDestination.Fly.route) {
            FlyScreen()
        }
        composable(MainDestination.Missions.route) {
            MissionsScreen()
        }
        composable(MainDestination.Params.route) {
            ParamsScreen()
        }
        composable(MainDestination.Logs.route) {
            LogsScreen()
        }
        composable(MainDestination.Settings.route) {
            SettingsScreen()
        }
    }
}