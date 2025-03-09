package com.esgi.todoapp.data.repository

import com.esgi.todoapp.data.dao.TaskDao
import com.esgi.todoapp.data.entity.toTask
import com.esgi.todoapp.data.entity.toTaskEntity
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    override suspend fun getTaskById(id: Int): Result<Task?> {
        return try {
            val task = taskDao.getTaskById(id)?.toTask()
            Result.success(task)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun insertTask(task: Task): Result<Unit> {
        return try {
            taskDao.insertTask(task.toTaskEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            taskDao.updateTask(task.toTaskEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun deleteTask(task: Task): Result<Unit> {
        return try {
            taskDao.deleteTask(task.toTaskEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun deleteAllTasks(): Result<Unit> {
        return try {
            taskDao.deleteAllTasks()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
}