package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*
import org.junit.Assert.*

class DeleteTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `invoke deletes task successfully`(): Unit = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            creationDate = Date(),
            completed = false
        )

        `when`(taskRepository.deleteTask(task)).thenReturn(Result.success(Unit))

        // When
        val result = deleteTaskUseCase(task)

        // Then
        assertTrue(result is Result.Success)
        verify(taskRepository).deleteTask(task)
    }

    @Test
    fun `invoke handles repository error`(): Unit = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            creationDate = Date(),
            completed = false
        )

        val errorMessage = "Error deleting task"
        `when`(taskRepository.deleteTask(task)).thenReturn(Result.error(errorMessage))

        // When
        val result = deleteTaskUseCase(task)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
        verify(taskRepository).deleteTask(task)
    }
}