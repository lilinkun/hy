package com.communication.lib_http.api

import com.communication.lib_http.httpdata.token.TokenData
import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.base.TokenModel
import com.communication.lib_http.httpdata.login.LoginInfo
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by LG
 * on 2022/2/28  18:08
 * Description：
 */
interface LoginApi {

    @POST("$SERVER_BASE_URL/auth/app/login")
    suspend fun login(
        @Body info : LoginInfo
    ) : BaseModel<TokenData>

}