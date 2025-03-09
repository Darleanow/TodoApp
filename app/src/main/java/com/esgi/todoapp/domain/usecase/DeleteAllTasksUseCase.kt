package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import javax.inject.Inject

/**
 * Use case for deleting all tasks from the repository.
 * This use case provides a clean way to trigger the deletion of all tasks
 * without exposing the repository implementation details to the presentation layer.
 *
 * @property repository The TaskRepository implementation to delete tasks from
 */
class DeleteAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Invokes the use case to delete all tasks.
     *
     * @return Result indicating success or providing an error message
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.deleteAllTasks()
    }
}