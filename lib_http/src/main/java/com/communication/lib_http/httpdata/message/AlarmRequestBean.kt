package com.communication.lib_http.httpdata.message

data class AlarmRequestBean(
    val facilityCodeList : String,
    val pageNum : Int,
    val pageSize : Int
)
