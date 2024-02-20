package com.communication.lib_http.httpdata.message

/**
 * Created by LG
 * on 2022/3/28  18:01
 * Description：
 */
data class MessageBean(
    val id : Int,
    val isRead : Int,
    val acceptTime : String,
    val messageContent : String,
    val messageId : Int,
    val messageSource : String,
    val messageTitle : String,
    val messageType : Int,
    val serialNumber : String,
    val userId : String,
)
