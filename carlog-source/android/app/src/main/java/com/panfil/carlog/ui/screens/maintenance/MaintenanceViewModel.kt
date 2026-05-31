package com.panfil.carlog.ui.screens.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panfil.carlog.data.repository.CarLogRepository
import com.panfil.carlog.domain.CarInfo
import com.panfil.carlog.domain.Maintenance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MaintenanceUiState(
    val items: List<Maintenance> = emptyList(),
    val carInfo: CarInfo = CarInfo(),
    val showAddDialog: Boolean = false,
)

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val repository: CarLogRepository,
) : ViewModel() {

    private val _showAdd = MutableStateFlow(false)

    val uiState: StateFlow<MaintenanceUiState> = combine(
        repository.allMaintenance,
        repository.carInfo,
        _showAdd,
    ) { items, carInfo, showAdd ->
        MaintenanceUiState(items = items, carInfo = carInfo, showAddDialog = showAdd)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaintenanceUiState())

    fun showAddDialog() { _showAdd.value = true }
    fun hideAddDialog() { _showAdd.value = false }

    fun addMaintenance(m: Maintenance) {
        viewModelScope.launch {
            repository.addMaintenance(m)
            // Эффективный пробег машины автоматически подтянется до lastMileage,
            // если оно больше текущего (это считает репозиторий).
            _showAdd.value = false
        }
    }

    fun deleteMaintenance(m: Maintenance) {
        viewModelScope.launch { repository.deleteMaintenance(m) }
    }

    fun toggleNotification(m: Maintenance) {
        viewModelScope.launch {
            repository.updateMaintenance(m.copy(notifyEnabled = !m.notifyEnabled))
        }
    }

    fun markDone(m: Maintenance, currentMileage: Int) {
        viewModelScope.launch {
            repository.updateMaintenance(
                m.copy(
                    lastMileage = currentMileage,
                    lastDate = System.currentTimeMillis(),
                ),
            )
            // Эффективный пробег уже учитывает lastMileage, отдельно бампать
            // ничего не нужно.
        }
    }
}
