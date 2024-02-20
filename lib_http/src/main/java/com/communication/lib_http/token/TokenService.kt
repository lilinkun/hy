package com.communication.lib_http.token

import com.communication.lib_http.httpdata.token.TokenData
import com.communication.lib_http.base.TokenModel
import com.communication.lib_http.httpdata.login.LoginInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST("api-user/users/login")
    fun getToken(
        @Body info: LoginInfo
    ): Call<TokenModel<TokenData>>
}