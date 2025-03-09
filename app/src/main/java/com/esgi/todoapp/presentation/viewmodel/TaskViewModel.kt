package com.esgi.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.usecase.TaskUseCases
import com.esgi.todoapp.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Date

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    // État UI
    data class UiState(
        val isLoading: Boolean = false,
        val isAddingTask: Boolean = false,
        val selectedTask: Task? = null,
        val errorMessage: String? = null,
        val isSuccess: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val tasks: StateFlow<List<Task>> = taskUseCases.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions UI
    fun onAddTaskClick() {
        _uiState.value = _uiState.value.copy(isAddingTask = true)
    }

    fun onAddTaskDismiss() {
        _uiState.value = _uiState.value.copy(isAddingTask = false)
    }

    fun onTaskSelected(task: Task?) {
        _uiState.value = _uiState.value.copy(selectedTask = task)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    // Opérations de tâches
    fun addTask(title: String, description: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val task = Task(
                title = title,
                description = description,
                creationDate = Date()
            )

            when (val result = taskUseCases.addTask(task)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAddingTask = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Géré par l'état isLoading initial
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
                        isLoading = false,
                        selectedTask = null,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Géré par l'état isLoading initial
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
                        isLoading = false,
                        selectedTask = if (_uiState.value.selectedTask?.id == task.id) null else _uiState.value.selectedTask,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Géré par l'état isLoading initial
                }
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = taskUseCases.deleteAllTasks()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        selectedTask = null,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Géré par l'état isLoading initial
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
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Géré par l'état isLoading initial
                }
            }
        }
    }
}