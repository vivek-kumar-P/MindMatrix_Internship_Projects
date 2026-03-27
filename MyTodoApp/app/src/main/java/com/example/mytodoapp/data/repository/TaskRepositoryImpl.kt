package com.example.mytodoapp.data.repository

import com.example.mytodoapp.data.local.TaskDao
import com.example.mytodoapp.data.mapper.toDomain
import com.example.mytodoapp.data.mapper.toEntity
import com.example.mytodoapp.domain.model.Subtask
import com.example.mytodoapp.domain.model.Task
import com.example.mytodoapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getActiveTasks(): Flow<List<Task>> =
        taskDao.getActiveTasks().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getCompletedTasks(): Flow<List<Task>> =
        taskDao.getCompletedTasks().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getTasksByPriority(priority: String): Flow<List<Task>> =
        taskDao.getTasksByPriority(priority).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getActiveTaskCount(): Flow<Int> =
        taskDao.getActiveTaskCount()

    override fun getCompletedTaskCount(): Flow<Int> =
        taskDao.getCompletedTaskCount()

    override suspend fun getTaskById(id: Long): Task? =
        taskDao.getTaskById(id)?.toDomain()

    override suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(task.toEntity())

    override suspend fun updateTask(task: Task) =
        taskDao.updateTask(task.toEntity())

    override suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(task.toEntity())

    override suspend fun deleteTaskById(id: Long) =
        taskDao.deleteTaskById(id)

    override suspend fun toggleTaskCompletion(id: Long, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        taskDao.updateTaskCompletion(id, isCompleted, completedAt)
    }

    override suspend fun insertSubtask(subtask: Subtask): Long =
        taskDao.insertSubtask(subtask.toEntity())

    override suspend fun updateSubtask(subtask: Subtask) =
        taskDao.updateSubtask(subtask.toEntity())

    override suspend fun deleteSubtask(subtask: Subtask) =
        taskDao.deleteSubtask(subtask.toEntity())
}