package com.communication.lib_http.api

import com.communication.lib_http.httpdata.token.TokenData
import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.httpdata.home.AppsItem
import com.communication.lib_http.httpdata.home.HomeFlowBean
import com.communication.lib_http.httpdata.home.HomeItem
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by LG
 * on 2022/3/24  15:18
 * Description：
 */
interface HomeApi {

    @GET("$SERVER_BASE_URL/system/app/getAppRouters")
    suspend fun getHomeAppsList() : BaseModel<MutableList<HomeItem<MutableList<AppsItem>>>>

    @GET("$SERVER_BASE_URL/operation/app/appOperationSituation/appNetworkCarTraffic")
    suspend fun getHomeFlow() : BaseModel<HomeFlowBean>

}