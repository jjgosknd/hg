package com.panfil.carlog.ui.screens.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panfil.carlog.R
import com.panfil.carlog.domain.Maintenance
import com.panfil.carlog.ui.components.AddMaintenanceDialog
import com.panfil.carlog.ui.theme.AppGradients
import com.panfil.carlog.ui.theme.BrandCyan
import com.panfil.carlog.ui.theme.BrandEmerald
import com.panfil.carlog.ui.theme.BrandRose
import java.text.NumberFormat
import java.util.Locale

private val numberFormat = NumberFormat.getNumberInstance(Locale("ru", "RU"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(viewModel: MaintenanceViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.add_short)) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                expanded = true,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            val total = state.items.size
            val overdueCount = state.items.count { item ->
                val due = item.lastMileage + item.mileageInterval
                item.mileageInterval > 0 && state.carInfo.mileage >= due
            }
            val okCount = total - overdueCount

            MaintenanceHeader(
                total = total,
                overdue = overdueCount,
                ok = okCount,
            )

            if (state.items.isEmpty()) {
                EmptyMaintenanceState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp,
                        top = 8.dp, bottom = 96.dp,
                    ),
                ) {
                    items(state.items, key = { it.id }) { item ->
                        MaintenanceCard(
                            item = item,
                            currentMileage = state.carInfo.mileage,
                            onToggleNotify = { viewModel.toggleNotification(item) },
                            onMarkDone = { viewModel.markDone(item, state.carInfo.mileage) },
                            onDelete = { viewModel.deleteMaintenance(item) },
                        )
                    }
                }
            }
        }
    }

    if (state.showAddDialog) {
        AddMaintenanceDialog(
            onDismiss = { viewModel.hideAddDialog() },
            onSave = { viewModel.addMaintenance(it) },
        )
    }
}

@Composable
private fun MaintenanceHeader(total: Int, overdue: Int, ok: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppGradients.Cool),
    ) {
        Column(
            modifier = Modifier.padding(
                start = 20.dp, end = 20.dp,
                top = 24.dp, bottom = 20.dp,
            ),
        ) {
            Text(
                stringResource(R.string.tab_maintenance),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (overdue > 0) "$overdue требует внимания" else "Всё под контролем",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HeaderChip(
                    icon = Icons.Default.Build,
                    label = stringResource(R.string.maint_count_total, total),
                )
                if (overdue > 0) {
                    HeaderChip(
                        icon = Icons.Default.Warning,
                        label = stringResource(R.string.maint_count_overdue, overdue),
                        accent = BrandRose,
                    )
                }
                if (ok > 0) {
                    HeaderChip(
                        icon = Icons.Default.CheckCircle,
                        label = stringResource(R.string.maint_count_ok, ok),
                        accent = BrandEmerald,
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    accent: Color = Color.White,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.18f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun MaintenanceCard(
    item: Maintenance,
    currentMileage: Int,
    onToggleNotify: () -> Unit,
    onMarkDone: () -> Unit,
    onDelete: () -> Unit,
) {
    val dueMileage = item.lastMileage + item.mileageInterval
    val isOverdue = item.mileageInterval > 0 && currentMileage >= dueMileage
    val remaining = dueMileage - currentMileage
    val progress = if (item.mileageInterval > 0) {
        ((currentMileage - item.lastMileage).toFloat() / item.mileageInterval).coerceIn(0f, 1f)
    } else 0f

    val accent = if (isOverdue) BrandRose else BrandCyan

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = accent.copy(alpha = 0.15f),
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (isOverdue) Icons.Default.Warning else Icons.Default.Build,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    val sub = when {
                        item.mileageInterval > 0 && isOverdue ->
                            stringResource(R.string.overdue_at, dueMileage)
                        item.mileageInterval > 0 ->
                            "Осталось ${numberFormat.format(remaining)} км · при ${numberFormat.format(dueMileage)} км"
                        item.monthsInterval > 0 ->
                            stringResource(R.string.every_months, item.monthsInterval)
                        else -> stringResource(R.string.maint_no_interval)
                    }
                    Text(
                        sub,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isOverdue) BrandRose
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Row {
                    IconButton(
                        onClick = onToggleNotify,
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(
                            if (item.notifyEnabled) Icons.Default.Notifications
                            else Icons.Default.NotificationsOff,
                            contentDescription = null,
                            tint = if (item.notifyEnabled) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    IconButton(
                        onClick = onMarkDone,
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.mark_done),
                            tint = BrandEmerald,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            if (item.mileageInterval > 0) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = accent,
                    trackColor = accent.copy(alpha = 0.15f),
                )
            }
        }
    }
}

@Composable
private fun EmptyMaintenanceState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            modifier = Modifier.size(96.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.empty_maintenance_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.empty_maintenance_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
