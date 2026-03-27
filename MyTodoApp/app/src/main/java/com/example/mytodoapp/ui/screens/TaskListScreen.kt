package com.example.mytodoapp.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mytodoapp.domain.model.Priority
import com.example.mytodoapp.ui.components.*
import com.example.mytodoapp.ui.viewmodel.TaskFilter
import com.example.mytodoapp.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onNavigateToAddTask: () -> Unit,
    onNavigateToTask: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<com.example.mytodoapp.domain.model.Task?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showPriorityFilter by remember { mutableStateOf(false) }

    // Undo delete snackbar
    LaunchedEffect(uiState.recentlyDeletedTask) {
        uiState.recentlyDeletedTask?.let {
            val result = snackbarHostState.showSnackbar(
                message = "\"${it.title}\" deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Tasks", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${uiState.activeCount} active · ${uiState.completedCount} done",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Date created") },
                                onClick = {
                                    viewModel.setSortOrder(com.example.mytodoapp.ui.viewmodel.SortOrder.DATE_CREATED)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.Schedule, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Due date") },
                                onClick = {
                                    viewModel.setSortOrder(com.example.mytodoapp.ui.viewmodel.SortOrder.DUE_DATE)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.CalendarToday, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Priority") },
                                onClick = {
                                    viewModel.setSortOrder(com.example.mytodoapp.ui.viewmodel.SortOrder.PRIORITY)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.Flag, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Title") },
                                onClick = {
                                    viewModel.setSortOrder(com.example.mytodoapp.ui.viewmodel.SortOrder.TITLE)
                                    showSortMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.SortByAlpha, null) }
                            )
                        }
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddTask,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Task") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress bar
            if (uiState.activeCount + uiState.completedCount > 0) {
                val progress = uiState.completedCount.toFloat() /
                        (uiState.activeCount + uiState.completedCount)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Filter tabs
            ScrollableTabRow(
                selectedTabIndex = TaskFilter.entries.indexOf(uiState.selectedFilter),
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                divider = {}
            ) {
                TaskFilter.entries.forEach { filter ->
                    Tab(
                        selected = uiState.selectedFilter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        text = {
                            Text(
                                text = when (filter) {
                                    TaskFilter.ALL -> "All"
                                    TaskFilter.TODAY -> "Today"
                                    TaskFilter.UPCOMING -> "Upcoming"
                                    TaskFilter.COMPLETED -> "Done"
                                }
                            )
                        }
                    )
                }
            }

            // Priority filter chips
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedPriority == null,
                        onClick = { viewModel.setPriorityFilter(null) },
                        label = { Text("All priorities") }
                    )
                }
                items(Priority.entries) { priority ->
                    FilterChip(
                        selected = uiState.selectedPriority == priority,
                        onClick = { viewModel.setPriorityFilter(priority) },
                        label = { Text(priority.label) }
                    )
                }
            }

            // Task list
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.filteredTasks.isEmpty()) {
                EmptyState(filter = uiState.selectedFilter)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.filteredTasks,
                        key = { it.id }
                    ) { task ->
                        SwipeableTaskItem(
                            task = task,
                            onToggleComplete = { viewModel.toggleComplete(task) },
                            onDelete = {
                                taskToDelete = task
                                viewModel.deleteTask(task)
                            },
                            onClick = { onNavigateToTask(task.id) }
                        ) {
                            TaskItem(
                                task = task,
                                onToggleComplete = { viewModel.toggleComplete(task) },
                                onDelete = {
                                    taskToDelete = task
                                    viewModel.deleteTask(task)
                                },
                                onClick = { onNavigateToTask(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(filter: TaskFilter) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = when (filter) {
                    TaskFilter.ALL -> Icons.Default.CheckCircle
                    TaskFilter.TODAY -> Icons.Default.Today
                    TaskFilter.UPCOMING -> Icons.Default.CalendarMonth
                    TaskFilter.COMPLETED -> Icons.Default.Done
                },
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                text = when (filter) {
                    TaskFilter.ALL -> "No tasks yet\nTap + to add one"
                    TaskFilter.TODAY -> "Nothing due today"
                    TaskFilter.UPCOMING -> "Nothing upcoming"
                    TaskFilter.COMPLETED -> "Nothing completed yet"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}