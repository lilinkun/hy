package com.communication.pingyi.ui.update_version

import com.communication.lib_http.api.VersionApi
import com.communication.lib_http.base.BaseRepository
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.version.VersionModel

/**
 * Created by LG
 * on 2022/4/12  16:00
 * Description：
 */
class UpdateVersionRepository(private val api: VersionApi) : BaseRepository() {

    suspend fun getVersion(): NetResult<VersionModel> {
        return callRequest(call = { handleResponse(api.getVersion()) })
    }

}