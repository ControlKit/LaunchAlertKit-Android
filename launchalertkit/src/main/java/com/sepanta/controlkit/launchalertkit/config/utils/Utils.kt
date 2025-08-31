package com.sepanta.controlkit.launchalertkit.config.utils

import androidx.compose.ui.platform.UriHandler
import com.google.gson.Gson
import com.sepanta.controlkit.launchalertkit.service.apiError.ApiError
import com.sepanta.controlkit.launchalertkit.service.apiError.ApiErrorEntity
import org.json.JSONException
import java.io.IOException


fun convertErrorBody(error: String?,code:Int?): ApiError {
    var message = String()
     try {
        val gson = Gson()
        val jsonObject = gson.fromJson(
            error,
            ApiErrorEntity.Main::class.java
        )
        message = jsonObject.message
    } catch (ex: IOException) {
        ex.printStackTrace()
    } catch (ex: JSONException) {
        ex.printStackTrace()
    }
    return ApiError(
        message,
        code,
        ApiError.ErrorStatus.DATA_ERROR,
    )

}
fun openLink(url: String?, uriHandler: UriHandler) {
    url?.let { uriHandler.openUri(it) }
}