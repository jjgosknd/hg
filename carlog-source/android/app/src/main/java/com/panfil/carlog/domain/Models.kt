package com.panfil.carlog.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: ExpenseType = ExpenseType.FUEL,
    val amount: Double = 0.0,
    val mileage: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val description: String = "",
)

@Entity(tableName = "maintenance")
data class Maintenance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val mileageInterval: Int = 0,
    val monthsInterval: Int = 0,
    val lastMileage: Int = 0,
    val lastDate: Long = System.currentTimeMillis(),
    val notifyEnabled: Boolean = true,
)

@Serializable
data class CarInfo(
    val brand: String = "",
    val model: String = "",
    val generation: String = "",
    val yearFrom: Int = 0,
    val yearTo: Int = 0,
    val mileage: Int = 0,
)

enum class ExpenseType(val label: String) {
    FUEL("Топливо"),
    PARTS("Запчасти"),
    TIRES("Шины"),
    WASH("Мойка"),
    OIL_CHANGE("Замена масла"),
    SERVICE("Сервис"),
    INSURANCE("Страховка"),
    OTHER("Другое"),
}

data class MaintenanceRecommendation(
    val title: String,
    val description: String,
    val mileageInterval: Int,
    val monthsInterval: Int,
    val status: RecommendationStatus,
    val nextDueMileage: Int,
    val overdueByKm: Int = 0,
    /** Километров осталось до следующей замены (может быть < 0, если просрочено). */
    val remainingKm: Int = 0,
    /** Прогресс текущего интервала: 0f — только что заменили, 1f — пора менять. */
    val progress: Float = 0f,
    /** true, если по этому пункту уже есть запись о выполненном ТО. */
    val hasRecord: Boolean = false,
)

enum class RecommendationStatus { OVERDUE, UPCOMING, OK }

enum class FilterPeriod(val label: String) {
    ALL("Все"),
    WEEK("Неделя"),
    MONTH("Месяц"),
    QUARTER("Квартал"),
    YEAR("Год"),
    CUSTOM("Период"),
}
