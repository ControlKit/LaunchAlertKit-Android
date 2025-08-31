package com.sepanta.controlkit.launchalertkit.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import kotlin.jvm.java

class LaunchAlertViewModelFactory(
    private val api: LaunchAlertApi,
    private val localDataSource: LocalDataSource

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaunchAlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LaunchAlertViewModel(api,localDataSource) as T
        }
        throw kotlin.IllegalArgumentException("Unknown ViewModel class")
    }
}