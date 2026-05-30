package com.panfil.carlog.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
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
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            FloatingPillNavBar(
                currentRoute = current,
                onTabSelected = { route -> navigateToTab(navController, route) },
            )
        },
    ) { padding ->
        // Учитываем только bottom-инсет (где навбар), верх отдаём экранам сами,
        // чтобы они могли рисовать edge-to-edge с собственными заголовками.
        val ld = LocalLayoutDirection.current
        val contentPadding = PaddingValues(
            start = padding.calculateStartPadding(ld),
            end = padding.calculateEndPadding(ld),
            top = 0.dp,
            bottom = padding.calculateBottomPadding(),
        )
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(contentPadding),
        ) {
            composable(Routes.HOME) {
                HomeScreen(onNavigate = { route -> navigateToTab(navController, route) })
            }
            composable(Routes.EXPENSES) { ExpensesScreen() }
            composable(Routes.ANALYTICS) { AnalyticsScreen() }
            composable(Routes.MAINTENANCE) { MaintenanceScreen() }
        }
    }
}

private fun navigateToTab(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun FloatingPillNavBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Tabs.forEach { tab ->
                NavPill(
                    tab = tab,
                    selected = currentRoute == tab.route,
                    onClick = { onTabSelected(tab.route) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.NavPill(tab: Tab, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surface,
        animationSpec = tween(220),
        label = "navBg",
    )
    val tint by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(220),
        label = "navTint",
    )

    val interaction = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .weight(if (selected) 1.6f else 1f)
            .clip(RoundedCornerShape(22.dp))
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            tab.icon,
            contentDescription = stringResource(tab.labelRes),
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
        if (selected) {
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(tab.labelRes),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = tint,
            )
        }
    }
}
