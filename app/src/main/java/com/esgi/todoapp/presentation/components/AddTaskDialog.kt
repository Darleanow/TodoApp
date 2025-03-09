package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.esgi.todoapp.util.Constants.PADDING_MEDIUM
import com.esgi.todoapp.presentation.theme.TodoAppEnzoHugonnierTheme
import com.esgi.todoapp.util.Constants.PADDING_EXTRA_LARGE
import com.esgi.todoapp.util.Constants.PADDING_LARGE

/**
 * Composable dialog for adding a new task.
 * Provides input fields for the task title and description and buttons for confirmation or dismissal.
 *
 * @param onDismiss Function to call when the dialog is dismissed
 * @param onConfirm Function to call when a task is confirmed, passing the title and description
 */
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(PADDING_LARGE.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(PADDING_EXTRA_LARGE.dp)
        ) {
            Text(
                text = "Ajouter une tâche",
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

            Spacer(modifier = Modifier.height(PADDING_EXTRA_LARGE.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Annuler")
                }

                Spacer(modifier = Modifier.width(PADDING_MEDIUM.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onConfirm(title, description)
                        }
                    },
                    enabled = title.isNotBlank()
                ) {
                    Text("Ajouter")
                }
            }
        }
    }
}

/**
 * Preview for the AddTaskDialog composable.
 */
@Preview(showBackground = true, widthDp = 400)
@Composable
fun AddTaskDialogPreview() {
    TodoAppEnzoHugonnierTheme {
        Surface {
            AddTaskDialog(
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }
    }
}