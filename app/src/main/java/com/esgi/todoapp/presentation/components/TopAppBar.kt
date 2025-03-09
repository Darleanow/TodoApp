package com.esgi.todoapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarTask(
    onAddClick: () -> Unit,
    onClearAllClick: () -> Unit,
    taskCount: Int
) {
    TopAppBar(
        title = {
            Text(
                text = "Mes Tâches ($taskCount)",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter une tâche"
                )
            }
            IconButton(onClick = onClearAllClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer toutes les tâches"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}