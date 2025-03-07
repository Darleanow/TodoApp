package com.esgi.todoapp.domain.usecase

import javax.inject.Inject

data class TaskUseCases @Inject constructor(
    val getAllTasks: GetAllTasksUseCase,
    val addTask: AddTaskUseCase,
    val updateTask: UpdateTaskUseCase,
    val deleteTask: DeleteTaskUseCase,
    val deleteAllTasks: DeleteAllTasksUseCase
)