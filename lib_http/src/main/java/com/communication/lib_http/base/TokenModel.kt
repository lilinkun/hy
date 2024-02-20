package com.communication.lib_http.base

data class TokenModel<out T>(val code: Int, val msg: String, val data: T?)