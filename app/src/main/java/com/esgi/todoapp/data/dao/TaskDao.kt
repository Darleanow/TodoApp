package com.esgi.todoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.esgi.todoapp.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for Task operations.
 * This interface defines the methods to interact with the tasks table in the database.
 */
@Dao
interface TaskDao {
    /**
     * Retrieves all tasks from the database ordered by creation date in descending order.
     * Returns a Flow to observe database changes.
     *
     * @return Flow emitting a list of TaskEntity objects when data changes
     */
    @Query("SELECT * FROM tasks ORDER BY creationDate DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    /**
     * Retrieves a specific task by its ID.
     *
     * @param id The unique identifier of the task
     * @return The TaskEntity if found, or null if no task exists with the given ID
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?

    /**
     * Inserts a new task into the database.
     * If a task with the same ID already exists, it will be replaced.
     *
     * @param task The TaskEntity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    /**
     * Updates an existing task in the database.
     *
     * @param task The TaskEntity with updated values
     */
    @Update
    suspend fun updateTask(task: TaskEntity)

    /**
     * Deletes a specific task from the database.
     *
     * @param task The TaskEntity to delete
     */
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    /**
     * Deletes all tasks from the database.
     */
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}