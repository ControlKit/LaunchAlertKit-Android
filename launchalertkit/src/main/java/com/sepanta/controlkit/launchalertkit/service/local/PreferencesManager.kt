package com.sepanta.controlkit.launchalertkit.service.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LaunchAlert", Context.MODE_PRIVATE)
    private val key="LaunchAlertKey"

    fun saveData( value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getData( ): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}