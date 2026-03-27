package com.example.mytodoapp.domain.repository

import com.example.mytodoapp.domain.model.Task
import com.example.mytodoapp.domain.model.Subtask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getActiveTasks(): Flow<List<Task>>
    fun getCompletedTasks(): Flow<List<Task>>
    fun searchTasks(query: String): Flow<List<Task>>
    fun getTasksByPriority(priority: String): Flow<List<Task>>
    fun getActiveTaskCount(): Flow<Int>
    fun getCompletedTaskCount(): Flow<Int>
    suspend fun getTaskById(id: Long): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(id: Long)
    suspend fun toggleTaskCompletion(id: Long, isCompleted: Boolean)
    suspend fun insertSubtask(subtask: Subtask): Long
    suspend fun updateSubtask(subtask: Subtask)
    suspend fun deleteSubtask(subtask: Subtask)
}