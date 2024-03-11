package com.communication.pingyi.ui.message

import androidx.lifecycle.MutableLiveData
import com.communication.lib_core.tools.EVENTBUS_ALARM_BADGE
import com.communication.lib_core.tools.EVENTBUS_EVENT_BADGE
import com.communication.lib_core.tools.EVENTBUS_UNREAD_MESSAGE
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.message.AlarmRequestBean
import com.communication.lib_http.httpdata.message.AlarmTodoBean
import com.communication.lib_http.httpdata.message.EventTodoBean
import com.communication.pingyi.base.BaseViewModel
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/29  11:22
 * Description：
 */
class MessageViewModel(private val repo : MessageRepository) : BaseViewModel() {

    val messageList = MutableLiveData<MutableList<EventTodoBean>>()
    val alarmList = MutableLiveData<MutableList<AlarmTodoBean>>()
    val messageError = MutableLiveData<String>()

    //获取待办事件列表
    fun getMessageList(){
        launch {
            isLoading.postValue(true)
            val result = repo.getMessageList()

            if (result is NetResult.Success){

                result.data?.let {

                    messageList.postValue(result.data)




                    LiveEventBus.get(EVENTBUS_EVENT_BADGE).post(it.count{ it.eventStatus == "untreated" })

                }

            }else if (result is NetResult.Error){
                result.exception.let {
                    if(!it.msg.contains("解析错误")) {
                        messageError.postValue(it.msg)
                    }
                }
            }
            isLoading.postValue(false)


        }
    }

    /**
     * 获取待办告警列表
     */
    fun getAlarmList(alarmRequestBean:AlarmRequestBean){
        launch {

            isLoading.postValue(true)

            val result = repo.getAlarmList(alarmRequestBean)

            if (result is NetResult.Success){

                result.data?.let {

                    alarmList.postValue(it)

                    LiveEventBus.get(EVENTBUS_ALARM_BADGE).post(it.count{ it.alarmStatus == "untreated" })

                }

            }else if (result is NetResult.Error){
                result.exception.let {
                    if(!it.msg.contains("解析错误")) {
//                        messageError.postValue(it.msg)
                    }
                }
            }



            isLoading.postValue(false)
        }


    }

    fun unreadCount(eventCount : Int,alarmCount : Int,msgCount : Int){
        if ((msgCount != -1 && alarmCount != -1) ||  (msgCount != -1 && eventCount != -1 )) {
            var count : Int = 0
            if (eventCount == -1){
                count = alarmCount+msgCount
            }else if(alarmCount == -1){
                count = eventCount+msgCount
            }else{
                count = alarmCount + msgCount + eventCount
            }
            LiveEventBus.get(EVENTBUS_UNREAD_MESSAGE).post(count)
        }
    }

    fun readOnlyMessage(id : String){
        launch {
            isLoading.postValue(true)
            val result = repo.readOnlyMessage(id)

            if (result is NetResult.Success){

                result.data?.let {
                    getMessageList()
                }

            }else if (result is NetResult.Error){
                result.exception.let {
                    messageError.postValue(it.msg)
                }
            }
            isLoading.postValue(false)
        }
    }


    fun readAllMessage(userId : String){
        launch {
            isLoading.postValue(true)
            val result = repo.readAllMessage(userId)

            if (result is NetResult.Success){

                result.data?.let {
                    getMessageList()
                }

            }else if (result is NetResult.Error){
                result.exception.let {
                    messageError.postValue(it.msg)
                }
            }
            isLoading.postValue(false)
        }
    }


}