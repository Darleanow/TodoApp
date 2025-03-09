package com.esgi.todoapp.util

/**
 * Constants used throughout the application.
 */
object Constants {
    // Database
    const val DATABASE_NAME = "task_db"

    // UI dimensions
    const val PADDING_SMALL = 4
    const val PADDING_MEDIUM = 8
    const val PADDING_LARGE = 16
    const val PADDING_EXTRA_LARGE = 24

    // Animation durations
    const val ANIMATION_DURATION = 500
    const val ANIMATION_DURATION_SHORT = 300

    // Task validation constraints
    const val MAX_TITLE_LENGTH = 50
    const val MAX_DESCRIPTION_LENGTH = 500

    // Error messages
    const val ERROR_EMPTY_TITLE = "Le titre de la tâche ne peut pas être vide"
    const val ERROR_TITLE_TOO_LONG = "Le titre ne peut pas dépasser 50 caractères"
    const val ERROR_DESCRIPTION_TOO_LONG = "La description ne peut pas dépasser 500 caractères"
    const val ERROR_INVALID_ID = "ID de tâche invalide"
    const val ERROR_GENERIC = "Une erreur est survenue"
}