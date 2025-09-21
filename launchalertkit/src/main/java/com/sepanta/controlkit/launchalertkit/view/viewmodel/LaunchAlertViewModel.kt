package com.sepanta.controlkit.launchalertkit.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepanta.controlkit.launchalertkit.BuildConfig
import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.apiError.NetworkResult
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.service.model.toDomain
import com.sepanta.controlkit.launchalertkit.view.viewmodel.state.LaunchAlertState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID


class LaunchAlertViewModel(
    private val api: LaunchAlertApi, private val localDataSource: LocalDataSource,

    ) : ViewModel() {
    private val url = BuildConfig.API_URL

    private var config: LaunchAlertServiceConfig? = null
    fun setConfig(config: LaunchAlertServiceConfig) {
        this.config = config
    }

    private var itemId: String? = null

    private val _mutableState = MutableStateFlow<LaunchAlertState>(LaunchAlertState.Initial)
    val state: StateFlow<LaunchAlertState> = _mutableState.asStateFlow()

    fun clearState() {
        _mutableState.value = LaunchAlertState.Initial
    }

    fun sendAction(action: String) {

        if (itemId == null) return
        viewModelScope.launch {
            val data = api.setAction(
                url + "/${itemId}",
                config!!.appId,
                config!!.version,
                config!!.deviceId ?: "",
                BuildConfig.LIB_VERSION_NAME,
                action,
            )
            when (data) {
                is NetworkResult.Success -> {

                    _mutableState.value = LaunchAlertState.Action(action)
                }

                is NetworkResult.Error -> {
                    _mutableState.value = LaunchAlertState.ActionError(data.error)
                }
            }

        }
    }

    fun getData() {
        if (config == null) return
        viewModelScope.launch {
            val data = api.getLaunchAlertData(
                url,
                config!!.appId,
                config!!.version,
                config!!.deviceId ?: "",
                localDataSource.getLastId() ?: ""
            )
            when (data) {
                is NetworkResult.Success -> {
                    val response = data.value?.toDomain(config?.lang)
                    handleResponse(response)
                }

                is NetworkResult.Error -> {
                    _mutableState.value = LaunchAlertState.ShowViewError(data.error)
                }
            }
        }

    }

    private suspend fun saveLastId(responce: CheckUpdateResponse) {

        val lastId = responce.id
        lastId?.let { localDataSource.saveLastId(lastId) }
    }

    private suspend fun getLastId(): String? {
        return localDataSource.getLastId()
    }

    private suspend fun handleResponse(response: CheckUpdateResponse?) {
        if (response == null || response.id == null) {
            _mutableState.value = LaunchAlertState.NoAlert
            return
        }

        val lastId = getLastId()
        val shouldShowAlert = when {
            lastId == null -> true
            else -> {
                val lastUuid = UUID.fromString(lastId)
                val newUuid = UUID.fromString(response.id)
                newUuid > lastUuid
            }
        }

        if (shouldShowAlert) {
            showAlert(response)
        } else {
            _mutableState.value = LaunchAlertState.NoAlert
        }
    }

    private suspend fun showAlert(response: CheckUpdateResponse) {
        itemId = response.id
        saveLastId(response)
        _mutableState.value = LaunchAlertState.ShowView(response)
        sendAction(Actions.VIEW.value)
    }

    private val _openDialog = MutableStateFlow(true)
    val openDialog: StateFlow<Boolean> = _openDialog.asStateFlow()

    fun showDialog() {

        _openDialog.value = true
    }

    fun submitDialog() {
        sendAction(Actions.ACCEPTED.value)
        _openDialog.value = false
        clearState()
    }

    fun dismissDialog() {
        sendAction(Actions.CANCELED.value)
        _openDialog.value = false
        triggerLaunchAlert()
        clearState()

    }


    private val _launchAlertEvent = Channel<Unit>(Channel.BUFFERED)
    val launchAlertEvent = _launchAlertEvent.receiveAsFlow()

    fun triggerLaunchAlert() {
        viewModelScope.launch {
            _launchAlertEvent.send(Unit)
        }
    }

}

