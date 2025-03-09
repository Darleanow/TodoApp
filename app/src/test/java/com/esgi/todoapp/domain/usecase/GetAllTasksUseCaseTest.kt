package com.esgi.todoapp.domain.usecase

import com.esgi.todoapp.domain.model.Task
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.domain.util.Result
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*
import org.junit.Assert.*

class GetAllTasksUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        getAllTasksUseCase = GetAllTasksUseCase(taskRepository)
    }

    @Test
    fun `invoke returns all tasks`(): Unit = runBlocking {
        // Given
        val tasks = listOf(
            Task(id = 1, title = "Task 1", description = "Description 1", creationDate = Date(), completed = false),
            Task(id = 2, title = "Task 2", description = "Description 2", creationDate = Date(), completed = true)
        )

        `when`(taskRepository.getAllTasks()).thenReturn(flowOf(Result.success(tasks)))

        // When
        val result = getAllTasksUseCase().first()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(tasks, (result as Result.Success).data)
        verify(taskRepository).getAllTasks()
    }

    @Test
    fun `invoke with empty repository returns empty list`(): Unit = runBlocking {
        // Given
        val emptyList = emptyList<Task>()
        `when`(taskRepository.getAllTasks()).thenReturn(flowOf(Result.success(emptyList)))

        // When
        val result = getAllTasksUseCase().first()

        // Then
        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
        verify(taskRepository).getAllTasks()
    }

    @Test
    fun `invoke handles repository error`(): Unit = runBlocking {
        // Given
        val errorMessage = "Error fetching tasks"
        `when`(taskRepository.getAllTasks()).thenReturn(flowOf(Result.error(errorMessage)))

        // When
        val result = getAllTasksUseCase().first()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
        verify(taskRepository).getAllTasks()
    }
}