package com.panfil.carlog.data.repository

import com.panfil.carlog.data.db.ExpenseDao
import com.panfil.carlog.data.db.MaintenanceDao
import com.panfil.carlog.data.prefs.PrefsStore
import com.panfil.carlog.domain.CarInfo
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.Maintenance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarLogRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val maintenanceDao: MaintenanceDao,
    private val prefsStore: PrefsStore,
) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAll()
    val allMaintenance: Flow<List<Maintenance>> = maintenanceDao.getAll()

    /**
     * "Эффективный" пробег машины = максимум из:
     *  - вручную заданного значения (EditCarDialog),
     *  - самого большого пробега в записях о расходах,
     *  - самого большого пробега из записей о выполненных ТО.
     *
     * Благодаря этому:
     *  - при добавлении расхода/ТО с большим пробегом главная сразу
     *    подтягивается;
     *  - при удалении записей пробег пересчитывается автоматически
     *    (берётся следующий по величине из оставшихся).
     */
    val carInfo: Flow<CarInfo> = combine(
        prefsStore.carInfo,
        expenseDao.maxMileage(),
        maintenanceDao.maxMileage(),
    ) { manual, maxExp, maxMaint ->
        val effective = maxOf(manual.mileage, maxExp ?: 0, maxMaint ?: 0)
        manual.copy(mileage = effective)
    }

    /** Чистое значение, которое реально лежит в Prefs. Нужно для EditCarDialog. */
    val carInfoManual: Flow<CarInfo> = prefsStore.carInfo

    val darkTheme: Flow<Boolean> = prefsStore.darkTheme
    val useSystemTheme: Flow<Boolean> = prefsStore.useSystemTheme
    val dismissedRecommendations: Flow<Set<String>> = prefsStore.dismissedRecommendations

    fun expensesForMonth(year: Int, month: Int): Flow<List<Expense>> {
        val start = LocalDate.of(year, month, 1)
        val end = start.plusMonths(1)
        val zone = ZoneId.systemDefault()
        return expenseDao.getByDateRange(
            start.atStartOfDay(zone).toInstant().toEpochMilli(),
            end.atStartOfDay(zone).toInstant().toEpochMilli(),
        )
    }

    fun expensesForYear(year: Int): Flow<List<Expense>> {
        val start = LocalDate.of(year, 1, 1)
        val end = start.plusYears(1)
        val zone = ZoneId.systemDefault()
        return expenseDao.getByDateRange(
            start.atStartOfDay(zone).toInstant().toEpochMilli(),
            end.atStartOfDay(zone).toInstant().toEpochMilli(),
        )
    }

    fun monthlyTotal(year: Int, month: Int): Flow<Double?> {
        val start = LocalDate.of(year, month, 1)
        val end = start.plusMonths(1)
        val zone = ZoneId.systemDefault()
        return expenseDao.totalInRange(
            start.atStartOfDay(zone).toInstant().toEpochMilli(),
            end.atStartOfDay(zone).toInstant().toEpochMilli(),
        )
    }

    fun yearlyTotal(year: Int): Flow<Double?> {
        val start = LocalDate.of(year, 1, 1)
        val end = start.plusYears(1)
        val zone = ZoneId.systemDefault()
        return expenseDao.totalInRange(
            start.atStartOfDay(zone).toInstant().toEpochMilli(),
            end.atStartOfDay(zone).toInstant().toEpochMilli(),
        )
    }

    fun expensesByDateRange(startMs: Long, endMs: Long): Flow<List<Expense>> =
        expenseDao.getByDateRange(startMs, endMs)

    suspend fun addExpense(expense: Expense) = expenseDao.insert(expense)
    suspend fun updateExpense(expense: Expense) = expenseDao.update(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.delete(expense)

    suspend fun addMaintenance(m: Maintenance) = maintenanceDao.insert(m)
    suspend fun updateMaintenance(m: Maintenance) = maintenanceDao.update(m)
    suspend fun deleteMaintenance(m: Maintenance) = maintenanceDao.delete(m)
    suspend fun getMaintenanceById(id: Long) = maintenanceDao.getById(id)

    suspend fun saveCarInfo(info: CarInfo) = prefsStore.saveCarInfo(info)

    /**
     * Возвращает текущее эффективное значение пробега. Полезно, когда
     * нужно сравнить ввод пользователя с тем, что было до операции.
     */
    suspend fun currentEffectiveMileage(): Int = carInfo.first().mileage

    suspend fun setDarkTheme(dark: Boolean) = prefsStore.setDarkTheme(dark)
    suspend fun setUseSystemTheme(use: Boolean) = prefsStore.setUseSystemTheme(use)

    suspend fun dismissRecommendation(title: String) = prefsStore.dismissRecommendation(title)
    suspend fun restoreRecommendation(title: String) = prefsStore.restoreRecommendation(title)
    suspend fun clearDismissedRecommendations() = prefsStore.clearDismissedRecommendations()
}
