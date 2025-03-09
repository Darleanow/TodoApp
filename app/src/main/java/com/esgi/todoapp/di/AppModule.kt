package com.esgi.todoapp.di

import android.app.Application
import androidx.room.Room
import com.esgi.todoapp.data.dao.TaskDao
import com.esgi.todoapp.data.database.TaskDatabase
import com.esgi.todoapp.data.repository.TaskRepositoryImpl
import com.esgi.todoapp.domain.repository.TaskRepository
import com.esgi.todoapp.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides dependency injection for the application.
 * This module defines how to create instances of various dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of the Room database.
     *
     * @param app The application context.
     * @return A singleton instance of TaskDatabase.
     */
    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    /**
     * Provides a singleton instance of the TaskDao.
     *
     * @param db The TaskDatabase instance.
     * @return A singleton instance of TaskDao.
     */
    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao
    }

    /**
     * Provides a singleton implementation of the TaskRepository.
     *
     * @param dao The TaskDao instance to be used by the repository.
     * @return A singleton implementation of TaskRepository.
     */
    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(dao)
    }
}