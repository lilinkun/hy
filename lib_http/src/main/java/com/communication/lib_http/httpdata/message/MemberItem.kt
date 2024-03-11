package com.communication.lib_http.httpdata.message

data class MemberItem(
    val code: Int,
    val errorMessage: Any,
    val id: Any,
    val members: List<Member>,
    val requestId: String
)

data class Member(
    val id: String
)