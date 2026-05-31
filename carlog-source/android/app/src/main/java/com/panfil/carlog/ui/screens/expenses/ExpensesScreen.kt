package com.panfil.carlog.ui.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panfil.carlog.R
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.ExpenseType
import com.panfil.carlog.domain.FilterPeriod
import com.panfil.carlog.ui.components.AddExpenseDialog
import com.panfil.carlog.ui.theme.AppGradients
import com.panfil.carlog.ui.theme.BrandAmber
import com.panfil.carlog.ui.theme.BrandCyan
import com.panfil.carlog.ui.theme.BrandEmerald
import com.panfil.carlog.ui.theme.BrandIndigo
import com.panfil.carlog.ui.theme.BrandRose
import com.panfil.carlog.ui.theme.BrandViolet
import com.panfil.carlog.util.PdfReportGenerator
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val mileageBumpedTemplate = stringResource(R.string.mileage_bumped_to)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ExpensesEvent.MileageUpdated -> {
                    snackbarHostState.showSnackbar(
                        message = mileageBumpedTemplate.format(event.newMileage),
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            // 1) Hero summary header
            ExpensesHeader(
                total = state.total,
                count = state.expenses.size,
                hasData = state.expenses.isNotEmpty(),
                onExportPdf = {
                    PdfReportGenerator.generateAndShare(
                        context = context,
                        expenses = state.expenses,
                        carInfo = state.carInfo,
                        periodLabel = state.periodLabel,
                    )
                },
            )

            // 2) Period filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = state.filterPeriod == period,
                        onClick = { viewModel.setFilterPeriod(period) },
                        label = {
                            Text(
                                period.label,
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                }
            }

            if (state.filterPeriod == FilterPeriod.CUSTOM) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = { viewModel.showStartPicker() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text(
                            state.customStart?.format(dateFormatter)
                                ?: stringResource(R.string.filter_from),
                        )
                    }
                    Text("—")
                    OutlinedButton(
                        onClick = { viewModel.showEndPicker() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text(
                            state.customEnd?.format(dateFormatter)
                                ?: stringResource(R.string.filter_to),
                        )
                    }
                }
            }

            // 3) List
            if (state.expenses.isEmpty()) {
                EmptyExpensesState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp,
                        top = 4.dp, bottom = 96.dp,
                    ),
                ) {
                    items(state.expenses, key = { it.id }) { expense ->
                        ExpenseRow(expense = expense, onDelete = { viewModel.deleteExpense(expense) })
                    }
                }
            }
        }
    }

    if (state.showAddDialog) {
        AddExpenseDialog(
            onDismiss = { viewModel.hideAddDialog() },
            onSave = { viewModel.addExpense(it) },
        )
    }

    if (state.showStartPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.customStart
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { viewModel.hideStartPicker() },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { ms ->
                        val date = Instant.ofEpochMilli(ms)
                            .atZone(ZoneOffset.UTC).toLocalDate()
                        viewModel.setCustomStart(date)
                    }
                    viewModel.hideStartPicker()
                }) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideStartPicker() }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        ) { DatePicker(state = pickerState) }
    }

    if (state.showEndPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.customEnd
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { viewModel.hideEndPicker() },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { ms ->
                        val date = Instant.ofEpochMilli(ms)
                            .atZone(ZoneOffset.UTC).toLocalDate()
                        viewModel.setCustomEnd(date)
                    }
                    viewModel.hideEndPicker()
                }) { Text(stringResource(R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideEndPicker() }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        ) { DatePicker(state = pickerState) }
    }
}

@Composable
private fun ExpensesHeader(
    total: Double,
    count: Int,
    hasData: Boolean,
    onExportPdf: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppGradients.Warm),
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 20.dp, end = 20.dp,
                    top = 24.dp, bottom = 24.dp,
                ),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.tab_expenses),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                    if (hasData) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.clickable(onClick = onExportPdf),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Default.PictureAsPdf,
                                    contentDescription = stringResource(R.string.export_pdf),
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "PDF",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.period_total),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    currencyFormat.format(total),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(R.string.period_count, count),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }
        }
    }
}

@Composable
private fun ExpenseRow(expense: Expense, onDelete: () -> Unit) {
    val (icon, color) = iconAndColor(expense.type)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.15f),
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    expense.type.label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                val dateStr = Instant.ofEpochMilli(expense.date)
                    .atZone(ZoneId.systemDefault())
                    .format(dateFormatter)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        dateStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (expense.mileage > 0) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "${expense.mileage} км",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                if (expense.description.isNotEmpty()) {
                    Text(
                        expense.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    currencyFormat.format(expense.amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = color,
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp),
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
    }
}

@Composable
private fun EmptyExpensesState() {
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
                    Icons.Default.Receipt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.empty_expenses_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.empty_expenses_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun iconAndColor(type: ExpenseType): Pair<ImageVector, Color> = when (type) {
    ExpenseType.FUEL -> Icons.Default.LocalGasStation to BrandAmber
    ExpenseType.PARTS -> Icons.Default.Settings to BrandIndigo
    ExpenseType.TIRES -> Icons.Default.LocalShipping to BrandViolet
    ExpenseType.WASH -> Icons.Default.CleaningServices to BrandCyan
    ExpenseType.OIL_CHANGE -> Icons.Default.OilBarrel to BrandRose
    ExpenseType.SERVICE -> Icons.Default.Handyman to BrandEmerald
    ExpenseType.INSURANCE -> Icons.Default.HealthAndSafety to BrandIndigo
    ExpenseType.OTHER -> Icons.Default.Category to BrandIndigo
}
