package com.esgi.todoapp.data.repository

import com.esgi.todoapp.data.dao.TaskDao
import com.esgi.todoapp.data.entity.toTask
import com.esgi.todoapp.data.entity.toTaskEntity
import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
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

    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)?.toTask()
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toTaskEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toTaskEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toTaskEntity())
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}