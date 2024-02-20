package com.communication.lib_http.httpdata.home

/**
 * Created by LG
 * on 2022/3/30  9:57
 * Description：
 */
data class HomeItem<out T>(
    val name: String,
    val children : T
)