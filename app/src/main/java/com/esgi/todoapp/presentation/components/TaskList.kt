package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.util.Constants.PADDING_LARGE
import com.esgi.todoapp.util.Constants.PADDING_MEDIUM
import com.esgi.todoapp.util.Constants.PADDING_SMALL

/**
 * Composable that displays a list of tasks using LazyColumn.
 * Tasks are sorted by creation date in descending order (newest first).
 * Each task is rendered as a TaskItem with click handlers for viewing, toggling completion, and deletion.
 *
 * @param tasks The list of tasks to display
 * @param onTaskClick Function to call when a task is clicked
 * @param onToggleComplete Function to call when a task's completion status is toggled
 * @param onDeleteClick Function to call when a task's delete button is clicked
 * @param contentPadding Padding values to apply to the content
 */
@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    contentPadding: PaddingValues
) {
    val sortedTasks by remember(tasks) {
        derivedStateOf {
            tasks.sortedByDescending { it.creationDate }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(horizontal = PADDING_LARGE.dp, vertical = PADDING_MEDIUM.dp)
    ) {
        items(
            items = sortedTasks,
            key = { task -> task.id }
        ) { task ->
            TaskItem(
                task = task,
                onTaskClick = onTaskClick,
                onToggleComplete = onToggleComplete,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.padding(vertical = PADDING_SMALL.dp)
            )
        }
    }
}