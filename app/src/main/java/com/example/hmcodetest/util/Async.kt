package com.example.hmcodetest.util

sealed class Async<out T> {
    data class Error(val errorMessage: String, val errorType: ErrorType = ErrorType.UNKNOWN) : Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()

    fun onSuccess(action: (T) -> Unit): Async<T> {
        if (this is Success) action(data)
        return this
    }

    fun onError(action: (String) -> Unit): Async<T> {
        if (this is Error) action(errorMessage)
        return this
    }
}

enum class ErrorType {
    /** No internet connection available */
    NO_INTERNET,
    
    /** Request timeout */
    TIMEOUT,
    
    /** Server returned 4xx error (client error) */
    CLIENT_ERROR,
    
    /** Server returned 5xx error (server error) */
    SERVER_ERROR,
    
    /** Network error (connection failed, DNS failure, etc.) */
    NETWORK_ERROR,
    
    /** Data parsing/serialization error */
    PARSING_ERROR,
    
    /** Unknown or unhandled error */
    UNKNOWN
}
