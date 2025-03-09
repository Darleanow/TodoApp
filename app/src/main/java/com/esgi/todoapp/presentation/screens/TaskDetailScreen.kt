package com.esgi.todoapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.presentation.components.EditTaskDialog
import com.esgi.todoapp.presentation.theme.TaskCompleted
import com.esgi.todoapp.presentation.theme.TaskPending
import com.esgi.todoapp.presentation.theme.TodoAppEnzoHugonnierTheme
import com.esgi.todoapp.presentation.viewmodel.TaskDetailViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int,
    navigateBack: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    // Charger la tâche correspondante à l'ID
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Gérer les erreurs
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar(message = error)
                viewModel.clearError()
            }
        }
    }

    // Afficher la boîte de dialogue d'édition si nécessaire
    val currentTask = uiState.task // Stocker la référence dans une variable locale
    if (uiState.isEditing && currentTask != null) {
        EditTaskDialog(
            task = currentTask,
            onDismiss = viewModel::cancelEdit,
            onConfirm = { updatedTask ->
                viewModel.updateTask(updatedTask)
            },
            onDelete = { taskToDelete ->
                viewModel.deleteTask(taskToDelete)
                navigateBack()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails de la tâche") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::startEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modifier la tâche"
                        )
                    }
                    IconButton(onClick = {
                        val task = uiState.task
                        if (task != null) {
                            viewModel.deleteTask(task)
                            navigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer la tâche",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
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
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val task = uiState.task
                if (task == null) {
                    Text(
                        text = "Tâche non trouvée",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    TaskDetailContent(
                        task = task,
                        onToggleComplete = { taskToToggle ->
                            viewModel.toggleTaskCompletion(taskToToggle)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskDetailContent(
    task: Task,
    onToggleComplete: (Task) -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Status indicator
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (task.completed)
                    TaskCompleted.copy(alpha = 0.2f)
                else
                    TaskPending.copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (task.completed) "Tâche terminée" else "Tâche en cours",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (task.completed) TaskCompleted else TaskPending
                )

                Button(
                    onClick = { onToggleComplete(task) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.completed) TaskCompleted else TaskPending
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (task.completed) "Marquer comme non terminée" else "Marquer comme terminée"
                    )
                }
            }
        }

        // Task title
        Text(
            text = task.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Creation date
        Text(
            text = "Créée le ${dateFormatter.format(task.creationDate)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Description title
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Description content
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = task.description.ifEmpty { "Aucune description" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (task.description.isEmpty())
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailPreview() {
    TodoAppEnzoHugonnierTheme {
        TaskDetailContent(
            task = Task(
                id = 1,
                title = "Exemple de tâche",
                description = "Ceci est une description détaillée de la tâche qui peut être assez longue pour montrer comment le texte s'affiche dans l'interface.",
                creationDate = Date(),
                completed = false
            ),
            onToggleComplete = {}
        )
    }
}