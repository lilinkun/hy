package com.communication.pingyi.ui.home

import androidx.lifecycle.MutableLiveData
import com.communication.lib_core.tools.EVENTBUS_TOKEN_INVALID
import com.communication.lib_http.api.mBaseModel
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.exception.ApiResultCode.INTERNAL_SERVER_ERROR
import com.communication.lib_http.httpdata.home.AppsItem
import com.communication.lib_http.httpdata.home.HomeFlowBean
import com.communication.lib_http.httpdata.home.HomeItem
import com.communication.pingyi.base.BaseViewModel
import com.communication.pingyi.ext.pyLog
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/24  15:09
 * Description：
 */
class AppsViewModel(private val repository: HomeAppsRepository) : BaseViewModel(){

    val appsLiveData = MutableLiveData<HomeItem<MutableList<AppsItem>>>()
    val homeFlow = MutableLiveData<HomeFlowBean>()

    fun getHomeAppsList(){
        launch {
            isLoading.postValue(true)
            val result = repository.getHomeAppsList()

            if (result is NetResult.Success){
                result.data?.let {

                    if(it.size > 0) {
                        appsLiveData.postValue(it[0])
                    }

                }
            }else if (result is NetResult.Error){
                /*if (result.exception.code == INTERNAL_SERVER_ERROR && result.exception.msg.contains("过期")){
                    LiveEventBus.get(EVENTBUS_TOKEN_INVALID).post(true)
                }*/

                mBaseModel?.let {
                    if (mBaseModel?.data == 401){
                        LiveEventBus.get(EVENTBUS_TOKEN_INVALID).post(mBaseModel?.msg)
                    }
                }
            }

            isLoading.postValue(false)


        }
    }

    fun getHomeFlow(){
        launch {

            val result = repository.getHomeFlow()
            if (result is NetResult.Success){
                result.data?.let {

                    homeFlow.postValue(it)

                }
            }else if (result is NetResult.Error && result.exception.msg.contains("过期")){
                if (result.exception.code == INTERNAL_SERVER_ERROR){
//                    LiveEventBus.get(EVENTBUS_TOKEN_INVALID).post(true)
                }
                mBaseModel?.let {
                    if (mBaseModel?.data == 401){
                        LiveEventBus.get(EVENTBUS_TOKEN_INVALID).post(mBaseModel?.msg)
                    }
                }
            }

        }
    }


}