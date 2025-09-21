package com.sepanta.controlkit.launchalertkit.util

import android.content.Context
import androidx.compose.ui.platform.UriHandler
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.sepanta.controlkit.launchalertkit.service.model.LocalizedText
import java.util.Locale
import java.util.UUID

object Utils {


     fun openLink(url: String?, uriHandler: UriHandler) {
        url?.let { uriHandler.openUri(it) }
    }
    fun List<LocalizedText>?.getContentBySystemLang(lang: String? = null): String? {
        if (this.isNullOrEmpty()) return null

        lang?.let { inputLang ->
            this.firstOrNull { it.language == inputLang }?.content?.let { return it }
        }

        val systemLang = Locale.getDefault().language
        this.firstOrNull { it.language == systemLang }?.content?.let { return it }

        return this.firstOrNull { it.language == "en" }?.content
    }

}
object UniqueUserIdProvider {

    private const val PREF_NAME = "secure_prefs"
    private const val KEY_USER_ID = "user_id"

    fun getOrCreateUserId(context: Context): String {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var userId = sharedPreferences.getString(KEY_USER_ID, null)

        if (userId == null) {
            userId = UUID.randomUUID().toString()
            sharedPreferences.edit { putString(KEY_USER_ID, userId) }
        }

        return userId
    }
}