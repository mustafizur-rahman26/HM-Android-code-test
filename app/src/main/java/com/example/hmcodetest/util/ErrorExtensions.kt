package com.example.hmcodetest.util

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toErrorMessage(): String = when (this) {
    // No internet connection
    is UnknownHostException -> "No internet connection. Please check your network."

    // Request timeout
    is SocketTimeoutException -> "Request timed out. Please try again."

    // HTTP errors from server
    is HttpException -> {
        when (val code = this.code()) {
            in 400..499 -> getClientErrorMessage(code)
            in 500..599 -> "Server error. Please try again later."
            else -> "HTTP error: ${this.message()}"
        }
    }

    // Network errors (connection failures, etc.)
    is IOException -> "Network error. Please check your connection."

    // Serialization/parsing errors
    is SerializationException -> "Failed to process data. Please try again."

    // Unknown errors
    else -> this.message?.takeIf { it.isNotEmpty() }
        ?: "An unexpected error occurred."
}

fun Throwable.toErrorType(): ErrorType = when (this) {
    is UnknownHostException -> ErrorType.NO_INTERNET
    is SocketTimeoutException -> ErrorType.TIMEOUT
    is HttpException -> {
        val code = this.code()
        when (code) {
            in 400..499 -> ErrorType.CLIENT_ERROR
            in 500..599 -> ErrorType.SERVER_ERROR
            else -> ErrorType.UNKNOWN
        }
    }
    is IOException -> ErrorType.NETWORK_ERROR
    is SerializationException -> ErrorType.PARSING_ERROR
    else -> ErrorType.UNKNOWN
}

private fun getClientErrorMessage(code: Int): String = when (code) {
    400 -> "Invalid request. Please try again."
    401 -> "Authentication failed. Please sign in again."
    403 -> "Access denied."
    404 -> "Resource not found."
    408 -> "Request timed out. Please try again."
    429 -> "Too many requests. Please slow down."
    else -> "Request error. Please try again."
}
