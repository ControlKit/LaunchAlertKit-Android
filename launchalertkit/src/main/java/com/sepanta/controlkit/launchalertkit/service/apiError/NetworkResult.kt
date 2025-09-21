package com.sepanta.controlkit.launchalertkit.service.apiError

import com.sepanta.errorhandler.ApiError

sealed class NetworkResult<out T> {
    data class Success<out T>(val value: T?): NetworkResult<T>()
    data class Error( val error: ApiError<*>? = null): NetworkResult<Nothing>()
}