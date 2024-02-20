package com.communication.lib_http.api

import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.httpdata.me.PersonInfoBean
import retrofit2.http.DELETE
import retrofit2.http.GET

/**
 * Created by LG
 * on 2022/3/17  16:37
 * Description：
 */
interface MeApi {

    @DELETE("$SERVER_BASE_URL/auth/app/logout")
    suspend fun logout() : BaseModel<String>

    @GET("$SERVER_BASE_URL/system/user/profile")
    suspend fun getinfo() : BaseModel<PersonInfoBean>


}