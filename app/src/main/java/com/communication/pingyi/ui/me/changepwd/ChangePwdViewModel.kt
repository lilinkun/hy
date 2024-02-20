package com.communication.pingyi.ui.me.changepwd

import androidx.lifecycle.MutableLiveData
import com.communication.lib_core.tools.EVENTBUS_TOAST_STRING
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.changepwd.ChangePwdBean
import com.communication.pingyi.base.BaseViewModel
import com.communication.pingyi.ext.pyLog
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/4/2  11:12
 * Description：
 */
class ChangePwdViewModel(private val mrepo : ChangePwdRepository) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>()

    fun changePwd( changepwd : ChangePwdBean){

        launch {

            val result = mrepo.changePwd(changepwd)

            if (result is NetResult.Success){
                isSuccess.postValue(true)
            }else if (result is NetResult.Error){
                LiveEventBus.get(EVENTBUS_TOAST_STRING).post(result.exception.message)
            }


        }


    }

}