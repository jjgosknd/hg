package com.panfil.carlog.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.panfil.carlog.domain.CarInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "carlog_prefs")

@Singleton
class PrefsStore @Inject constructor(@ApplicationContext private val context: Context) {

    private object Keys {
        val BRAND = stringPreferencesKey("car_brand")
        val MODEL = stringPreferencesKey("car_model")
        val GENERATION = stringPreferencesKey("car_generation")
        val YEAR_FROM = intPreferencesKey("car_year_from")
        val YEAR_TO = intPreferencesKey("car_year_to")
        val MILEAGE = intPreferencesKey("car_mileage")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val USE_SYSTEM_THEME = booleanPreferencesKey("use_system_theme")
        val DISMISSED_RECOMMENDATIONS = stringSetPreferencesKey("dismissed_recommendations")
    }

    val carInfo: Flow<CarInfo> = context.dataStore.data.map { prefs ->
        CarInfo(
            brand = prefs[Keys.BRAND] ?: "",
            model = prefs[Keys.MODEL] ?: "",
            generation = prefs[Keys.GENERATION] ?: "",
            yearFrom = prefs[Keys.YEAR_FROM] ?: 0,
            yearTo = prefs[Keys.YEAR_TO] ?: 0,
            mileage = prefs[Keys.MILEAGE] ?: 0,
        )
    }

    val darkTheme: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.DARK_THEME] ?: false
    }

    val useSystemTheme: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.USE_SYSTEM_THEME] ?: true
    }

    suspend fun saveCarInfo(info: CarInfo) {
        context.dataStore.edit { prefs ->
            prefs[Keys.BRAND] = info.brand
            prefs[Keys.MODEL] = info.model
            prefs[Keys.GENERATION] = info.generation
            prefs[Keys.YEAR_FROM] = info.yearFrom
            prefs[Keys.YEAR_TO] = info.yearTo
            prefs[Keys.MILEAGE] = info.mileage
        }
    }

    suspend fun setDarkTheme(dark: Boolean) {
        context.dataStore.edit { it[Keys.DARK_THEME] = dark }
    }

    suspend fun setUseSystemTheme(use: Boolean) {
        context.dataStore.edit { it[Keys.USE_SYSTEM_THEME] = use }
    }

    val dismissedRecommendations: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.DISMISSED_RECOMMENDATIONS] ?: emptySet()
    }

    suspend fun dismissRecommendation(title: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.DISMISSED_RECOMMENDATIONS] ?: emptySet()
            prefs[Keys.DISMISSED_RECOMMENDATIONS] = current + title
        }
    }

    suspend fun restoreRecommendation(title: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.DISMISSED_RECOMMENDATIONS] ?: emptySet()
            prefs[Keys.DISMISSED_RECOMMENDATIONS] = current - title
        }
    }

    suspend fun clearDismissedRecommendations() {
        context.dataStore.edit { prefs ->
            prefs[Keys.DISMISSED_RECOMMENDATIONS] = emptySet()
        }
    }
}
