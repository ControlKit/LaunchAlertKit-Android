package com.sepanta.controlkit.launchalertkit.config

import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewConfig
import retrofit2.http.Header


data class LaunchAlertServiceConfig(
    var viewConfig: LaunchAlertViewConfig = LaunchAlertViewConfig(),
    var version: String= "notSet",
    var appId: String,
    var deviceId: String ?= null,
    var lang:String = "en",
    var timeOut: Long = 5000L,
    var timeRetryThreadSleep: Long = 1000L,
    var maxRetry: Int = 5,
)


