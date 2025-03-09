package com.esgi.todoapp.data.repository

import com.esgi.todoapp.data.dao.TaskDao
import com.esgi.todoapp.data.entity.toTask
import com.esgi.todoapp.data.entity.toTaskEntity
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the TaskRepository interface.
 * This class serves as a mediator between the data layer (Room database) and the domain layer.
 * It handles the conversion between database entities and domain models.
 *
 * @property taskDao The Data Access Object for task-related database operations
 */
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    /**
     * Retrieves all tasks from the database as a Flow of domain models.
     * Wraps the result in a Result class to handle success and error cases.
     *
     * @return Flow emitting Result containing a list of Task domain models
     */
    override fun getAllTasks(): Flow<Result<List<Task>>> {
        return taskDao.getAllTasks()
            .map { entities ->
                Result.success(entities.map { it.toTask() })
            }
            .catch { e ->
                emit(Result.error(e as Exception))
            }
    }

    /**
     * Retrieves a specific task by its ID.
     * Wraps the result in a Result class to handle success and error cases.
     *
     * @param id The unique identifier of the task
     * @return Result containing the Task if found, or null
     */
    override suspend fun getTaskById(id: Int): Result<Task?> {
        return try {
            val task = taskDao.getTaskById(id)?.toTask()
            Result.success(task)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    /**
     * Inserts a new task into the database.
     * Wraps the result in a Result class to handle success and error cases.
     *
     * @param task The Task domain model to insert
     * @return Result indicating success or an error
     */
    override suspend fun insertTask(task: Task): Result<Unit> {
        return try {
            taskDao.insertTask(task.toTaskEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    /**
     * Updates an existing task in the database.
     * Wraps the result in a Result class to handle success and error cases.
     *
     * @param task The Task domain model with updated values
     * @return Result indicating success or an error
     */
    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            taskDao.updateTask(task.toTaskEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    /**
     * Deletes a task from the database.
     * Wraps the result in a Result class to handle success and error cases.
     *
     * @param task The Task domain model to delete
     * @return Result indicating success or an error
     */
    override suspend fun deleteTask(task: Task): Result<Unit> {
        return try {
            taskDao.deleteTask(task.toTaskEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    /**
     * Deletes all tasks from the database.
     * Wraps the result in a Result class to handle success and error cases.
     *
     * @return Result indicating success or an error
     */
    override suspend fun deleteAllTasks(): Result<Unit> {
        return try {
            taskDao.deleteAllTasks()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
}