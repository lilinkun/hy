package com.communication.pingyi.ui.contact.contact

import com.communication.lib_http.api.ContactApi
import com.communication.lib_http.base.BaseRepository
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.contact.ContactBean
import com.communication.lib_http.httpdata.contact.ContactUserBean
import com.communication.lib_http.httpdata.contact.SearchUserBean

/**
 * Created by LG
 * on 2022/3/17  16:07
 * Description：
 */
class ContactRepository(private val contactApi : ContactApi) : BaseRepository() {


    suspend fun getContact() : NetResult<ContactBean> {
        return callRequest { handleResponse(contactApi.getContact()) }
    }

    suspend fun getContact(id : String?) : NetResult<ContactBean> {
        return callRequest { handleResponse(contactApi.getContact(id)) }
    }

    suspend fun searchUser(userName : String) : NetResult<MutableList<SearchUserBean>> {
        return callRequest { handleResponse(contactApi.searchUser(userName)) }
    }

}