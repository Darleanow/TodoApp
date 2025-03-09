package com.esgi.todoapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esgi.todoapp.presentation.theme.TaskCompleted
import com.esgi.todoapp.presentation.theme.TaskPending

/**
 * A badge component to display the status of a task.
 * The badge changes color and text based on the completed state.
 *
 * @param isCompleted Whether the task is completed
 * @param modifier Modifier to be applied to the badge
 */
@Composable
fun StatusBadge(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isCompleted) TaskCompleted.copy(alpha = 0.7f) else TaskPending
    val textColor = Color.White
    val text = if (isCompleted) "Terminé" else "En cours"

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor.copy(alpha = 0.8f))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}