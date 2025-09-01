package com.sepanta.controlkit.launchalertkit.service.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("user_prefs")

class LocalDataSource(val context: Context) {

    private val LAST_ID = stringPreferencesKey("last_id")

    suspend fun saveLastId(token: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_ID] = token
        }
    }

    suspend fun getLastId(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[LAST_ID]
    }

    suspend fun clearLastId() {
        context.dataStore.edit { prefs ->
            prefs.remove(LAST_ID)
        }
    }
}
