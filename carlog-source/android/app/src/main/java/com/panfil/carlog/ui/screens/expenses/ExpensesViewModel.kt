package com.panfil.carlog.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panfil.carlog.data.repository.CarLogRepository
import com.panfil.carlog.domain.CarInfo
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.FilterPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val showAddDialog: Boolean = false,
    val filterPeriod: FilterPeriod = FilterPeriod.ALL,
    val customStart: LocalDate? = null,
    val customEnd: LocalDate? = null,
    val total: Double = 0.0,
    val periodLabel: String = "Все",
    val carInfo: CarInfo = CarInfo(),
    val showStartPicker: Boolean = false,
    val showEndPicker: Boolean = false,
)

sealed interface ExpensesEvent {
    /** Пробег машины был обновлён до указанного значения автоматически. */
    data class MileageUpdated(val newMileage: Int) : ExpensesEvent

    /** Пробег пересчитан после удаления записи (стал меньше). */
    data class MileageRecalculated(val newMileage: Int) : ExpensesEvent
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val repository: CarLogRepository,
) : ViewModel() {

    private val _showAdd = MutableStateFlow(false)
    private val _filterPeriod = MutableStateFlow(FilterPeriod.ALL)
    private val _customStart = MutableStateFlow<LocalDate?>(null)
    private val _customEnd = MutableStateFlow<LocalDate?>(null)
    private val _showStartPicker = MutableStateFlow(false)
    private val _showEndPicker = MutableStateFlow(false)

    /** События для UI: например, "пробег машины обновлён до X км". */
    private val _events = Channel<ExpensesEvent>(Channel.BUFFERED)
    val events: Flow<ExpensesEvent> = _events.receiveAsFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val zone = ZoneId.systemDefault()

    private val filteredExpenses = combine(
        _filterPeriod, _customStart, _customEnd,
    ) { period, cStart, cEnd ->
        val now = LocalDate.now()
        when (period) {
            FilterPeriod.ALL -> null to null
            FilterPeriod.WEEK -> (now.minusWeeks(1)) to now.plusDays(1)
            FilterPeriod.MONTH -> (now.minusMonths(1)) to now.plusDays(1)
            FilterPeriod.QUARTER -> (now.minusMonths(3)) to now.plusDays(1)
            FilterPeriod.YEAR -> (now.minusYears(1)) to now.plusDays(1)
            FilterPeriod.CUSTOM -> (cStart ?: now.minusMonths(1)) to (cEnd?.plusDays(1) ?: now.plusDays(1))
        }
    }.flatMapLatest { (start, end) ->
        if (start == null || end == null) {
            repository.allExpenses
        } else {
            val startMs = start.atStartOfDay(zone).toInstant().toEpochMilli()
            val endMs = end.atStartOfDay(zone).toInstant().toEpochMilli()
            repository.expensesByDateRange(startMs, endMs)
        }
    }

    val uiState: StateFlow<ExpensesUiState> = combine(
        filteredExpenses,
        _showAdd,
        _filterPeriod,
        _customStart,
        _customEnd,
    ) { expenses, showAdd, period, cStart, cEnd ->
        val total = expenses.sumOf { it.amount }
        val label = buildPeriodLabel(period, cStart, cEnd)
        ExpensesUiState(
            expenses = expenses,
            showAddDialog = showAdd,
            filterPeriod = period,
            customStart = cStart,
            customEnd = cEnd,
            total = total,
            periodLabel = label,
        )
    }.combine(combine(_showStartPicker, _showEndPicker, repository.carInfo) { sp, ep, car ->
        Triple(sp, ep, car)
    }) { state, (sp, ep, car) ->
        state.copy(showStartPicker = sp, showEndPicker = ep, carInfo = car)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpensesUiState())

    fun showAddDialog() { _showAdd.value = true }
    fun hideAddDialog() { _showAdd.value = false }

    fun setFilterPeriod(period: FilterPeriod) {
        _filterPeriod.value = period
    }

    fun setCustomStart(date: LocalDate) { _customStart.value = date }
    fun setCustomEnd(date: LocalDate) { _customEnd.value = date }
    fun showStartPicker() { _showStartPicker.value = true }
    fun hideStartPicker() { _showStartPicker.value = false }
    fun showEndPicker() { _showEndPicker.value = true }
    fun hideEndPicker() { _showEndPicker.value = false }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            // Запоминаем "до" — чтобы понять, поднял ли расход пробег.
            val prevEffective = repository.currentEffectiveMileage()
            repository.addExpense(expense)
            // Эффективный пробег пересчитывается автоматически через Flow,
            // поэтому здесь нам нужно лишь решить — показывать снэк-бар или нет.
            if (expense.mileage > 0 && expense.mileage > prevEffective) {
                _events.trySend(ExpensesEvent.MileageUpdated(expense.mileage))
            }
            _showAdd.value = false
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            val prevEffective = repository.currentEffectiveMileage()
            repository.deleteExpense(expense)
            // Если удалённая запись была "тем самым максимумом" — пробег сам
            // откатится через Flow. Сообщим пользователю об этом, чтобы он
            // не подумал, что что-то сломалось.
            val newEffective = repository.currentEffectiveMileage()
            if (newEffective < prevEffective) {
                _events.trySend(ExpensesEvent.MileageRecalculated(newEffective))
            }
        }
    }

    private fun buildPeriodLabel(period: FilterPeriod, start: LocalDate?, end: LocalDate?): String {
        val now = LocalDate.now()
        return when (period) {
            FilterPeriod.ALL -> "Все время"
            FilterPeriod.WEEK -> "${now.minusWeeks(1).format(dateFormatter)} — ${now.format(dateFormatter)}"
            FilterPeriod.MONTH -> "${now.minusMonths(1).format(dateFormatter)} — ${now.format(dateFormatter)}"
            FilterPeriod.QUARTER -> "${now.minusMonths(3).format(dateFormatter)} — ${now.format(dateFormatter)}"
            FilterPeriod.YEAR -> "${now.minusYears(1).format(dateFormatter)} — ${now.format(dateFormatter)}"
            FilterPeriod.CUSTOM -> {
                val s = start?.format(dateFormatter) ?: "..."
                val e = end?.format(dateFormatter) ?: "..."
                "$s — $e"
            }
        }
    }
}
