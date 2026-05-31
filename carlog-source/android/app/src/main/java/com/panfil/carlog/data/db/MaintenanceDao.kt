package com.panfil.carlog.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.panfil.carlog.domain.Maintenance
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance ORDER BY id ASC")
    fun getAll(): Flow<List<Maintenance>>

    @Query("SELECT * FROM maintenance WHERE id = :id")
    suspend fun getById(id: Long): Maintenance?

    @Query("SELECT MAX(lastMileage) FROM maintenance")
    fun maxMileage(): Flow<Int?>

    @Insert
    suspend fun insert(maintenance: Maintenance): Long

    @Update
    suspend fun update(maintenance: Maintenance)

    @Delete
    suspend fun delete(maintenance: Maintenance)
}
