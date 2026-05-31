package com.panfil.carlog.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.panfil.carlog.domain.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startMs AND date < :endMs ORDER BY date DESC")
    fun getByDateRange(startMs: Long, endMs: Long): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startMs AND date < :endMs")
    fun totalInRange(startMs: Long, endMs: Long): Flow<Double?>

    @Query("SELECT MAX(mileage) FROM expenses")
    fun maxMileage(): Flow<Int?>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}
