package com.sepanta.controlkit.launchalertkit.view.viewmodel.state

import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse
import com.sepanta.errorhandler.ApiError


sealed class LaunchAlertState {
    object Initial : LaunchAlertState()
    object NoAlert : LaunchAlertState()


    data class Action(val data: String) : LaunchAlertState()
    data class ActionError(val data: ApiError<*>?) : LaunchAlertState()

    data class ShowView(val data: CheckUpdateResponse?) : LaunchAlertState()
    data class ShowViewError(val data: ApiError<*>?) : LaunchAlertState()
}

