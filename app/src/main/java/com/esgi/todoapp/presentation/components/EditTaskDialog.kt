package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.presentation.theme.TodoAppEnzoHugonnierTheme
import com.esgi.todoapp.util.Constants.PADDING_EXTRA_LARGE
import com.esgi.todoapp.util.Constants.PADDING_LARGE
import com.esgi.todoapp.util.Constants.PADDING_MEDIUM
import java.util.Date

/**
 * Dialog component for editing an existing task.
 * Allows the user to modify the title, description, and completion status of a task.
 * Also provides buttons to save changes, delete the task, or cancel the operation.
 *
 * @param task The task to be edited
 * @param onDismiss Function to call when the dialog is dismissed
 * @param onConfirm Function to call when changes are confirmed, passing the updated task
 * @param onDelete Function to call when the delete button is clicked, passing the task to delete
 */
@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var isCompleted by remember { mutableStateOf(task.completed) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(PADDING_LARGE.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(PADDING_EXTRA_LARGE.dp)
        ) {
            Text(
                text = "Modifier la tâche",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(PADDING_LARGE.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(PADDING_LARGE.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(PADDING_LARGE.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PADDING_MEDIUM.dp)
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    text = "Tâche terminée",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(PADDING_EXTRA_LARGE.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PADDING_MEDIUM.dp)
            ) {
                OutlinedButton(
                    onClick = { onDelete(task) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            contentDescription = "Supprimer la tâche"
                        }
                ) {
                    Text("Supprimer")
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onConfirm(
                                task.copy(
                                    title = title,
                                    description = description,
                                    completed = isCompleted
                                )
                            )
                        }
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            contentDescription = "Enregistrer les modifications"
                        }
                ) {
                    Text("Enregistrer")
                }
            }

            Spacer(modifier = Modifier.height(PADDING_MEDIUM.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Annuler")
            }
        }
    }
}

/**
 * Preview for the EditTaskDialog composable.
 * Shows how the dialog appears with a sample task.
 */
@Preview(showBackground = true, widthDp = 400)
@Composable
fun EditTaskDialogPreview() {
    TodoAppEnzoHugonnierTheme {
        Surface {
            EditTaskDialog(
                task = Task(
                    id = 1,
                    title = "Exemple de tâche",
                    description = "Description de la tâche",
                    creationDate = Date(),
                    completed = false
                ),
                onDismiss = {},
                onConfirm = {},
                onDelete = {}
            )
        }
    }
}