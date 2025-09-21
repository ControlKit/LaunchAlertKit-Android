package com.sepanta.controlkit.launchalertkit.service

import com.sepanta.controlkit.launchalertkit.service.model.ActionResponse
import com.sepanta.controlkit.launchalertkit.service.model.ApiCheckUpdateResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET()
    suspend fun getData(
        @Url url: String,
        @Header("x-app-id") appId: String?,
        @Header("x-version") version: String,
        @Header("x-device-uuid") deviceId: String?,
        @Header("x-sdk-version") sdkVersion: String,
    ): Response<ApiCheckUpdateResponse>

    @FormUrlEncoded
    @POST()
    suspend fun setAction(
        @Url url: String,
        @Header("x-app-id") appId: String?,
        @Header("x-version") version: String,
        @Header("x-sdk-version") sdkVersion: String,
        @Header("x-device-uuid") deviceId: String?,
        @Field("action") action: String,
        ): Response<ActionResponse>
}