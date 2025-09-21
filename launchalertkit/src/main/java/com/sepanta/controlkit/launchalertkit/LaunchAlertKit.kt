package com.sepanta.controlkit.launchalertkit

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.ApiService
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.RetrofitClientInstance
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.util.UniqueUserIdProvider
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewStyle
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModel
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModelFactory
import com.sepanta.controlkit.launchalertkit.view.viewmodel.state.LaunchAlertState


class LaunchAlertKit(
    private var config: LaunchAlertServiceConfig ,
    context: Context? = null,

    ) {

    private var _viewModel: LaunchAlertViewModel? = null
    val viewModel: LaunchAlertViewModel
        get() = _viewModel ?: throw kotlin.IllegalStateException("ViewModel not initialized yet")

    init {

        context?.let { setupViewModel(it) }
    }

    private fun setupViewModel(context: Context) {
        val retrofit = RetrofitClientInstance.getRetrofitInstance(
            config.timeOut,
            config.maxRetry,
            config.timeRetryThreadSleep
        ) ?: return

        val localDataSource = LocalDataSource(context)
        val api = LaunchAlertApi(retrofit.create(ApiService::class.java))
        _viewModel = LaunchAlertViewModelFactory(
            api,
            localDataSource
        ).create(LaunchAlertViewModel::class.java)

        if (config.deviceId == null) {
            config.deviceId = UniqueUserIdProvider.getOrCreateUserId(context)
            _viewModel?.setConfig(config)

        } else {
            _viewModel?.setConfig(config)

        }
    }


    @Composable
    internal fun ConfigureComposable(
        onDismiss: (() -> Unit)? = null,
        onState: ((LaunchAlertState) -> Unit)? = null,
    ) {
        if (_viewModel == null) return
        val state = _viewModel?.state?.collectAsState()?.value
        val checkUpdateResponse = remember { mutableStateOf<CheckUpdateResponse?>(null) }

        LaunchedEffect(Unit) {
            _viewModel?.launchAlertEvent?.collect {
                onDismiss?.invoke()
            }
        }
        InitView(checkUpdateResponse)

        when (state) {

            LaunchAlertState.Initial -> onState?.invoke(LaunchAlertState.Initial)

            LaunchAlertState.NoAlert -> {
                config.viewConfig.noUpdateState?.invoke()
                onState?.invoke(LaunchAlertState.NoAlert)
            }

            is LaunchAlertState.Action -> {
                onState?.invoke(LaunchAlertState.Action(data = state.data))
            }

            is LaunchAlertState.ActionError -> {
                onState?.invoke(LaunchAlertState.ActionError(state.data))
            }

            is LaunchAlertState.ShowView -> {
                state.data?.let {
                    checkUpdateResponse.value = it
                    onState?.invoke(LaunchAlertState.ShowView(it))
                    _viewModel?.showDialog()
                }

            }

            is LaunchAlertState.ShowViewError -> {
                onState?.invoke(LaunchAlertState.ShowViewError(state.data))


            }

            else -> Unit
        }
    }

    fun showView() {
        _viewModel?.getData()
    }

    @Composable
    private fun InitView(checkUpdateResponse: MutableState<CheckUpdateResponse?>) {
        checkUpdateResponse.value?.let { data ->
            LaunchAlertViewStyle.checkViewStyle(config.viewConfig.launchAlertViewStyle)
                .ShowView(config = config.viewConfig, data, viewModel)

        }
    }
}

@Composable
fun launchAlertKitHost(
    config: LaunchAlertServiceConfig,
    onDismiss: (() -> Unit)? = null,
    onState: ((LaunchAlertState) -> Unit)? = null,
): LaunchAlertKit {
    val context = LocalContext.current

    val kit = remember { LaunchAlertKit(config, context = context) }
    kit.ConfigureComposable(onDismiss = onDismiss, onState = onState)
    return kit
}





