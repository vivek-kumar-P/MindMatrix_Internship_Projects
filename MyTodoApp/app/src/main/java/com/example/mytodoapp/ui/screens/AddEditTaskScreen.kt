package com.example.mytodoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mytodoapp.domain.model.*
import com.example.mytodoapp.ui.components.TaskDatePickerDialog
import com.example.mytodoapp.ui.components.TagChip
import com.example.mytodoapp.ui.components.toFormattedDate
import com.example.mytodoapp.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditing = taskId != null
    val existingTask = uiState.allTasks.find { it.id == taskId }

    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var description by remember { mutableStateOf(existingTask?.description ?: "") }
    var priority by remember { mutableStateOf(existingTask?.priority ?: Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf(existingTask?.dueDate) }
    var recurrence by remember { mutableStateOf(existingTask?.recurrenceType ?: RecurrenceType.NONE) }
    var tags by remember { mutableStateOf(existingTask?.tags ?: emptyList<Tag>()) }
    var subtasks by remember { mutableStateOf(existingTask?.subtasks ?: emptyList<Subtask>()) }
    var newSubtaskText by remember { mutableStateOf("") }
    var newTagText by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPriorityMenu by remember { mutableStateOf(false) }
    var showRecurrenceMenu by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }

    if (showDatePicker) {
        TaskDatePickerDialog(
            initialDate = dueDate,
            onDateSelected = { dueDate = it },
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Task" else "New Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isBlank()) {
                                titleError = true
                                return@TextButton
                            }
                            val task = Task(
                                id = existingTask?.id ?: 0L,
                                title = title.trim(),
                                description = description.trim(),
                                priority = priority,
                                dueDate = dueDate,
                                recurrenceType = recurrence,
                                tags = tags,
                                subtasks = subtasks,
                                isCompleted = existingTask?.isCompleted ?: false,
                                createdAt = existingTask?.createdAt ?: System.currentTimeMillis()
                            )
                            if (isEditing) viewModel.updateTask(task)
                            else viewModel.addTask(task)
                            onNavigateBack()
                        }
                    ) {
                        Text(if (isEditing) "Update" else "Save")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Title
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; titleError = false },
                    label = { Text("Task title *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleError,
                    supportingText = if (titleError) {{ Text("Title is required") }} else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Description
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Priority
            item {
                Text("Priority", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Priority.entries.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.label) }
                        )
                    }
                }
            }

            // Due date
            item {
                Text("Due Date", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(dueDate?.toFormattedDate() ?: "Pick a date")
                    }
                    if (dueDate != null) {
                        IconButton(onClick = { dueDate = null }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear date",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            // Recurrence
            item {
                Text("Recurrence", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RecurrenceType.entries.forEach { r ->
                        FilterChip(
                            selected = recurrence == r,
                            onClick = { recurrence = r },
                            label = {
                                Text(
                                    when (r) {
                                        RecurrenceType.NONE -> "None"
                                        RecurrenceType.DAILY -> "Daily"
                                        RecurrenceType.WEEKLY -> "Weekly"
                                        RecurrenceType.MONTHLY -> "Monthly"
                                    }
                                )
                            }
                        )
                    }
                }
            }

            // Tags
            item {
                Text("Tags", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTagText,
                        onValueChange = { newTagText = it },
                        label = { Text("Add tag") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    IconButton(
                        onClick = {
                            if (newTagText.isNotBlank()) {
                                tags = tags + Tag(name = newTagText.trim())
                                newTagText = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add tag")
                    }
                }
                if (tags.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(tags) { tag ->
                            TagChip(
                                tag = tag,
                                onRemove = { tags = tags.filter { it != tag } }
                            )
                        }
                    }
                }
            }

            // Subtasks
            item {
                Text("Subtasks", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newSubtaskText,
                        onValueChange = { newSubtaskText = it },
                        label = { Text("Add subtask") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    IconButton(
                        onClick = {
                            if (newSubtaskText.isNotBlank()) {
                                subtasks = subtasks + Subtask(
                                    taskId = taskId ?: 0L,
                                    title = newSubtaskText.trim()
                                )
                                newSubtaskText = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add subtask")
                    }
                }
                subtasks.forEach { subtask ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = subtask.isCompleted,
                            onCheckedChange = {
                                subtasks = subtasks.map { s ->
                                    if (s == subtask) s.copy(isCompleted = it) else s
                                }
                            }
                        )
                        Text(
                            text = subtask.title,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = { subtasks = subtasks.filter { it != subtask } }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove subtask",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}