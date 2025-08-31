package com.sepanta.controlkit.launchalertkit.view.config

import androidx.compose.runtime.Composable
import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModel

interface LaunchAlertViewContract {
    @Composable
    fun ShowView(config: LaunchAlertViewConfig, response: CheckUpdateResponse,viewModel: LaunchAlertViewModel)
}