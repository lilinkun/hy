package com.communication.pingyi.ui.login.account

import com.communication.lib_http.api.LoginApi
import com.communication.lib_http.base.BaseRepository
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.login.LoginInfo
import com.communication.lib_http.httpdata.token.TokenData

/**
 * Created by LG
 * on 2022/3/15  9:02
 * Description：
 */
class LoginRepository(private val api : LoginApi) : BaseRepository(){

    suspend fun login(info : LoginInfo) : NetResult<TokenData>{
        return callRequest ( call = { handleResponse(api.login(info)) })
    }

}