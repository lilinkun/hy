package com.communication.pingyi.ui.message.message

import com.communication.lib_core.tools.EVENTBUS_EVENT_BACK
import com.communication.lib_core.tools.EVENTBUS_GROUP_MEMBER
import com.communication.lib_core.tools.EVENTBUS_GROUP_NAME
import com.communication.lib_http.base.NetResult
import com.communication.pingyi.base.BaseViewModel
import com.jeremyliao.liveeventbus.LiveEventBus

class ChatViewModel(private val repository : ChatRepository) : BaseViewModel() {


    fun getGroupMember(groupId : String){
        launch {
            isLoading.postValue(true)
            val result = repository.chatGroupMember(groupId)

            if (result is NetResult.Success){


                val data = result.data
                val code = data?.code
                if (code == 200){
                LiveEventBus.get(EVENTBUS_GROUP_MEMBER).post(data)
                }

            }

            isLoading.postValue(false)
        }
    }

    fun getGroupName(userId : String){
        launch {
            isLoading.postValue(true)
            val result = repository.groupList(userId)

            if (result is NetResult.Success){


                val data = result.data
                val code = data?.code
                if (code == 200){
                    LiveEventBus.get(EVENTBUS_GROUP_NAME).post(data)
                }

            }

            isLoading.postValue(false)
        }
    }


}