package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.esgi.todoapp.presentation.theme.TodoAppEnzoHugonnierTheme
import com.esgi.todoapp.util.Constants.ERROR_DESCRIPTION_TOO_LONG
import com.esgi.todoapp.util.Constants.ERROR_TITLE_TOO_LONG
import com.esgi.todoapp.util.Constants.MAX_DESCRIPTION_LENGTH
import com.esgi.todoapp.util.Constants.MAX_TITLE_LENGTH
import com.esgi.todoapp.util.Constants.PADDING_EXTRA_LARGE
import com.esgi.todoapp.util.Constants.PADDING_LARGE
import com.esgi.todoapp.util.Constants.PADDING_MEDIUM

/**
 * Composable dialog for adding a new task.
 * Provides input fields for the task title and description and buttons for confirmation or dismissal.
 * Includes validation with error messages.
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
                text = "Ajouter une tâche",
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
                        titleError =
                            if (title.isEmpty()) "Le titre ne peut pas être vide" else if (title.length > MAX_TITLE_LENGTH) ERROR_TITLE_TOO_LONG else null
                        descriptionError =
                            if (description.length > MAX_DESCRIPTION_LENGTH) ERROR_DESCRIPTION_TOO_LONG else null

                        if (titleError == null && descriptionError == null && title.isNotBlank()) {
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