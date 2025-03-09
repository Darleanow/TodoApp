package com.esgi.todoapp.domain.usecase

import javax.inject.Inject

/**
 * Container class that holds all the use cases related to tasks.
 * This class simplifies dependency injection by allowing a single injection point
 * for all task-related use cases.
 *
 * @property getAllTasks Use case for retrieving all tasks.
 * @property getTaskById Use case for retrieving a specific task by ID.
 * @property addTask Use case for adding a new task.
 * @property updateTask Use case for updating an existing task.
 * @property deleteTask Use case for deleting a task.
 * @property deleteAllTasks Use case for deleting all tasks.
 */
data class TaskUseCases @Inject constructor(
    val getAllTasks: GetAllTasksUseCase,
    val getTaskById: GetTaskByIdUseCase,
    val addTask: AddTaskUseCase,
    val updateTask: UpdateTaskUseCase,
    val deleteTask: DeleteTaskUseCase,
    val deleteAllTasks: DeleteAllTasksUseCase
)