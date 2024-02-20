package com.communication.pingyi.ui.me.changepwd

import com.communication.lib_http.api.ChangePwdApi
import com.communication.lib_http.base.BaseRepository
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.changepwd.ChangePwdBean

/**
 * Created by LG
 * on 2022/4/2  11:31
 * Description：
 */
class ChangePwdRepository(private val mApi : ChangePwdApi) : BaseRepository(){


    suspend fun changePwd(changePwd : ChangePwdBean) : NetResult<String>{
        return callRequest { handleResponse(mApi.changePwd(changePwd)) }
    }

}