package com.hara.kaera.core

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error<T>(val error: ErrorType) : ApiResult<T>()
}