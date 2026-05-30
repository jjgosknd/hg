package com.panfil.carlog.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panfil.carlog.data.repository.CarLogRepository
import com.panfil.carlog.domain.CarInfo
import com.panfil.carlog.domain.Maintenance
import com.panfil.carlog.notifications.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val carInfo: CarInfo = CarInfo(),
    val monthlyTotal: Double = 0.0,
    val nextMaintenance: List<MaintenanceStatus> = emptyList(),
    val showEditDialog: Boolean = false,
    val showAddExpenseDialog: Boolean = false,
    val useSystemTheme: Boolean = true,
    val darkTheme: Boolean = false,
)

data class MaintenanceStatus(
    val maintenance: Maintenance,
    val dueMileage: Int,
    val dueDate: Long,
    val isOverdue: Boolean,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CarLogRepository,
    private val notificationScheduler: NotificationScheduler,
) : ViewModel() {

    private val _showEdit = MutableStateFlow(false)
    private val _showAddExpense = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.carInfo,
        repository.monthlyTotal(LocalDate.now().year, LocalDate.now().monthValue),
        repository.allMaintenance,
        _showEdit,
        _showAddExpense,
        repository.useSystemTheme,
        repository.darkTheme,
    ) { values ->
        val carInfo = values[0] as CarInfo
        val monthly = (values[1] as? Double) ?: 0.0
        @Suppress("UNCHECKED_CAST")
        val maintenanceList = values[2] as List<Maintenance>
        val showEdit = values[3] as Boolean
        val showAddExp = values[4] as Boolean
        val useSys = values[5] as Boolean
        val dark = values[6] as Boolean

        val now = System.currentTimeMillis()
        val statuses = maintenanceList.map { m ->
            val dueMileage = m.lastMileage + m.mileageInterval
            val dueDate = m.lastDate + m.monthsInterval.toLong() * 30 * 24 * 60 * 60 * 1000
            val overdueMileage = m.mileageInterval > 0 && carInfo.mileage >= dueMileage
            val overdueDate = m.monthsInterval > 0 && now >= dueDate
            MaintenanceStatus(m, dueMileage, dueDate, overdueMileage || overdueDate)
        }

        HomeUiState(
            carInfo = carInfo,
            monthlyTotal = monthly,
            nextMaintenance = statuses,
            showEditDialog = showEdit,
            showAddExpenseDialog = showAddExp,
            useSystemTheme = useSys,
            darkTheme = dark,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        notificationScheduler.scheduleMaintenanceCheck()
    }

    fun showEditDialog() { _showEdit.value = true }
    fun hideEditDialog() { _showEdit.value = false }
    fun showAddExpenseDialog() { _showAddExpense.value = true }
    fun hideAddExpenseDialog() { _showAddExpense.value = false }

    fun saveCarInfo(info: CarInfo) {
        viewModelScope.launch {
            repository.saveCarInfo(info)
            _showEdit.value = false
        }
    }

    fun setDarkTheme(dark: Boolean) {
        viewModelScope.launch { repository.setDarkTheme(dark) }
    }

    fun setUseSystemTheme(use: Boolean) {
        viewModelScope.launch { repository.setUseSystemTheme(use) }
    }
}
