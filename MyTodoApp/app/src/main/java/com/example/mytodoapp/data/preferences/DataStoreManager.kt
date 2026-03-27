package com.example.mytodoapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
        val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        val DEFAULT_PRIORITY_KEY = stringPreferencesKey("default_priority")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val SHOW_COMPLETED_KEY = booleanPreferencesKey("show_completed")
        val DAILY_REMINDER_HOUR_KEY = intPreferencesKey("daily_reminder_hour")
        val DAILY_REMINDER_MINUTE_KEY = intPreferencesKey("daily_reminder_minute")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[DARK_THEME_KEY] ?: false }

    val isDynamicColor: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[DYNAMIC_COLOR_KEY] ?: true }

    val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[NOTIFICATIONS_ENABLED_KEY] ?: true }

    val defaultPriority: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[DEFAULT_PRIORITY_KEY] ?: "MEDIUM" }

    val sortOrder: Flow<String> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[SORT_ORDER_KEY] ?: "DATE_CREATED" }

    val showCompleted: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[SHOW_COMPLETED_KEY] ?: true }

    val dailyReminderHour: Flow<Int> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[DAILY_REMINDER_HOUR_KEY] ?: 9 }

    val dailyReminderMinute: Flow<Int> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[DAILY_REMINDER_MINUTE_KEY] ?: 0 }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[DARK_THEME_KEY] = enabled }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { it[DYNAMIC_COLOR_KEY] = enabled }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED_KEY] = enabled }
    }

    suspend fun setDefaultPriority(priority: String) {
        context.dataStore.edit { it[DEFAULT_PRIORITY_KEY] = priority }
    }

    suspend fun setSortOrder(order: String) {
        context.dataStore.edit { it[SORT_ORDER_KEY] = order }
    }

    suspend fun setShowCompleted(show: Boolean) {
        context.dataStore.edit { it[SHOW_COMPLETED_KEY] = show }
    }

    suspend fun setDailyReminder(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[DAILY_REMINDER_HOUR_KEY] = hour
            it[DAILY_REMINDER_MINUTE_KEY] = minute
        }
    }
}