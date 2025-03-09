package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import com.esgi.todoapp.util.Constants.ERROR_INVALID_ID
import javax.inject.Inject

/**
 * Use case for retrieving a specific task by its ID.
 * This use case adds validation to ensure the ID is valid before querying the repository.
 *
 * @property repository The TaskRepository implementation to retrieve the task from
 */
class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Invokes the use case to retrieve a task by its ID.
     * Validates that the ID is positive before querying the repository.
     *
     * @param id The unique identifier of the task to retrieve
     * @return Result containing the Task if found, or an error message
     */
    suspend operator fun invoke(id: Int): Result<Task?> {
        if (id <= 0) {
            return Result.error(ERROR_INVALID_ID)
        }
        return repository.getTaskById(id)
    }
}