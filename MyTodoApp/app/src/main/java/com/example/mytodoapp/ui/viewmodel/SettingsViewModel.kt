package com.example.mytodoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.preferences.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val isDynamicColor: Boolean = true,
    val isNotificationsEnabled: Boolean = true,
    val defaultPriority: String = "MEDIUM",
    val sortOrder: String = "DATE_CREATED",
    val showCompleted: Boolean = true,
    val dailyReminderHour: Int = 9,
    val dailyReminderMinute: Int = 0
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        dataStoreManager.isDarkTheme,
        dataStoreManager.isDynamicColor,
        dataStoreManager.isNotificationsEnabled,
        dataStoreManager.defaultPriority,
        dataStoreManager.sortOrder
    ) { darkTheme, dynamicColor, notifications, priority, sort ->
        SettingsUiState(
            isDarkTheme = darkTheme,
            isDynamicColor = dynamicColor,
            isNotificationsEnabled = notifications,
            defaultPriority = priority,
            sortOrder = sort
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { dataStoreManager.setDarkTheme(enabled) }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch { dataStoreManager.setDynamicColor(enabled) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreManager.setNotificationsEnabled(enabled) }
    }

    fun setDefaultPriority(priority: String) {
        viewModelScope.launch { dataStoreManager.setDefaultPriority(priority) }
    }

    fun setSortOrder(order: String) {
        viewModelScope.launch { dataStoreManager.setSortOrder(order) }
    }

    fun setShowCompleted(show: Boolean) {
        viewModelScope.launch { dataStoreManager.setShowCompleted(show) }
    }

    fun setDailyReminder(hour: Int, minute: Int) {
        viewModelScope.launch { dataStoreManager.setDailyReminder(hour, minute) }
    }
}