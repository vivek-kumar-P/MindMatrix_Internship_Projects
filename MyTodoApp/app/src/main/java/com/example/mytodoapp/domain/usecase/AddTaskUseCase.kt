package com.example.mytodoapp.domain.usecase

import com.example.mytodoapp.domain.model.Task
import com.example.mytodoapp.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long {
        if (task.title.isBlank()) throw IllegalArgumentException("Title cannot be empty")
        return repository.insertTask(task)
    }
}