package com.communication.pingyi.activity

import android.os.Bundle
import android.view.View
import com.communication.lib_core.tools.EVENTBUS_GROUP_MEMBER
import com.communication.lib_http.httpdata.message.MemberItem
import com.communication.pingyi.ui.message.message.ChatViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import io.rong.imkit.RongIM
import io.rong.imkit.conversation.RongConversationActivity
import io.rong.imkit.feature.mention.RongMentionManager
import io.rong.imlib.model.UserInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatAcitivity : RongConversationActivity() {


    val mChatViewModel : ChatViewModel by viewModel<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mTitleBar.rightView.visibility = View.VISIBLE

        mTitleBar.setOnRightIconClickListener{
            mChatViewModel.getGroupMember(mTargetId)
        }


        LiveEventBus.get(EVENTBUS_GROUP_MEMBER, MemberItem::class.java).observe(this){
//            val group = Group()

        }

    }

}