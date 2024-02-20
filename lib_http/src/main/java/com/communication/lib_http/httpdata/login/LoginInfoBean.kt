package com.communication.lib_http.httpdata.login

data class LoginInfo(
    val brand : String,
    val username : String,
    val password : String,
    val deviceId : String?,
    val sysVersion : String?,
)