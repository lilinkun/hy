package com.communication.lib_http.api

import com.communication.lib_http.base.BaseModel
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.changepwd.ChangePwdBean
import retrofit2.http.Body
import retrofit2.http.PUT

/**
 * Created by LG
 * on 2022/4/2  11:32
 * Description：
 */
interface ChangePwdApi {

    @PUT("$SERVER_BASE_URL/system/app/updatePwd")
    suspend fun changePwd(
        @Body changePwdBean : ChangePwdBean
    ) : BaseModel<String>

}