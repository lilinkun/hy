package com.communication.pingyi.ui.contact.contact

import androidx.lifecycle.MutableLiveData
import com.communication.lib_http.base.NetResult
import com.communication.lib_http.httpdata.contact.ContactBean
import com.communication.lib_http.httpdata.contact.ContactItem
import com.communication.lib_http.httpdata.contact.SearchUserBean
import com.communication.pingyi.base.BaseViewModel
import com.communication.pingyi.ext.pyLog

/**
 * Created by LG
 * on 2022/3/17  15:53
 * Description：
 */
class ContactViewModel(private val repos : ContactRepository) : BaseViewModel(){

    var org_list = MutableLiveData<MutableList<ContactItem>>()

    val org_user = MutableLiveData<ContactBean>()

    val user_info = MutableLiveData<MutableList<SearchUserBean>>()

    fun getContactList(){

        launch {
            isLoading.postValue(true)
            val result = repos.getContact()
            if (result is NetResult.Success){
                result.data?.let {
                   org_list.postValue(it.trees)
                }
            }else if(result is NetResult.Error){
                result.exception?.let {
                    pyLog(it.code.toString() + it.msg)
                }
            }else{
            }

            isLoading.postValue(false)
        }
    }

    fun getContactUser(id : String){

        launch {
            isLoading.postValue(true)
            val result = repos.getContact(id)
            if (result is NetResult.Success){
                result.data?.let {
                    org_user.postValue(it)
                }
            }else if(result is NetResult.Error){
            }
            isLoading.postValue(false)
        }
    }

    fun searchUser(userName : String){

        launch {
            isLoading.postValue(true)
            val result = repos.searchUser(userName)
            if (result is NetResult.Success){
                result.data?.let {
                    user_info.postValue(it)
                }
            }else if(result is NetResult.Error){
            }
            isLoading.postValue(false)
        }

    }


}

