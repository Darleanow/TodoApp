package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import com.esgi.todoapp.util.Constants.ERROR_DESCRIPTION_TOO_LONG
import com.esgi.todoapp.util.Constants.ERROR_EMPTY_TITLE
import com.esgi.todoapp.util.Constants.ERROR_TITLE_TOO_LONG
import com.esgi.todoapp.util.Constants.MAX_DESCRIPTION_LENGTH
import com.esgi.todoapp.util.Constants.MAX_TITLE_LENGTH
import javax.inject.Inject

/**
 * Use case for updating an existing task in the repository.
 * Implements business logic for validating task data before updating.
 *
 * @property repository The TaskRepository implementation to update the task in
 */
class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Validates and updates a task in the repository.
     * Checks that the task title is not blank and doesn't exceed the maximum length.
     * Also validates that the description doesn't exceed the maximum length.
     *
     * @param task The Task with updated values
     * @return Result indicating success or providing an error message
     */
    suspend operator fun invoke(task: Task): Result<Unit> {
        when {
            task.title.isBlank() -> {
                return Result.error(ERROR_EMPTY_TITLE)
            }

            task.title.length > MAX_TITLE_LENGTH -> {
                return Result.error(ERROR_TITLE_TOO_LONG)
            }

            task.description.length > MAX_DESCRIPTION_LENGTH -> {
                return Result.error(ERROR_DESCRIPTION_TOO_LONG)
            }
        }
        return repository.updateTask(task)
    }
}