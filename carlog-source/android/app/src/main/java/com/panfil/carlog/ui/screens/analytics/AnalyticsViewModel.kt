package com.panfil.carlog.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panfil.carlog.data.repository.CarLogRepository
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.ExpenseType
import com.panfil.carlog.domain.Maintenance
import com.panfil.carlog.domain.MaintenanceRecommendation
import com.panfil.carlog.domain.RecommendationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AnalyticsUiState(
    val monthlyTotal: Double = 0.0,
    val yearlyTotal: Double = 0.0,
    val byType: Map<ExpenseType, Double> = emptyMap(),
    val monthlyExpenses: List<Expense> = emptyList(),
    val recommendation: String = "",
    val recommendations: List<MaintenanceRecommendation> = emptyList(),
    val hasMileage: Boolean = false,
    val hasDismissed: Boolean = false,
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: CarLogRepository,
) : ViewModel() {

    private val now = LocalDate.now()

    val uiState: StateFlow<AnalyticsUiState> = combine(
        repository.monthlyTotal(now.year, now.monthValue),
        repository.yearlyTotal(now.year),
        repository.expensesForYear(now.year),
        combine(repository.carInfo, repository.allMaintenance, repository.dismissedRecommendations) { a, b, c ->
            Triple(a, b, c)
        },
    ) { monthly, yearly, yearExpenses, (carInfo, maintenanceItems, dismissed) ->
        val m = monthly ?: 0.0
        val y = yearly ?: 0.0

        val byType = yearExpenses
            .groupBy { it.type }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
            .toSortedMap(compareByDescending { byType -> byType.ordinal })

        val recommendation = when {
            y == 0.0 -> "Добавьте расходы для получения рекомендаций"
            m > y / 12 * 1.5 -> "Расходы за месяц выше среднего — проверьте необходимость трат"
            else -> "Все расходы в норме"
        }

        val allRecommendations = RecommendationProvider.generate(
            currentMileage = carInfo.mileage,
            existingMaintenance = maintenanceItems,
        )

        val visibleRecommendations = allRecommendations.filter { it.title !in dismissed }

        AnalyticsUiState(
            monthlyTotal = m,
            yearlyTotal = y,
            byType = byType,
            monthlyExpenses = yearExpenses,
            recommendation = recommendation,
            recommendations = visibleRecommendations,
            hasMileage = carInfo.mileage > 0,
            hasDismissed = dismissed.isNotEmpty(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsUiState())

    fun dismissRecommendation(title: String) {
        viewModelScope.launch { repository.dismissRecommendation(title) }
    }

    /**
     * Отмечает, что замену/обслуживание выполнили прямо сейчас на текущем пробеге.
     * Создаёт (или обновляет) запись ТО, благодаря чему отсчёт до следующей замены
     * сбрасывается и пересчитывается от актуального пробега.
     */
    fun markRecommendationDone(rec: MaintenanceRecommendation) {
        viewModelScope.launch {
            val mileage = repository.carInfo.first().mileage
            val now = System.currentTimeMillis()
            val existing = repository.allMaintenance.first().firstOrNull {
                it.title.equals(rec.title, ignoreCase = true) ||
                    it.title.contains(rec.title, ignoreCase = true) ||
                    rec.title.contains(it.title, ignoreCase = true)
            }
            if (existing != null) {
                repository.updateMaintenance(
                    existing.copy(lastMileage = mileage, lastDate = now),
                )
            } else {
                repository.addMaintenance(
                    Maintenance(
                        title = rec.title,
                        mileageInterval = rec.mileageInterval,
                        monthsInterval = rec.monthsInterval,
                        lastMileage = mileage,
                        lastDate = now,
                    ),
                )
            }
            // Если пункт ранее был скрыт пользователем — возвращаем его,
            // чтобы снова показывать актуальный отсчёт до следующей замены.
            repository.restoreRecommendation(rec.title)
        }
    }

    fun dismissAllRecommendations() {
        val current = uiState.value.recommendations
        viewModelScope.launch {
            current.forEach { repository.dismissRecommendation(it.title) }
        }
    }

    fun restoreRecommendations() {
        viewModelScope.launch { repository.clearDismissedRecommendations() }
    }
}
