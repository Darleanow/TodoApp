package com.esgi.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.usecase.TaskUseCases
import com.esgi.todoapp.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    data class UiState(
        val task: Task? = null,
        val isLoading: Boolean = false,
        val isEditing: Boolean = false,
        val errorMessage: String? = null,
        val isSuccess: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = taskUseCases.getTaskById(taskId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        task = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Impossible de charger la tâche: ${result.message}"
                    )
                }
                is Result.Loading -> {
                }
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = taskUseCases.updateTask(task)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        task = task,
                        isLoading = false,
                        isEditing = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Erreur lors de la mise à jour: ${result.message}"
                    )
                }
                is Result.Loading -> {
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = taskUseCases.deleteTask(task)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        task = null,
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Erreur lors de la suppression: ${result.message}"
                    )
                }
                is Result.Loading -> {
                }
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val updatedTask = task.copy(completed = !task.completed)
            when (val result = taskUseCases.updateTask(updatedTask)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        task = updatedTask,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Erreur lors de la mise à jour: ${result.message}"
                    )
                }
                is Result.Loading -> {
                }
            }
        }
    }

    fun startEdit() {
        _uiState.value = _uiState.value.copy(isEditing = true)
    }

    fun cancelEdit() {
        _uiState.value = _uiState.value.copy(isEditing = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}