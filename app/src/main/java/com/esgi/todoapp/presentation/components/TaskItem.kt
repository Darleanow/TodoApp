package com.esgi.todoapp.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.presentation.theme.TaskCompleted
import com.esgi.todoapp.presentation.theme.TaskPending
import com.esgi.todoapp.presentation.theme.TodoAppEnzoHugonnierTheme
import com.esgi.todoapp.util.Constants.ANIMATION_DURATION
import com.esgi.todoapp.util.Constants.PADDING_LARGE
import com.esgi.todoapp.util.Constants.PADDING_SMALL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable for displaying a single task item in the task list.
 * Features enhanced animated color transitions based on completion status,
 * displays task details (title, description, creation date),
 * and provides buttons for completing and deleting tasks.
 * Accessibility is enhanced with semantic properties.
 *
 * @param task The task to display
 * @param onTaskClick Function to call when the task card is clicked
 * @param onToggleComplete Function to call when the completion status is toggled
 * @param onDeleteClick Function to call when the delete button is clicked
 * @param modifier Modifier to be applied to the card
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit,
    onDeleteClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (task.completed) MaterialTheme.colorScheme.surface.copy(alpha = 0.95f) else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
        label = "background color"
    )

    val statusColor by animateColorAsState(
        targetValue = if (task.completed) TaskCompleted else TaskPending,
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
        label = "status color"
    )

    val cardElevation by animateDpAsState(
        targetValue = if (task.completed) 0.dp else 4.dp,
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
        label = "card elevation"
    )

    val dateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val statusText = if (task.completed) "Tâche terminée" else "Tâche en cours"
    val taskContentDescription = "${task.title}. ${statusText}. Créée le ${dateFormatter.format(task.creationDate)}"

    val scale by animateFloatAsState(
        targetValue = if (task.completed) 1f else 1f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION),
        label = "card scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = PADDING_SMALL.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                role = Role.Button,
                onClickLabel = "Voir les détails de la tâche ${task.title}"
            ) {
                onTaskClick(task)
            }
            .semantics {
                contentDescription = taskContentDescription
            },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = if (task.completed)
            CardDefaults.outlinedCardBorder().copy(
                width = 1.dp,
            )
        else null,
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_LARGE.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBadge(
                isCompleted = task.completed,
                modifier = Modifier.semantics {
                    contentDescription = statusText
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = PADDING_LARGE.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                    color = if (task.completed)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(PADDING_SMALL.dp))

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = if (task.completed) 0.4f else 0.7f
                    )
                )

                Spacer(modifier = Modifier.height(PADDING_SMALL.dp))

                Text(
                    text = "Créée le ${dateFormatter.format(task.creationDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Row {
                IconButton(
                    onClick = { onToggleComplete(task) },
                    modifier = Modifier.semantics {
                        contentDescription = if (task.completed)
                            "Marquer comme non terminée"
                        else
                            "Marquer comme terminée"
                    }
                ) {
                    Icon(
                        imageVector = if (task.completed) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = null,
                        tint = if (task.completed) TaskCompleted.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.scale(if (task.completed) 1.1f else 1f)
                    )
                }

                IconButton(
                    onClick = { onDeleteClick(task) },
                    modifier = Modifier.semantics {
                        contentDescription = "Supprimer la tâche ${task.title}"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Preview for an uncompleted task item.
 */
@Preview(name = "Task Item (Non complétée)", showBackground = true)
@Composable
fun TaskItemPreview() {
    TodoAppEnzoHugonnierTheme {
        TaskItem(
            task = Task(
                id = 1,
                title = "Exemple de tâche",
                description = "Ceci est une description de tâche qui pourrait être assez longue pour être tronquée sur deux lignes maximum.",
                creationDate = Date(),
                completed = false
            ),
            onTaskClick = {},
            onToggleComplete = {},
            onDeleteClick = {}
        )
    }
}

/**
 * Preview for a completed task item.
 */
@Preview(name = "Task Item (Complétée)", showBackground = true)
@Composable
fun CompletedTaskItemPreview() {
    TodoAppEnzoHugonnierTheme {
        TaskItem(
            task = Task(
                id = 2,
                title = "Tâche terminée",
                description = "Cette tâche est marquée comme terminée.",
                creationDate = Date(),
                completed = true
            ),
            onTaskClick = {},
            onToggleComplete = {},
            onDeleteClick = {}
        )
    }
}