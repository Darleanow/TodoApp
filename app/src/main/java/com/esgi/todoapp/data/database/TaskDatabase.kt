package com.esgi.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.esgi.todoapp.data.dao.TaskDao
import com.esgi.todoapp.data.entity.TaskEntity
import com.esgi.todoapp.data.util.DateConverter

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}