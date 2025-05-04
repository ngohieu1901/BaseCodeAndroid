package com.hieunt.base.data.result

sealed class AppResult<out T: Any> {
    data class Success<out T : Any>(val data: T) : AppResult<T>()
    data class Error(val exception: Exception) : AppResult<Nothing>()
}
