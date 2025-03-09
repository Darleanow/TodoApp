package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import javax.inject.Inject

/**
 * Use case for deleting a specific task from the repository.
 * This use case provides a clean way to delete a task without exposing
 * the repository implementation details to the presentation layer.
 *
 * @property repository The TaskRepository implementation to delete the task from
 */
class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Invokes the use case to delete a specific task.
     *
     * @param task The Task to be deleted
     * @return Result indicating success or providing an error message
     */
    suspend operator fun invoke(task: Task): Result<Unit> {
        return repository.deleteTask(task)
    }
}