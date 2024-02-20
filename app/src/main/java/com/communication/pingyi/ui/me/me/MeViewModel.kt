package com.communication.pingyi.ui.me.me

import androidx.lifecycle.MutableLiveData
import com.communication.lib_core.tools.EVENTBUS_LOGOUT_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_TOAST_STRING
import com.communication.lib_core.tools.EVENTBUS_TOKEN_INVALID
import com.communication.lib_core.tools.EVENTBUS_USER_INFO
import com.communication.lib_http.api.mBaseModel
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.pingyi.base.BaseViewModel
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/17  11:36
 * Description：
 */
class MeViewModel(private val repo : MeRepository) : BaseViewModel(){

    val personInfo = MutableLiveData<PersonInfoBean>()

    fun logout(){
        launch {

            isLoading.postValue(true)
            val result = repo.logout()
            if (result is NetResult.Success){
                LiveEventBus.get(EVENTBUS_LOGOUT_SUCCESS).post(true)
            }else if (result is NetResult.Error){
                LiveEventBus.get(EVENTBUS_TOAST_STRING).post(result.exception.message)
            }

            isLoading.postValue(false)

            mBaseModel?.let {
                if (mBaseModel?.data == 401){
                    LiveEventBus.get(EVENTBUS_TOKEN_INVALID).post(mBaseModel?.msg)
                }
            }
        }
    }

    fun getInfo(){
        launch {
            isLoading.postValue(true)
            val result = repo.getInfo()
            if (result is NetResult.Success){
                personInfo.postValue(result.data)
                LiveEventBus.get(EVENTBUS_USER_INFO).post(result.data)
            }else if (result is NetResult.Error){
                LiveEventBus.get(EVENTBUS_TOAST_STRING).post(result.exception.message)

            }
            isLoading.postValue(false)

            mBaseModel?.let {
                if (mBaseModel?.code == 500){
                    LiveEventBus.get(EVENTBUS_TOKEN_INVALID).post(mBaseModel?.msg)
                }
            }
        }



    }

}