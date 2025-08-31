package com.sepanta.controlkit.launchalertkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.ApiService
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.RetrofitClientInstance
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewStyle
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModel
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModelFactory
import com.sepanta.controlkit.launchalertkit.view.viewmodel.state.LaunchAlertState


class LaunchAlertKit(
    private var config: LaunchAlertServiceConfig = LaunchAlertServiceConfig(),
) {

    @Composable
    fun Configure(onDismiss: (() -> Unit)? = null, onState: ((LaunchAlertState) -> Unit)? = null) {
        val context = LocalContext.current
        if (RetrofitClientInstance.getRetrofitInstance(
            ) == null
        ) return
        val api = LaunchAlertApi(
            RetrofitClientInstance.getRetrofitInstance(
                config.timeOut,
                config.maxRetry, config.timeRetryThreadSleep
            )!!.create(ApiService::class.java)
        )
         val localDataSource =LocalDataSource(context)
        val viewModel: LaunchAlertViewModel = viewModel(
            factory = LaunchAlertViewModelFactory(api,localDataSource)
        )
        viewModel.setConfig(config)
        val state = viewModel.state.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.launchAlertEvent.collect {
                onDismiss?.invoke()
            }
        }
        when (state) {

            LaunchAlertState.Initial -> onState?.invoke(LaunchAlertState.Initial)

            LaunchAlertState.NoUpdate -> {
                config.viewConfig.noUpdateState?.invoke()
                onState?.invoke(LaunchAlertState.NoUpdate)
            }
            is LaunchAlertState.Update -> {
                state.data?.let {
                    LaunchAlertViewStyle.checkViewStyle(config.viewConfig.launchAlertViewStyle)
                        .ShowView(config = config.viewConfig, it,viewModel)
                    onState?.invoke(LaunchAlertState.Update(it))
                    viewModel.showDialog()
                }
            }

            is LaunchAlertState.Error -> {
                onState?.invoke(LaunchAlertState.Error(state.data))
            }
        }

    }


}


