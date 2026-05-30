package com.panfil.carlog.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.panfil.carlog.R
import com.panfil.carlog.ui.screens.analytics.AnalyticsScreen
import com.panfil.carlog.ui.screens.expenses.ExpensesScreen
import com.panfil.carlog.ui.screens.home.HomeScreen
import com.panfil.carlog.ui.screens.maintenance.MaintenanceScreen

object Routes {
    const val HOME = "home"
    const val EXPENSES = "expenses"
    const val ANALYTICS = "analytics"
    const val MAINTENANCE = "maintenance"
}

private data class Tab(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
)

private val Tabs = listOf(
    Tab(Routes.HOME, R.string.tab_home, Icons.Default.Home),
    Tab(Routes.EXPENSES, R.string.tab_expenses, Icons.Default.Receipt),
    Tab(Routes.ANALYTICS, R.string.tab_analytics, Icons.Default.Analytics),
    Tab(Routes.MAINTENANCE, R.string.tab_maintenance, Icons.Default.Build),
)

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = current == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        label = { Text(stringResource(tab.labelRes)) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.EXPENSES) { ExpensesScreen() }
            composable(Routes.ANALYTICS) { AnalyticsScreen() }
            composable(Routes.MAINTENANCE) { MaintenanceScreen() }
        }
    }
}
