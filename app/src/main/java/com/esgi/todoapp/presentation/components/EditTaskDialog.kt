package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.presentation.theme.TodoAppEnzoHugonnierTheme
import com.esgi.todoapp.util.Constants.ERROR_DESCRIPTION_TOO_LONG
import com.esgi.todoapp.util.Constants.ERROR_TITLE_TOO_LONG
import com.esgi.todoapp.util.Constants.MAX_DESCRIPTION_LENGTH
import com.esgi.todoapp.util.Constants.MAX_TITLE_LENGTH
import com.esgi.todoapp.util.Constants.PADDING_EXTRA_LARGE
import com.esgi.todoapp.util.Constants.PADDING_LARGE
import com.esgi.todoapp.util.Constants.PADDING_MEDIUM
import java.util.Date

/**
 * Dialog component for editing an existing task.
 * Allows the user to modify the title, description, and completion status of a task.
 * Also provides buttons to save changes, delete the task, or cancel the operation.
 * Includes validation with error messages.
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
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }

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
                onValueChange = {
                    title = it
                    titleError =
                        if (title.isEmpty()) "Le titre ne peut pas être vide" else if (title.length > MAX_TITLE_LENGTH) ERROR_TITLE_TOO_LONG else null
                },
                label = { Text("Titre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = titleError != null,
                supportingText = {
                    titleError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(PADDING_LARGE.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError =
                        if (description.length > MAX_DESCRIPTION_LENGTH) ERROR_DESCRIPTION_TOO_LONG else null
                },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                isError = descriptionError != null,
                supportingText = {
                    if (descriptionError != null) {
                        Text(
                            text = descriptionError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        Text(
                            text = "${description.length}/$MAX_DESCRIPTION_LENGTH",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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
                        titleError =
                            if (title.isEmpty()) "Le titre ne peut pas être vide" else if (title.length > MAX_TITLE_LENGTH) ERROR_TITLE_TOO_LONG else null
                        descriptionError =
                            if (description.length > MAX_DESCRIPTION_LENGTH) ERROR_DESCRIPTION_TOO_LONG else null

                        if (titleError == null && descriptionError == null) {
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