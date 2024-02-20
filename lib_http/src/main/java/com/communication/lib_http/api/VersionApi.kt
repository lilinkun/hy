package com.communication.lib_http.api

import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.httpdata.version.VersionModel
import retrofit2.http.GET

/**
 * Created by LG
 * on 2022/4/12  16:00
 * Description：
 */
interface VersionApi {

    @GET("$SERVER_BASE_URL/system/app/getAppVersion")
    suspend fun getVersion() : BaseModel<VersionModel>
}