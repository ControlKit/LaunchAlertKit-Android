package com.sepanta.controlkit.launchalertkit.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


class LaunchAlertViewModel(
    private val api: LaunchAlertApi, private val localDataSource: LocalDataSource

) : ViewModel() {

    private var config: LaunchAlertServiceConfig? = null
    fun setConfig(config: LaunchAlertServiceConfig) {
        this.config = config
        getData()
    }

    private val _mutableState = MutableStateFlow<LaunchAlertState>(LaunchAlertState.Initial)
    val state: StateFlow<LaunchAlertState> = _mutableState.asStateFlow()

    fun clearState() {
        _mutableState.value = LaunchAlertState.Initial
    }

    fun getData() {
        if (state.value != LaunchAlertState.Initial || config == null) return
        viewModelScope.launch {

            val data = api.getLaunchAlertData(
                config!!.route,
                config!!.appId,
                config!!.version,
                config!!.deviceId,
                localDataSource.getLastId() ?: ""
            )




            when (data) {
                is NetworkResult.Success -> {

                    if (data.value != null) {
                        val responce = data.value.toDomain()
                        saveLastId(responce)
                        _mutableState.value = LaunchAlertState.Update(responce)

                    } else {
                        _mutableState.value = LaunchAlertState.NoUpdate

                    }
                }

                is NetworkResult.Error -> {

                    _mutableState.value = LaunchAlertState.Error(data.error)
                }
            }
        }


    }

    private suspend fun saveLastId(responce: CheckUpdateResponse) {

        val lastId = responce.id
        lastId?.let { localDataSource.saveLastId(lastId) }
    }

    private val _openDialog = MutableStateFlow(true)
    val openDialog: StateFlow<Boolean> = _openDialog.asStateFlow()

    fun showDialog() {
        _openDialog.value = true
    }

    fun submitDialog() {
        _openDialog.value = false
        clearState()
    }

    fun dismissDialog() {
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

