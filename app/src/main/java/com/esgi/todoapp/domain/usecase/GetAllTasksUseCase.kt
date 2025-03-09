package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all tasks from the repository.
 * This use case provides a clean way to access all tasks without exposing
 * the repository implementation details to the presentation layer.
 *
 * @property repository The TaskRepository implementation to retrieve tasks from
 */
class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Invokes the use case to retrieve all tasks.
     * Returns a Flow to observe changes in the task list over time.
     *
     * @return Flow emitting Result containing the list of tasks
     */
    operator fun invoke(): Flow<Result<List<Task>>> {
        return repository.getAllTasks()
    }
}