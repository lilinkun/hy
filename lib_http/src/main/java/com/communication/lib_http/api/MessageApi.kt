package com.communication.lib_http.api

import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.base.BaseModelList
import com.communication.lib_http.httpdata.message.AlarmRequestBean
import com.communication.lib_http.httpdata.message.AlarmTodoBean
import com.communication.lib_http.httpdata.message.EventTodoBean
import com.communication.lib_http.httpdata.message.MessageBean
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by LG
 * on 2022/3/29  14:06
 * Description：
 */
interface MessageApi {

    @GET("$SERVER_BASE_URL/event/event/app/eventTodo")
    suspend fun getMessageList() : BaseModel<MutableList<EventTodoBean>>



    @GET("$SERVER_BASE_URL/operation/app/appMessage/getInfoById")
    suspend fun readOnlyMessage(
        @Query("id") id : String
    ) : BaseModel<String>

    @GET("$SERVER_BASE_URL/operation/app/appMessage/getInfoByUserId")
    suspend fun readAllMessage(
        @Query("userId") userId : String
    ) : BaseModel<String>


    @GET("$SERVER_BASE_URL/serviceArea/appServiceAlarm/getAppServiceAlarmInfoList")
    suspend fun getAlarmList(@Query("facilityCodeList")facilityCodeList : List<String>,@Query("pageNum") pageNum : Int,@Query("pageSize") pageSize : Int ) : BaseModelList<MutableList<AlarmTodoBean>>
}