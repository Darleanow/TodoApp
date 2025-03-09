package com.esgi.todoapp.domain.repository

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for handling Task data operations.
 * This interface abstracts the data layer and provides methods
 * for CRUD operations on tasks.
 */
interface TaskRepository {
    /**
     * Gets a Flow of all tasks in the database wrapped in Result.
     * @return Flow emitting Result containing lists of tasks when the data changes.
     */
    fun getAllTasks(): Flow<Result<List<Task>>>

    /**
     * Gets a task by its unique identifier.
     * @param id The unique identifier of the task to retrieve.
     * @return Result containing the task if found, or an error.
     */
    suspend fun getTaskById(id: Int): Result<Task?>

    /**
     * Inserts a new task into the database.
     * @param task The task to insert.
     * @return Result indicating success or an error.
     */
    suspend fun insertTask(task: Task): Result<Unit>

    /**
     * Updates an existing task in the database.
     * @param task The task with updated values to save.
     * @return Result indicating success or an error.
     */
    suspend fun updateTask(task: Task): Result<Unit>

    /**
     * Deletes a task from the database.
     * @param task The task to delete.
     * @return Result indicating success or an error.
     */
    suspend fun deleteTask(task: Task): Result<Unit>

    /**
     * Deletes all tasks from the database.
     * @return Result indicating success or an error.
     */
    suspend fun deleteAllTasks(): Result<Unit>
}