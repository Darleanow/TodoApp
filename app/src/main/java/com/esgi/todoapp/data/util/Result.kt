package com.esgi.todoapp.domain.util

import com.esgi.todoapp.util.Constants.ERROR_GENERIC

/**
 * A generic class that holds a value or an error message.
 * Used to handle success and error cases in a type-safe way.
 *
 * @param T The type of the value to be wrapped in case of success.
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data.
     *
     * @property data The value returned by the operation.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation with an error message.
     *
     * @property exception The exception that caused the failure.
     * @property message A human-readable error message.
     */
    data class Error(val exception: Exception, val message: String = exception.localizedMessage ?: ERROR_GENERIC) : Result<Nothing>()

    /**
     * Represents an operation in progress.
     */
    data object Loading : Result<Nothing>()

    companion object {
        /**
         * Creates a successful result with the given data.
         *
         * @param data The value to wrap in a Success result.
         * @return A Success result containing the data.
         */
        fun <T> success(data: T): Result<T> = Success(data)

        /**
         * Creates an error result with the given exception.
         *
         * @param exception The exception that caused the error.
         * @return An Error result containing the exception.
         */
        fun error(exception: Exception): Result<Nothing> = Error(exception)

        /**
         * Creates an error result with the given message.
         *
         * @param message A human-readable error message.
         * @return An Error result with the message.
         */
        fun error(message: String): Result<Nothing> = Error(Exception(message), message)

        /**
         * Creates a loading result.
         *
         * @return A Loading result.
         */
        fun loading(): Result<Nothing> = Loading
    }
}