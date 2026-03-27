package com.example.mytodoapp.domain.usecase

import com.example.mytodoapp.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleCompleteUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long, isCompleted: Boolean) =
        repository.toggleTaskCompletion(id, isCompleted)
}