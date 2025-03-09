package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import com.esgi.todoapp.util.Constants.ERROR_INVALID_ID
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*
import org.junit.Assert.*

class GetTaskByIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `invoke with valid id returns task`(): Unit = runBlocking {
        // Given
        val taskId = 1
        val task = Task(
            id = taskId,
            title = "Test Task",
            description = "Test Description",
            creationDate = Date(),
            completed = false
        )

        `when`(taskRepository.getTaskById(taskId)).thenReturn(Result.success(task))

        // When
        val result = getTaskByIdUseCase(taskId)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(task, (result as Result.Success).data)
        verify(taskRepository).getTaskById(taskId)
    }

    @Test
    fun `invoke with invalid id returns error`(): Unit = runBlocking {
        // Given
        val invalidId = 0

        // When
        val result = getTaskByIdUseCase(invalidId)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(ERROR_INVALID_ID, (result as Result.Error).message)
        verify(taskRepository, never()).getTaskById(invalidId)
    }

    @Test
    fun `invoke handles repository error`(): Unit = runBlocking {
        // Given
        val taskId = 1
        val errorMessage = "Error fetching task"
        `when`(taskRepository.getTaskById(taskId)).thenReturn(Result.error(errorMessage))

        // When
        val result = getTaskByIdUseCase(taskId)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
        verify(taskRepository).getTaskById(taskId)
    }
}