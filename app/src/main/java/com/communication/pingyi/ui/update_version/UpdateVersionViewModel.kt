package com.communication.pingyi.ui.update_version

import com.communication.lib_core.tools.EVENTBUS_CHECK_UPDATE_VERSION
import com.communication.lib_core.tools.EVENTBUS_CHECK_UPDATE_VERSION_BUTTON
import com.communication.lib_http.base.NetResult
import com.communication.pingyi.base.BaseViewModel
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/4/12  16:01
 * Description：
 */
class UpdateVersionViewModel(private val repository: UpdateVersionRepository) : BaseViewModel() {

    fun checkUpdate(isBtnClick: Boolean) {
        launch {
            isLoading.postValue(true)
            val result = repository.getVersion()
            if (result is NetResult.Success) {
                if (isBtnClick) {
                    LiveEventBus.get(EVENTBUS_CHECK_UPDATE_VERSION_BUTTON).post(result.data)
                } else {
                    LiveEventBus.get(EVENTBUS_CHECK_UPDATE_VERSION).post(result.data)
                }
            }
            isLoading.postValue(false)
        }
    }
}