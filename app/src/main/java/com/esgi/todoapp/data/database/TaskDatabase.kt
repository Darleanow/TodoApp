package com.esgi.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.esgi.todoapp.data.dao.TaskDao
import com.esgi.todoapp.data.entity.TaskEntity

/**
 * Room Database class for the application.
 * This class serves as the main access point for the underlying SQLite database.
 * It defines all entities that belong to this database and provides DAOs for accessing the data.
 */
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    /**
     * Provides access to the TaskDao for performing database operations on tasks.
     */
    abstract val taskDao: TaskDao
}