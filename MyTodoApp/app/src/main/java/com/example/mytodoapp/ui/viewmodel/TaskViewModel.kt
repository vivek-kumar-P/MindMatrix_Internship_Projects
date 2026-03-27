package com.example.mytodoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.notification.ReminderScheduler
import com.example.mytodoapp.domain.model.Priority
import com.example.mytodoapp.domain.model.Task
import com.example.mytodoapp.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUiState(
    val allTasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val selectedPriority: Priority? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentlyDeletedTask: Task? = null,
    val activeCount: Int = 0,
    val completedCount: Int = 0,
    val showCompleted: Boolean = true
)

enum class TaskFilter { ALL, TODAY, UPCOMING, COMPLETED }

enum class SortOrder { DATE_CREATED, DUE_DATE, PRIORITY, TITLE }

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleCompleteUseCase: ToggleCompleteUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DATE_CREATED)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    init {
        loadTasks()
        loadCounts()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTasksUseCase.getAllTasks()
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { tasks ->
                    _uiState.update { state ->
                        state.copy(
                            allTasks = tasks,
                            filteredTasks = applyFilters(tasks, state),
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun loadCounts() {
        viewModelScope.launch {
            getTasksUseCase.getActiveCount().collect { count ->
                _uiState.update { it.copy(activeCount = count) }
            }
        }
        viewModelScope.launch {
            getTasksUseCase.getCompletedCount().collect { count ->
                _uiState.update { it.copy(completedCount = count) }
            }
        }
    }

    private fun applyFilters(tasks: List<Task>, state: TaskUiState): List<Task> {
        val now = System.currentTimeMillis()
        val endOfDay = now + (24 * 60 * 60 * 1000)
        val endOfWeek = now + (7 * 24 * 60 * 60 * 1000)

        var result = tasks

        // Apply tab filter
        result = when (state.selectedFilter) {
            TaskFilter.ALL -> if (state.showCompleted) result
            else result.filter { !it.isCompleted }
            TaskFilter.TODAY -> result.filter { task ->
                !task.isCompleted && task.dueDate != null &&
                        task.dueDate in now..endOfDay
            }
            TaskFilter.UPCOMING -> result.filter { task ->
                !task.isCompleted && task.dueDate != null &&
                        task.dueDate in endOfDay..endOfWeek
            }
            TaskFilter.COMPLETED -> result.filter { it.isCompleted }
        }

        // Apply priority filter
        if (state.selectedPriority != null) {
            result = result.filter { it.priority == state.selectedPriority }
        }

        // Apply search
        if (state.searchQuery.isNotBlank()) {
            result = result.filter {
                it.title.contains(state.searchQuery, ignoreCase = true) ||
                        it.description.contains(state.searchQuery, ignoreCase = true) ||
                        it.tags.any { tag -> tag.name.contains(state.searchQuery, ignoreCase = true) }
            }
        }

        // Apply sort
        result = when (_sortOrder.value) {
            SortOrder.DATE_CREATED -> result.sortedByDescending { it.createdAt }
            SortOrder.DUE_DATE -> result.sortedWith(
                compareBy(nullsLast()) { it.dueDate }
            )
            SortOrder.PRIORITY -> result.sortedByDescending { it.priority.level }
            SortOrder.TITLE -> result.sortedBy { it.title.lowercase() }
        }

        return result
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                val id = addTaskUseCase(task)
                if (task.reminderTime != null) {
                    reminderScheduler.scheduleReminder(
                        taskId = id,
                        taskTitle = task.title,
                        taskDescription = task.description,
                        timeMillis = task.reminderTime
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                updateTaskUseCase(task)
                if (task.reminderTime != null) {
                    reminderScheduler.scheduleReminder(
                        taskId = task.id,
                        taskTitle = task.title,
                        taskDescription = task.description,
                        timeMillis = task.reminderTime
                    )
                } else {
                    reminderScheduler.cancelReminder(task.id)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(task)
                reminderScheduler.cancelReminder(task.id)
                _uiState.update { it.copy(recentlyDeletedTask = task) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            _uiState.value.recentlyDeletedTask?.let { task ->
                addTaskUseCase(task)
                _uiState.update { it.copy(recentlyDeletedTask = null) }
            }
        }
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            try {
                toggleCompleteUseCase(task.id, !task.isCompleted)
                if (!task.isCompleted) {
                    reminderScheduler.cancelReminder(task.id)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredTasks = applyFilters(state.allTasks, state.copy(searchQuery = query))
            )
        }
    }

    fun setFilter(filter: TaskFilter) {
        _uiState.update { state ->
            state.copy(
                selectedFilter = filter,
                filteredTasks = applyFilters(state.allTasks, state.copy(selectedFilter = filter))
            )
        }
    }

    fun setPriorityFilter(priority: Priority?) {
        _uiState.update { state ->
            state.copy(
                selectedPriority = priority,
                filteredTasks = applyFilters(state.allTasks, state.copy(selectedPriority = priority))
            )
        }
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.update { order }
        _uiState.update { state ->
            state.copy(filteredTasks = applyFilters(state.allTasks, state))
        }
    }

    fun toggleShowCompleted() {
        _uiState.update { state ->
            state.copy(
                showCompleted = !state.showCompleted,
                filteredTasks = applyFilters(
                    state.allTasks,
                    state.copy(showCompleted = !state.showCompleted)
                )
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}