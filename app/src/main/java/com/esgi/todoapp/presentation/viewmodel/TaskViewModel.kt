package com.esgi.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.usecase.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = taskUseCases.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAddingTask = MutableStateFlow(false)
    val isAddingTask: StateFlow<Boolean> = _isAddingTask

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    fun onAddTaskClick() {
        _isAddingTask.value = true
    }

    fun onAddTaskDismiss() {
        _isAddingTask.value = false
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            taskUseCases.addTask(
                Task(
                    title = title,
                    description = description
                )
            )
            _isAddingTask.value = false
        }
    }

    fun onTaskSelected(task: Task?) {
        _selectedTask.value = task
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskUseCases.updateTask(task)
            _selectedTask.value = null
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskUseCases.deleteTask(task)
            if (_selectedTask.value?.id == task.id) {
                _selectedTask.value = null
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            taskUseCases.deleteAllTasks()
            _selectedTask.value = null
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskUseCases.updateTask(
                task.copy(completed = !task.completed)
            )
        }
    }
}