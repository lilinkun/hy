package com.communication.lib_http.exception

/**
 * Created by LG
 * on 2022/2/28  18:03
 * Description：
 */
class ResultException(var code : Int?,var msg :String) : Exception(msg)