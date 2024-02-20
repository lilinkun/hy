package com.communication.lib_http.base

import com.communication.lib_http.exception.ResultException

/**
 * Created by LG
 * on 2022/2/28  17:17
 * Description：
 */
sealed class NetResult<out T : Any> {

    data class Success<out T : Any>(val data: T?) : NetResult<T>()

    data class Error(val exception: ResultException) : NetResult<Nothing>()

}