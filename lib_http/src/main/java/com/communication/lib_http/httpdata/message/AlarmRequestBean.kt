package com.communication.lib_http.httpdata.message

data class AlarmRequestBean(
    val facilityCodeList : List<String>,
    val pageNum : Int,
    val pageSize : Int
)
