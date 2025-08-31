package com.sepanta.controlkit.launchalertkit.service.model

import java.util.Locale

data class ApiCheckUpdateResponse(
    val data: ApiData
)

data class ApiData(
    val id: String?,
    val title: List<LocalizedText>?,
    val description: List<LocalizedText>?,
    val force: Boolean?,
    val icon: String?,
    val link: String?,
    val button_title: List<LocalizedText>?,
    val cancel_button_title: List<LocalizedText>?,
    val version: List<LocalizedText>?,
    val sdk_version: Int?,
    val minimum_version: String?,
    val maximum_version: String?,
    val created_at: String?
)

data class LocalizedText(
    val language: String?,
    val content: String?
)
fun List<LocalizedText>?.getContentBySystemLang(): String? {
    val lang = Locale.getDefault().language
    return this?.firstOrNull { it.language == lang }?.content
        ?: this?.firstOrNull { it.language == "en" }?.content
}

fun ApiCheckUpdateResponse.toDomain(): CheckUpdateResponse {
    val d = this.data
    return CheckUpdateResponse(
        id = d.id,
        version = d.version.getContentBySystemLang(),
        title = d.title.getContentBySystemLang(),
        forceUpdate = d.force,
        description = d.description.getContentBySystemLang(),
        iconUrl = d.icon,
        linkUrl = d.link,
        buttonTitle = d.button_title.getContentBySystemLang(),
        cancelButtonTitle = d.cancel_button_title.getContentBySystemLang(),
        sdkVersion = d.sdk_version?.toString(),
        minimumVersion = d.minimum_version,
        maximumVersion = d.maximum_version,
        created_at = d.created_at
    )
}

