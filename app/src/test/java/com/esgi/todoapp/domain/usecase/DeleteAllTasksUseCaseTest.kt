package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.junit.Assert.*

class DeleteAllTasksUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteAllTasksUseCase: DeleteAllTasksUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        deleteAllTasksUseCase = DeleteAllTasksUseCase(taskRepository)
    }

    @Test
    fun `invoke deletes all tasks successfully`(): Unit = runBlocking {
        // Given
        `when`(taskRepository.deleteAllTasks()).thenReturn(Result.success(Unit))

        // When
        val result = deleteAllTasksUseCase()

        // Then
        assertTrue(result is Result.Success)
        verify(taskRepository).deleteAllTasks()
    }

    @Test
    fun `invoke handles repository error`(): Unit = runBlocking {
        // Given
        val errorMessage = "Error deleting all tasks"
        `when`(taskRepository.deleteAllTasks()).thenReturn(Result.error(errorMessage))

        // When
        val result = deleteAllTasksUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
        verify(taskRepository).deleteAllTasks()
    }
}