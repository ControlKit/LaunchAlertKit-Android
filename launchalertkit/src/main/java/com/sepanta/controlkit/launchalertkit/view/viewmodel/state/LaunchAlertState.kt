package com.sepanta.controlkit.launchalertkit.view.viewmodel.state

import com.sepanta.controlkit.launchalertkit.service.apiError.ApiError
import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse


sealed class LaunchAlertState {
    object Initial : LaunchAlertState()
    object NoUpdate : LaunchAlertState()
    data class Update(val data: CheckUpdateResponse?) : LaunchAlertState()
    data class Error(val data: ApiError?) : LaunchAlertState()


}

