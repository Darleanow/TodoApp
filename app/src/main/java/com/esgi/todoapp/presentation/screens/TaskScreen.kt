package com.esgi.todoapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.esgi.todoapp.util.Constants.PADDING_LARGE
import kotlinx.coroutines.launch

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.isSuccess, uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = "OK"
                )
                viewModel.clearError()
            }
        }

        if (uiState.isSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Opération réussie",
                    actionLabel = "OK"
                )
                viewModel.clearSuccess()
            }
        }
    }

    if (uiState.isAddingTask) {
        AddTaskDialog(
            onDismiss = viewModel::onAddTaskDismiss,
            onConfirm = viewModel::addTask
        )
    }

    uiState.selectedTask?.let { task ->
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

    Scaffold(
        topBar = {
            TopAppBarTask(
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
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
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
                        .padding(PADDING_LARGE.dp)
                )
            } else {
                TaskList(
                    tasks = tasks,
                    onTaskClick = { task ->
                        viewModel.onTaskSelected(task)
                    },
                    onToggleComplete = viewModel::toggleTaskCompletion,
                    onDeleteClick = viewModel::deleteTask,
                    contentPadding = PaddingValues(bottom = 80.dp)
                )
            }
        }
    }
}