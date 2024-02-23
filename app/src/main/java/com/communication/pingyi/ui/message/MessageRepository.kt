package com.communication.pingyi.ui.message

import com.communication.lib_http.api.MessageApi
import com.communication.lib_http.base.BaseRepository
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.message.AlarmRequestBean
import com.communication.lib_http.httpdata.message.AlarmTodoBean
import com.communication.lib_http.httpdata.message.EventTodoBean

/**
 * Created by LG
 * on 2022/3/29  11:25
 * Description：
 */
class MessageRepository(private val mApi : MessageApi) : BaseRepository() {

    suspend fun getMessageList() : NetResult<MutableList<EventTodoBean>>{
        return callRequest { handleResponse(mApi.getMessageList()) }
    }

    suspend fun getAlarmList(alarmRequestBean: AlarmRequestBean) : NetResult<MutableList<AlarmTodoBean>>{
        return callRequest { handleListResponse(mApi.getAlarmList(alarmRequestBean.facilityCodeList,alarmRequestBean.pageNum,alarmRequestBean.pageSize)) }
    }

    suspend fun readOnlyMessage(id : String) : NetResult<String>{
        return callRequest { handleResponse(mApi.readOnlyMessage(id)) }
    }


    suspend fun readAllMessage(userId : String) : NetResult<String>{
        return callRequest { handleResponse(mApi.readAllMessage(userId)) }
    }

}