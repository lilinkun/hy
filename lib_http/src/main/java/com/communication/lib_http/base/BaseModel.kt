package com.communication.lib_http.base

/**
 * Created by LG
 * on 2022/2/28  17:09
 * Description：
 */
data class BaseModel<out T>(val code: Int,val msg : String,val data : T)

data class BaseModelList<out T>(val code: Int,val msg : String,val rows : T)