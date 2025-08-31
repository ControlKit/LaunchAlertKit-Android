package com.sepanta.controlkit.launchalertkit.service

import com.sepanta.controlkit.launchalertkit.service.apiError.NetworkResult
import com.sepanta.controlkit.launchalertkit.service.apiError.handleApi
import com.sepanta.controlkit.launchalertkit.service.model.ApiCheckUpdateResponse


class LaunchAlertApi(private val apiService: ApiService) {

    suspend fun getLaunchAlertData(
        route: String,
        appId: String,
        version: String,
        deviceId: String,
        lastId: String
    ): NetworkResult<ApiCheckUpdateResponse> {
        return handleApi {
            apiService.getData(route, appId, version, deviceId,lastId)
        }
    }
}
