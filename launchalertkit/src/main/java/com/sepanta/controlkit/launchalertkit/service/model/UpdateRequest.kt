package com.forceupdatekit.service.model

data class UpdateRequest(  var appId: String? = null,
 var version: String ,
 var route: String,
 var os: String = "Android",
 var language: String = "en")