package com.sepanta.controlkit.launchalertkit.service.apiError

import com.sepanta.controlkit.launchalertkit.config.utils.convertErrorBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


fun traceErrorException(throwable: Throwable?): ApiError {

    return when (throwable) {

        is HttpException -> {
            when (throwable.code()) {


                403 -> ApiError(
                    throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.FORBIDDEN
                )
                400 -> ApiError(
                    throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.DATA_ERROR
                )


                405 -> ApiError(
                    throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.METHOD_NOT_ALLOWED
                )

                409 -> ApiError(
                    throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.CONFLICT
                )

                422 -> {
                    return convertErrorBody(
                        throwable.response()!!.errorBody()!!.string(),
                        throwable.code()
                    )

                }

                500 -> {

                    ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.INTERNAL_SERVER_ERROR
                    )
                }

                else -> ApiError(
                    UNKNOWN_ERROR_MESSAGE,
                    0,
                    ApiError.ErrorStatus.UNKNOWN_ERROR
                )
            }
        }

        is SocketTimeoutException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.TIMEOUT)
        }

        is IOException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.NO_CONNECTION)
        }

        else -> ApiError(
            if (throwable?.message != null) throwable.message else UNKNOWN_ERROR_MESSAGE,
            0,
            ApiError.ErrorStatus.UNKNOWN_ERROR
        )
    }
}