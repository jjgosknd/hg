package com.panfil.carlog.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panfil.carlog.R
import com.panfil.carlog.data.CarDatabase
import com.panfil.carlog.ui.components.EditCarDialog
import com.panfil.carlog.ui.navigation.Routes
import com.panfil.carlog.ui.theme.AppGradients
import com.panfil.carlog.ui.theme.BrandAmber
import com.panfil.carlog.ui.theme.BrandCyan
import com.panfil.carlog.ui.theme.BrandEmerald
import com.panfil.carlog.ui.theme.BrandIndigo
import com.panfil.carlog.ui.theme.BrandRose
import com.panfil.carlog.ui.theme.BrandViolet
import java.text.NumberFormat
import java.time.LocalTime
import java.util.Locale

private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
private val numberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // 1) Header (no TopAppBar - cleaner look)
        HomeHeader(
            carName = if (state.carInfo.brand.isNotEmpty()) {
                "${state.carInfo.brand} ${state.carInfo.model}".trim()
            } else null,
            isDark = state.darkTheme && !state.useSystemTheme,
            onToggleTheme = {
                if (state.useSystemTheme) {
                    viewModel.setUseSystemTheme(false)
                    viewModel.setDarkTheme(!state.darkTheme)
                } else {
                    viewModel.setDarkTheme(!state.darkTheme)
                }
            },
        )

        // 2) HERO mileage card or "add car" CTA
        if (state.carInfo.brand.isEmpty()) {
            EmptyCarHero(onAdd = { viewModel.showEditDialog() })
        } else {
            HeroMileageCard(
                brand = state.carInfo.brand,
                model = state.carInfo.model,
                generation = state.carInfo.generation,
                yearFrom = state.carInfo.yearFrom,
                yearTo = state.carInfo.yearTo,
                mileage = state.carInfo.mileage,
                onEdit = { viewModel.showEditDialog() },
            )
        }

        // 3) Quick actions
        QuickActionsRow(
            onExpenses = { onNavigate(Routes.EXPENSES) },
            onMaintenance = { onNavigate(Routes.MAINTENANCE) },
            onAnalytics = { onNavigate(Routes.ANALYTICS) },
        )

        // 4) Stats grid (2x2)
        val overdueCount = state.nextMaintenance.count { it.isOverdue }
        val upcomingCount = state.nextMaintenance.size - overdueCount
        StatsGrid(
            monthlyTotal = state.monthlyTotal,
            mileage = state.carInfo.mileage,
            overdueCount = overdueCount,
            upcomingCount = upcomingCount,
        )

        // 5) Next maintenance section
        NextMaintenanceSection(
            statuses = state.nextMaintenance,
            onSeeAll = { onNavigate(Routes.MAINTENANCE) },
        )

        Spacer(Modifier.height(8.dp))
    }

    if (state.showEditDialog) {
        EditCarDialog(
            initial = state.carInfo,
            onDismiss = { viewModel.hideEditDialog() },
            onSave = { viewModel.saveCarInfo(it) },
        )
    }
}

@Composable
private fun HomeHeader(
    carName: String?,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
) {
    val greeting = remember { greetingForNow() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                greeting,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
            Text(
                carName ?: stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp,
        ) {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = stringResource(R.string.toggle_theme),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun HeroMileageCard(
    brand: String,
    model: String,
    generation: String,
    yearFrom: Int,
    yearTo: Int,
    mileage: Int,
    onEdit: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppGradients.Hero),
        ) {
            // Decorative blob in the corner for depth
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.10f))
                    .padding(0.dp),
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Speed,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.current_mileage_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.weight(1f))
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier.clickable(onClick = onEdit),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_car),
                                tint = Color.White,
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                stringResource(R.string.edit_car),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                // Big mileage number
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        numberFormat.format(mileage),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "км",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                Spacer(Modifier.height(14.dp))
                // Bottom row: car logo + name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Image(
                            painter = painterResource(CarDatabase.logoFor(brand)),
                            contentDescription = brand,
                            modifier = Modifier.padding(6.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        val gen = if (generation.isNotEmpty()) " $generation" else ""
                        Text(
                            "$brand $model$gen",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                        if (yearFrom > 0) {
                            Text(
                                "$yearFrom–$yearTo",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCarHero(onAdd: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAdd),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.background(AppGradients.Hero)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier.size(64.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    stringResource(R.string.no_car_info),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(R.string.add_car_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = Color.White,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        stringResource(R.string.edit_car),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onExpenses: () -> Unit,
    onMaintenance: () -> Unit,
    onAnalytics: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        QuickAction(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Receipt,
            label = stringResource(R.string.qa_expenses),
            tint = BrandAmber,
            onClick = onExpenses,
        )
        QuickAction(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Build,
            label = stringResource(R.string.qa_maintenance),
            tint = BrandCyan,
            onClick = onMaintenance,
        )
        QuickAction(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.QueryStats,
            label = stringResource(R.string.qa_analytics),
            tint = BrandViolet,
            onClick = onAnalytics,
        )
    }
}

@Composable
private fun QuickAction(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = tint.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun StatsGrid(
    monthlyTotal: Double,
    mileage: Int,
    overdueCount: Int,
    upcomingCount: Int,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.stat_monthly),
                value = currencyFormat.format(monthlyTotal),
                accent = BrandAmber,
                subtitle = stringResource(R.string.stat_monthly_sub),
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.stat_mileage),
                value = "${numberFormat.format(mileage)} км",
                accent = BrandIndigo,
                subtitle = stringResource(R.string.stat_mileage_sub),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.stat_overdue),
                value = overdueCount.toString(),
                accent = if (overdueCount > 0) BrandRose else BrandEmerald,
                subtitle = if (overdueCount > 0) stringResource(R.string.stat_overdue_sub_warn)
                else stringResource(R.string.stat_overdue_sub_ok),
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.stat_upcoming),
                value = upcomingCount.toString(),
                accent = BrandCyan,
                subtitle = stringResource(R.string.stat_upcoming_sub),
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    accent: Color,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accent),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NextMaintenanceSection(
    statuses: List<com.panfil.carlog.ui.screens.home.MaintenanceStatus>,
    onSeeAll: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.next_maintenance),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            if (statuses.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                    modifier = Modifier.clickable(onClick = onSeeAll),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            stringResource(R.string.see_all),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        if (statuses.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BrandEmerald.copy(alpha = 0.15f),
                    ) {
                        Box(
                            modifier = Modifier.padding(10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.Speed,
                                contentDescription = null,
                                tint = BrandEmerald,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            stringResource(R.string.no_upcoming_maintenance),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            stringResource(R.string.maintenance_empty_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                statuses.take(3).forEach { status ->
                    NextMaintenanceRow(status = status)
                }
            }
        }
    }
}

@Composable
private fun NextMaintenanceRow(status: com.panfil.carlog.ui.screens.home.MaintenanceStatus) {
    val accent = if (status.isOverdue) BrandRose else BrandCyan
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = accent.copy(alpha = 0.15f),
                modifier = Modifier.size(36.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (status.isOverdue) Icons.Default.Warning else Icons.Default.Build,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    status.maintenance.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    if (status.isOverdue) stringResource(R.string.overdue)
                    else stringResource(R.string.at_mileage, status.dueMileage),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (status.isOverdue) BrandRose
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

private fun greetingForNow(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 5..11 -> "Доброе утро 👋"
        in 12..17 -> "Добрый день 👋"
        in 18..22 -> "Добрый вечер 👋"
        else -> "Доброй ночи 🌙"
    }
}
