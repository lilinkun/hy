package com.communication.lib_http.httpdata.message

/**
 * 群组名称
 */
data class GroupName(
    val code: Int,
    val errorMessage: Any,
    val groups: List<Group>,
    val reqBody: String,
    val requestId: String
)

data class Group(
    val bindNotifyMsg: Boolean,
    val content: Any,
    val fromUserId: Any,
    val id: String,
    val isIncludeSender: Int,
    val isPersisted: Int,
    val maxMember: Int,
    val members: Any,
    val minute: Any,
    val name: String,
    val objectName: Any,
    val pushContent: Any,
    val pushData: Any,
    val pushExt: String,
    val status: Any
)