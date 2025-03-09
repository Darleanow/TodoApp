package com.esgi.todoapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight

/**
 * Top app bar component for the task screen.
 * Displays the title with a count of tasks and action buttons for adding and clearing tasks.
 * Uses semantic properties to improve accessibility.
 *
 * @param onAddClick Function to call when the add button is clicked
 * @param onClearAllClick Function to call when the clear all button is clicked
 * @param taskCount The number of tasks to display in the title
 */
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics {
                    contentDescription = "En-tête: Mes Tâches, $taskCount tâches au total"
                }
            )
        },
        actions = {
            IconButton(
                onClick = onClearAllClick,
                modifier = Modifier.semantics {
                    contentDescription = "Supprimer toutes les tâches"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}