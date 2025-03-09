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

class AddTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var addTaskUseCase: AddTaskUseCase

    @Before
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        addTaskUseCase = AddTaskUseCase(taskRepository)
    }

    @Test
    fun `invoke with valid task returns success`() = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "Test Task",
            description = "Test Description",
            creationDate = Date(),
            completed = false
        )

        `when`(taskRepository.insertTask(task)).thenReturn(Result.success(Unit))

        // When
        val result = addTaskUseCase(task)

        // Then
        assertTrue(result is Result.Success)
        verify(taskRepository).insertTask(task)
    }

    @Test
    fun `invoke with blank title returns error`() = runBlocking {
        // Given
        val task = Task(
            id = 1,
            title = "",
            description = "Test Description",
            creationDate = Date(),
            completed = false
        )

        // When
        val result = addTaskUseCase(task)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Le titre de la tâche ne peut pas être vide", (result as Result.Error).message)
        verify(taskRepository, never()).insertTask(task)
    }
}