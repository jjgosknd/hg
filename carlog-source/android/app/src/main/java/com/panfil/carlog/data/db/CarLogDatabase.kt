package com.panfil.carlog.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.panfil.carlog.domain.Expense
import com.panfil.carlog.domain.Maintenance

@Database(entities = [Expense::class, Maintenance::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CarLogDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun maintenanceDao(): MaintenanceDao
}
