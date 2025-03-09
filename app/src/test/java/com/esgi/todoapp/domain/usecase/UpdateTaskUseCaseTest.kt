package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import com.esgi.todoapp.util.Constants.ERROR_EMPTY_TITLE
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*
import org.junit.Assert.*

class UpdateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
    }

    @Test
    fun `invoke with valid task returns success`(): Unit = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "Updated Task",
            description = "Updated Description",
            creationDate = Date(),
            completed = true
        )

        `when`(taskRepository.updateTask(task)).thenReturn(Result.success(Unit))

        // When
        val result = updateTaskUseCase(task)

        // Then
        assertTrue(result is Result.Success)
        verify(taskRepository).updateTask(task)
    }

    @Test
    fun `invoke with blank title returns error`(): Unit = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "",
            description = "Updated Description",
            creationDate = Date(),
            completed = true
        )

        // When
        val result = updateTaskUseCase(task)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(ERROR_EMPTY_TITLE, (result as Result.Error).message)
        verify(taskRepository, never()).updateTask(task)
    }

    @Test
    fun `invoke handles repository error`(): Unit = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "Updated Task",
            description = "Updated Description",
            creationDate = Date(),
            completed = true
        )

        val errorMessage = "Error updating task"
        `when`(taskRepository.updateTask(task)).thenReturn(Result.error(errorMessage))

        // When
        val result = updateTaskUseCase(task)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
        verify(taskRepository).updateTask(task)
    }
}