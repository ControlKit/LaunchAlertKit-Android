package com.sepanta.controlkit.launchalertkit.service.model

data class
CheckUpdateResponse(
    val id: String?=null,
    val version: String?=null,
    val title: String?=null,
    val forceUpdate: Boolean?=null,
    val description: String?=null,
    val iconUrl: String?=null,
    val linkUrl: String?=null,
    val buttonTitle: String?=null,
    val cancelButtonTitle: String?=null,
    val sdkVersion: String?=null,
    val minimumVersion: String?=null,
    val maximumVersion: String?=null,
    val created_at: String?=null
)