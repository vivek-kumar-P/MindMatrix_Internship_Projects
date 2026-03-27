package com.example.mytodoapp.domain.usecase

import com.example.mytodoapp.domain.repository.TaskRepository
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(query: String) = repository.searchTasks(query)
}