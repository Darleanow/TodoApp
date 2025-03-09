package com.esgi.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.usecase.TaskUseCases
import com.esgi.todoapp.domain.util.Result
import com.esgi.todoapp.util.Constants.ERROR_DESCRIPTION_TOO_LONG
import com.esgi.todoapp.util.Constants.ERROR_TITLE_TOO_LONG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the task detail screen.
 * Manages operations specific to viewing and editing a single task.
 *
 * @property taskUseCases Collection of use cases for task-related operations
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    /**
     * UI state for the task detail screen.
     *
     * @property task The task being displayed/edited, or null if none
     * @property isEditing Whether the task is currently being edited
     */
    data class TaskDetailUiState(
        val task: Task? = null,
        val isEditing: Boolean = false
    ) : BaseUiState()

    private val _uiState = MutableStateFlow(TaskDetailUiState().apply { setLoading(true) })

    /**
     * Updates an existing task after validating input.
     *
     * @param task The task with updated values
     */
    fun updateTask(task: Task) {
        if (task.title.isBlank()) return
        if (task.title.length > 50) {
            _uiState.value = _uiState.value.apply {
                setError(ERROR_TITLE_TOO_LONG)
            }
            return
        }
        if (task.description.length > 500) {
            _uiState.value = _uiState.value.apply {
                setError(ERROR_DESCRIPTION_TOO_LONG)
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.apply { setLoading(true) }

            when (val result = taskUseCases.updateTask(task)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setSuccess(true)
                        setError(null)
                    }.copy(
                        task = task,
                        isEditing = false
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError("Erreur lors de la mise à jour: ${result.message}")
                    }
                }

                is Result.Loading -> {
                }
            }
        }
    }

    /**
     * Deletes the current task.
     *
     * @param task The task to delete
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.apply { setLoading(true) }

            when (val result = taskUseCases.deleteTask(task)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setSuccess(true)
                        setError(null)
                    }.copy(task = null)
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError("Erreur lors de la suppression: ${result.message}")
                    }
                }

                is Result.Loading -> {
                }
            }
        }
    }
}