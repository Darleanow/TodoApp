package com.esgi.todoapp.presentation.viewmodel

/**
 * Base class for UI states in the application.
 * Provides common state handling for loading, success, and error states.
 * This class is designed to be extended by specific UI state classes.
 */
open class BaseUiState {
    /**
     * Indicates whether data is currently being loaded.
     */
    var isLoading: Boolean = false
        protected set

    /**
     * Contains an error message if an error has occurred, or null if there's no error.
     */
    var errorMessage: String? = null
        protected set

    /**
     * Indicates whether the last operation was successful.
     */
    var isSuccess: Boolean = false
        protected set

    /**
     * Sets the loading state.
     *
     * @param loading The new loading state
     * @return This BaseUiState instance for method chaining
     */
    fun setLoading(loading: Boolean): BaseUiState {
        this.isLoading = loading
        return this
    }

    /**
     * Sets an error message.
     *
     * @param message The error message, or null to clear the error
     * @return This BaseUiState instance for method chaining
     */
    fun setError(message: String?): BaseUiState {
        this.errorMessage = message
        return this
    }

    /**
     * Sets the success state.
     *
     * @param success The new success state
     * @return This BaseUiState instance for method chaining
     */
    fun setSuccess(success: Boolean): BaseUiState {
        this.isSuccess = success
        return this
    }

    /**
     * Clears any error message.
     */
    fun clearError() {
        this.errorMessage = null
    }

    /**
     * Resets the success state to false.
     */
    fun clearSuccess() {
        this.isSuccess = false
    }
}