package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.esgi.todoapp.domain.model.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    contentPadding: PaddingValues
) {
    // Utilisation de remember et derivedStateOf pour éviter les recompositions inutiles
    val sortedTasks = remember(tasks) {
        derivedStateOf {
            tasks.sortedByDescending { it.creationDate }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = sortedTasks.value,
            key = { task -> task.id } // Utilisation de clés pour améliorer les performances
        ) { task ->
            TaskItem(
                task = task,
                onTaskClick = onTaskClick,
                onToggleComplete = onToggleComplete,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}