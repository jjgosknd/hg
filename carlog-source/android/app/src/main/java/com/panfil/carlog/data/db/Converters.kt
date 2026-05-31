package com.panfil.carlog.data.db

import androidx.room.TypeConverter
import com.panfil.carlog.domain.ExpenseType

class Converters {
    @TypeConverter
    fun fromExpenseType(value: ExpenseType): String = value.name

    @TypeConverter
    fun toExpenseType(value: String): ExpenseType = ExpenseType.valueOf(value)
}
