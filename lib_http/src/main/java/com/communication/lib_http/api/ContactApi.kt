package com.communication.lib_http.api

import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.httpdata.contact.ContactBean
import com.communication.lib_http.httpdata.contact.ContactUserBean
import com.communication.lib_http.httpdata.contact.SearchUserBean
import com.communication.lib_http.httpdata.login.LoginInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by LG
 * on 2022/3/17  16:18
 * Description：
 */
interface ContactApi {

    @GET("$SERVER_BASE_URL/system/app/dept/appTreeSelect")
    suspend fun getContact(
    ) : BaseModel<ContactBean>

    @GET("$SERVER_BASE_URL/system/app/dept/appTreeSelect")
    suspend fun getContact(
        @Query("parentId") parentId: String?,
    ) : BaseModel<ContactBean>

    @GET("$SERVER_BASE_URL/system/app/dept/search")
    suspend fun searchUser(
        @Query("userName") userName: String?,
    ) : BaseModel<MutableList<SearchUserBean>>


}