package com.panfil.carlog.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CarLogDatabase =
        Room.databaseBuilder(context, CarLogDatabase::class.java, "carlog.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideExpenseDao(db: CarLogDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideMaintenanceDao(db: CarLogDatabase): MaintenanceDao = db.maintenanceDao()
}
