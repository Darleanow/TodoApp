package com.esgi.todoapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.todoapp.presentation.components.AddTaskDialog
import com.esgi.todoapp.presentation.components.EditTaskDialog
import com.esgi.todoapp.presentation.components.TaskList
import com.esgi.todoapp.presentation.components.TopAppBarTask
import com.esgi.todoapp.presentation.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Show dialogs
    if (isAddingTask) {
        AddTaskDialog(
            onDismiss = viewModel::onAddTaskDismiss,
            onConfirm = viewModel::addTask
        )
    }

    selectedTask?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { viewModel.onTaskSelected(null) },
            onConfirm = { updatedTask ->
                viewModel.updateTask(updatedTask)
            },
            onDelete = { taskToDelete ->
                viewModel.deleteTask(taskToDelete)
            }
        )
    }

    // Show error snackbar if there's an error
    errorMessage?.let { error ->
        LaunchedEffect(error) {
            // You can add more advanced error handling here if needed
        }
    }

    Scaffold(
        topBar = {
            TopAppBarTask(
                onAddClick = viewModel::onAddTaskClick,
                onClearAllClick = viewModel::deleteAllTasks,
                taskCount = tasks.size
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter une tâche"
                )
            }
        },
        snackbarHost = {
            errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = viewModel::clearError) {
                            Text("OK")
                        }
                    },
                    dismissAction = {
                        IconButton(onClick = viewModel::clearError) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer"
                            )
                        }
                    }
                ) {
                    Text(text = error)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (tasks.isEmpty()) {
                Text(
                    text = "Aucune tâche pour le moment.\nAppuyez sur le bouton + pour en créer une.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                TaskList(
                    tasks = tasks,
                    onTaskClick = viewModel::onTaskSelected,
                    onToggleComplete = viewModel::toggleTaskCompletion,
                    onDeleteClick = viewModel::deleteTask,
                    contentPadding = PaddingValues(bottom = 80.dp)
                )
            }
        }
    }
}