package com.communication.lib_http.api

import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.httpdata.message.GroupName
import com.communication.lib_http.httpdata.message.MemberItem
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by LG
 * on 2024/3/3  18:08
 * Description：
 */
interface ChatApi {

    @GET("$SERVER_BASE_URL/http/queryGroup")
    suspend fun groupMember(
        @Query("groupId") groupId : String
    ) : BaseModel<MemberItem>

    @GET("$SERVER_BASE_URL/http/queryGroupByUser")
    suspend fun groupList(
        @Query("userId") userId : String
    ) : BaseModel<GroupName>
}