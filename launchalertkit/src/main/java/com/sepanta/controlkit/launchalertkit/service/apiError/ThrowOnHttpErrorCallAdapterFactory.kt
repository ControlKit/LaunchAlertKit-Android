package com.sepanta.controlkit.launchalertkit.service.apiError

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ThrowOnHttpErrorCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType)
        val responseType = getParameterUpperBound(0, returnType)
        return ThrowOnHttpErrorCallAdapter<Any>(responseType)
    }
}

class ThrowOnHttpErrorCallAdapter<R>(
    private val responseType: Type
) : CallAdapter<R, Call<R>> {
    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Call<R> {
        return object : Call<R> by call {
            override fun enqueue(callback: Callback<R>) {
                call.enqueue(object : Callback<R> {
                    override fun onResponse(call: Call<R>, response: Response<R>) {
                        if (response.isSuccessful) {
                            callback.onResponse(call, response)
                        } else {
                            callback.onFailure(call, HttpException(response))
                        }
                    }

                    override fun onFailure(call: Call<R>, t: Throwable) {
                        callback.onFailure(call, t)
                    }
                })
            }

            override fun execute(): Response<R> {
                val response = call.execute()
                if (response.isSuccessful) {
                    return response
                } else {
                    throw HttpException(response)
                }
            }
        }
    }
}
