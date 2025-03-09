package com.esgi.todoapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.usecase.TaskUseCases
import com.esgi.todoapp.domain.util.Result
import com.esgi.todoapp.util.Constants.ERROR_DESCRIPTION_TOO_LONG
import com.esgi.todoapp.util.Constants.ERROR_TITLE_TOO_LONG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Date

/**
 * ViewModel responsible for managing the task list screen UI state and interactions.
 * Handles operations like adding, updating, deleting, and toggling completion status of tasks.
 * Uses TaskUseCases to interact with the domain layer.
 *
 * @property taskUseCases Collection of use cases for task-related operations
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    /**
     * UI state for the task list screen.
     *
     * @property isAddingTask Whether the add task dialog is currently displayed
     * @property selectedTask The currently selected task (for editing), or null if none
     */
    data class TaskUiState(
        val isAddingTask: Boolean = false,
        val selectedTask: Task? = null
    ) : BaseUiState()

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    /**
     * StateFlow of all tasks in the repository.
     * Maps the Result from the repository to a list of tasks, handling errors appropriately.
     */
    val tasks: StateFlow<List<Task>> = taskUseCases.getAllTasks()
        .map { result ->
            when (result) {
                is Result.Success -> result.data
                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setError(result.message)
                    }
                    emptyList()
                }
                is Result.Loading -> emptyList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    /**
     * Shows the add task dialog.
     */
    fun onAddTaskClick() {
        _uiState.value = _uiState.value.copy(isAddingTask = true)
    }

    /**
     * Hides the add task dialog.
     */
    fun onAddTaskDismiss() {
        _uiState.value = _uiState.value.copy(isAddingTask = false)
    }

    /**
     * Sets the currently selected task for editing.
     *
     * @param task The task to select, or null to deselect
     */
    fun onTaskSelected(task: Task?) {
        _uiState.value = _uiState.value.copy(selectedTask = task)
    }

    /**
     * Clears any error message in the UI state.
     */
    fun clearError() {
        _uiState.value.clearError()
    }

    /**
     * Clears the success flag in the UI state.
     */
    fun clearSuccess() {
        _uiState.value.clearSuccess()
    }

    /**
     * Adds a new task with the provided title and description.
     * Validates input before creating the task.
     *
     * @param title The title for the new task
     * @param description The description for the new task
     */
    fun addTask(title: String, description: String) {
        if (title.isBlank()) return
        if (title.length > 50) {
            _uiState.value = _uiState.value.apply {
                setError(ERROR_TITLE_TOO_LONG)
            }
            return
        }
        if (description.length > 500) {
            _uiState.value = _uiState.value.apply {
                setError(ERROR_DESCRIPTION_TOO_LONG)
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.apply { setLoading(true) }

            val task = Task(
                title = title,
                description = description,
                creationDate = Date()
            )

            when (val result = taskUseCases.addTask(task)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setSuccess(true)
                    }.copy(isAddingTask = false)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError("Erreur lors de l'ajout de la tâche: ${result.message}")
                    }
                    Log.e("TaskViewModel", "Erreur lors de l'ajout de la tâche", result.exception)
                }
                is Result.Loading -> {
                }
            }
        }
    }

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
                    }.copy(selectedTask = null)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError(result.message)
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    /**
     * Deletes a specific task.
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
                    }.copy(selectedTask = if (_uiState.value.selectedTask?.id == task.id) null else _uiState.value.selectedTask)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError(result.message)
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    /**
     * Deletes all tasks in the repository.
     */
    fun deleteAllTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.apply { setLoading(true) }

            when (val result = taskUseCases.deleteAllTasks()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setSuccess(true)
                    }.copy(selectedTask = null)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError(result.message)
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }

    /**
     * Toggles the completion status of a task.
     *
     * @param task The task to toggle
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.apply { setLoading(true) }

            val updatedTask = task.copy(completed = !task.completed)
            when (val result = taskUseCases.updateTask(updatedTask)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                    }
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.apply {
                        setLoading(false)
                        setError(result.message)
                    }
                }
                is Result.Loading -> {
                }
            }
        }
    }
}