package com.sepanta.controlkit.launchalertkit.service.apiError

import com.sepanta.errorhandler.traceErrorException
import retrofit2.Response

suspend fun <T : Any> handleApi(execute: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = execute()
        NetworkResult.Success(response.body())
    } catch (e: Exception) {
        NetworkResult.Error(traceErrorException(e))
    }
}
