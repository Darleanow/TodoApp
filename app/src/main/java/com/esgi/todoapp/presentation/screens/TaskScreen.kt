package com.esgi.todoapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navigateToTaskDetail: (Int) -> Unit = {},
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Gestion des messages de succès/erreur
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

    // Show dialogs
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
                        .padding(16.dp)
                )
            } else {
                TaskList(
                    tasks = tasks,
                    onTaskClick = { task ->
                        // Deux options possibles:
                        // 1. Ouvrir la boîte de dialogue d'édition (comportement actuel)
                        viewModel.onTaskSelected(task)

                        // 2. Naviguer vers un écran de détail (nouvelle fonctionnalité)
                        // navigateToTaskDetail(task.id)
                    },
                    onToggleComplete = viewModel::toggleTaskCompletion,
                    onDeleteClick = viewModel::deleteTask,
                    contentPadding = PaddingValues(bottom = 80.dp)
                )
            }
        }
    }
}