package com.communication.pingyi.ui.message.message

import com.communication.lib_http.api.ChatApi
import com.communication.lib_http.base.BaseRepository
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.message.GroupName
import com.communication.lib_http.httpdata.message.MemberItem

class ChatRepository(private val api : ChatApi) : BaseRepository() {

    suspend fun chatGroupMember(groupId : String) : NetResult<MemberItem>{
        return callRequest ( call = {handleResponse(api.groupMember(groupId))} )
    }


    suspend fun groupList(userId : String) : NetResult<GroupName>{
        return callRequest ( call = {handleResponse(api.groupList(userId))} )
    }

}