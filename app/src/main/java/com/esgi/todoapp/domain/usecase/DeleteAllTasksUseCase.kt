package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllTasks()
    }
}