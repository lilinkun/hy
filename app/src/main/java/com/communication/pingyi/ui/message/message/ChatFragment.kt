package com.communication.pingyi.ui.message.message

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.communication.lib_core.tools.EVENTBUS_CHAT_USER
import com.communication.lib_core.tools.EVENTBUS_GETMSGLIST
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CONTENT
import com.communication.lib_core.tools.EVENTBUS_SEND_MESSAGE
import com.communication.pingyi.R
import com.communication.pingyi.activity.VoiceAndVideoActivity
import com.communication.pingyi.adapter.MessageListAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentChatBinding
import com.communication.pingyi.model.ConversationMsgList
import com.communication.pingyi.model.ConversationUserInfo
import com.communication.pingyi.model.ConversationUserListBean
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.SIPManager


class ChatFragment : BaseFragment<FragmentChatBinding>() {

    val chatAdapter = MessageListAdapter()

    lateinit var user : ConversationUserInfo

    val sipManager: SIPManager = SIPManager.getInstance()


    override fun getLayoutResId(): Int = R.layout.fragment_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(EVENTBUS_GETMSGLIST, ConversationUserListBean::class.java).observe(this){
            var list : List<ConversationMsgList> = it.data as List<ConversationMsgList>
            chatAdapter.submitList(list)
            chatAdapter.notifyDataSetChanged()
            binding.rcMessageList.scrollToPosition(chatAdapter.getItemCount() - 1);
        }

        LiveEventBus.get(EVENTBUS_CHAT_USER, ConversationUserInfo::class.java).observe(this){
            user = it
        }
    }

    override fun initView() {
        binding.apply {
            rcMessageList.layoutManager = LinearLayoutManager(activity)
            rcMessageList.adapter = chatAdapter

            messageInput.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count > 0){
                        tvSend.visibility = View.VISIBLE
                        addButton.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

            setSendClick{

                if (messageInput.text.toString().trim().length > 0) {
                    LiveEventBus.get(EVENTBUS_MESSAGE_CONTENT, String::class.java)
                        .post(messageInput.text.toString())
                }

            }

            setAddClick {
                if (!functionalButtonsLayout.isShown) {
                    functionalButtonsLayout.visibility = View.VISIBLE
                }else{
                    functionalButtonsLayout.visibility = View.GONE
                }

            }

            setVideoClick {

                val number = user.userName.toString()
                sipManager.callNumber(number, true)

            }
            setVoiceClick {
                val number = user.userName.toString()
                sipManager.callNumber(number, false)

//                var intent = Intent(this, VoiceAndVideoActivity::class.java)
//                var bundle = Bundle()
//                bundle.putInt("type",type)
//                bundle.putInt("callId",event.message.callId)
//                bundle.putString("callName",event.message.callName?: "")
//                bundle.putBoolean("callIsVideo",event.message.callIsVideo)
//                intent.putExtras(bundle)
//                startActivity(intent)

            }
            setPosClick {
                LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).post(3)
            }
            setPhotoClick {
                LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).post(1)
            }
            setCamareClick {
                LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).post(2)
            }

        }


    }

    override fun observeViewModels() {
    }
}