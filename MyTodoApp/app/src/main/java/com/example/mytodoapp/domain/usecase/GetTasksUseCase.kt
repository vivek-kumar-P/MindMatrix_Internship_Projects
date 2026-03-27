package com.example.mytodoapp.domain.usecase

import com.example.mytodoapp.domain.model.Task
import com.example.mytodoapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    fun getAllTasks(): Flow<List<Task>> = repository.getAllTasks()
    fun getActiveTasks(): Flow<List<Task>> = repository.getActiveTasks()
    fun getCompletedTasks(): Flow<List<Task>> = repository.getCompletedTasks()
    fun getActiveCount(): Flow<Int> = repository.getActiveTaskCount()
    fun getCompletedCount(): Flow<Int> = repository.getCompletedTaskCount()
}