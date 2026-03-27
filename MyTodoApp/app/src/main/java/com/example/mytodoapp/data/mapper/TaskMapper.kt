package com.example.mytodoapp.data.mapper

import com.example.mytodoapp.data.local.entity.SubtaskEntity
import com.example.mytodoapp.data.local.entity.TaskEntity
import com.example.mytodoapp.domain.model.*

fun TaskEntity.toDomain(subtasks: List<SubtaskEntity> = emptyList()): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = Priority.valueOf(priority),
        dueDate = dueDate,
        reminderTime = reminderTime,
        recurrenceType = RecurrenceType.valueOf(recurrenceType),
        tags = tags.split(",")
            .filter { it.isNotBlank() }
            .map { Tag(name = it.trim()) },
        subtasks = subtasks.map { it.toDomain() },
        projectId = projectId,
        createdAt = createdAt,
        completedAt = completedAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = priority.name,
        dueDate = dueDate,
        reminderTime = reminderTime,
        recurrenceType = recurrenceType.name,
        tags = tags.joinToString(",") { it.name },
        projectId = projectId,
        createdAt = createdAt,
        completedAt = completedAt
    )
}

fun SubtaskEntity.toDomain(): Subtask {
    return Subtask(
        id = id,
        taskId = taskId,
        title = title,
        isCompleted = isCompleted
    )
}

fun Subtask.toEntity(): SubtaskEntity {
    return SubtaskEntity(
        id = id,
        taskId = taskId,
        title = title,
        isCompleted = isCompleted
    )
}