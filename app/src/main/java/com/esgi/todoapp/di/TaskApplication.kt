package com.esgi.todoapp.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for the app, annotated with @HiltAndroidApp.
 * This is the entry point for Hilt dependency injection in the application.
 * The annotation @HiltAndroidApp triggers Hilt's code generation, including
 * a base class for the application that serves as the application-level dependency container.
 */
@HiltAndroidApp
class TaskApplication : Application()