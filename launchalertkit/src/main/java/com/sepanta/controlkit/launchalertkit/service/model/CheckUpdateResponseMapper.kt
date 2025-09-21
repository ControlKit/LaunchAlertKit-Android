package com.sepanta.controlkit.launchalertkit.service.model

import com.sepanta.controlkit.launchalertkit.util.Utils.getContentBySystemLang

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


fun ApiCheckUpdateResponse.toDomain(lang: String?): CheckUpdateResponse {
    val d = this.data
    return CheckUpdateResponse(
        id = d.id,
        version = d.version.getContentBySystemLang(lang),
        title = d.title.getContentBySystemLang(lang),
        forceUpdate = d.force,
        description = d.description.getContentBySystemLang(lang),
        iconUrl = d.icon,
        linkUrl = d.link,
        buttonTitle = d.button_title.getContentBySystemLang(lang),
        cancelButtonTitle = d.cancel_button_title.getContentBySystemLang(lang),
        sdkVersion = d.sdk_version?.toString(),
        minimumVersion = d.minimum_version,
        maximumVersion = d.maximum_version,
        created_at = d.created_at
    )
}

