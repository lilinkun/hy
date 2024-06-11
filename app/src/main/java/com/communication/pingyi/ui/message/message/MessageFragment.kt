package com.communication.pingyi.ui.message.message

import android.content.Intent
import android.os.Bundle
import com.communication.lib_core.tools.EVENTBUS_CHATLISTUSER
import com.communication.lib_core.tools.EVENTBUS_CHATLOGIN
import com.communication.lib_core.tools.EVENTBUS_CHAT_MESSAGE
import com.communication.lib_core.tools.EVENTBUS_CONTACT_USER_CLICK
import com.communication.lib_http.base.MMKVTool
import com.communication.pingyi.R
import com.communication.pingyi.activity.ConversationAcitivity
import com.communication.pingyi.activity.MainActivity
import com.communication.pingyi.adapter.UserListAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentMessagelistBinding
import com.communication.pingyi.model.ConversationUserInfo
import com.communication.pingyi.model.ConversationUserListBean
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.ImManager

class MessageFragment : BaseFragment<FragmentMessagelistBinding>(){

    val userListAdapter = UserListAdapter()

    override fun getLayoutResId(): Int  = R.layout.fragment_messagelist

    var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(EVENTBUS_CHATLOGIN,String::class.java).observe(this) {
            if (it.equals("登录成功")){
                if (isLogin) {
                    var imManager: ImManager = ImManager.getInstance()
                    imManager.getUserList()
                    isLogin = !isLogin
                }
            }
        }

        LiveEventBus.get(EVENTBUS_CHATLISTUSER, ConversationUserListBean::class.java).observe(this){
            if (it.code == 200){
                val list : List<ConversationUserInfo> = it.data as List<ConversationUserInfo>

                userListAdapter.submitList(list)

            }
        }

        LiveEventBus.get(EVENTBUS_CONTACT_USER_CLICK,ConversationUserInfo::class.java).observe(this){

                var bundle  = Bundle()
                bundle.putSerializable("user",it)
                var intent = Intent(activity,ConversationAcitivity::class.java)
                intent.putExtras(bundle)
                this.startActivity(intent)
        }

        LiveEventBus.get(EVENTBUS_CHAT_MESSAGE,Boolean::class.java).observe(this){
            if (it){
                var imManager: ImManager = ImManager.getInstance()
                imManager.getUserList()
            }
        }

    }

    override fun initView() {

        binding.apply {

            recyclerViewChatList.adapter = userListAdapter

        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun observeViewModels() {
    }
}