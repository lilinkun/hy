package com.communication.pingyi.ui.call

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.communication.lib_core.tools.EVENTBUS_CHAT_EVENT
import com.communication.lib_core.tools.EVENTBUS_CHAT_INCOMEEND
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.CallIncomingFragmentBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.EventMessage
import com.matt.linphonelibrary.core.SIPManager

class CallIncomingFragment : BaseFragment<CallIncomingFragmentBinding>() {

    var currentCall: Int? = null
    var number : String? = null
//    private val args: CallIncomingFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.call_incoming_fragment

    override fun initView() {

        LiveEventBus.get(EVENTBUS_CHAT_INCOMEEND, Boolean::class.java).observe(this){
            findNavController().navigateUp()
        }

        LiveEventBus.get(EVENTBUS_CHAT_EVENT, EventMessage::class.java).observe(this){
            binding.number.text = it.message.callName.toString()
            binding.type.text = if (it.message.callIsVideo) "视频通话" else "语音通话"
            currentCall = it.message.callId
            number = it.message.callName.toString()
            if (it.message.callIsVideo) binding.answerAudio.visibility = View.GONE else binding.answerVideo.visibility = View.GONE
        }

//        LiveEventBus.get(
//            EVENTBUS_CHAT_VOICE,
//            EventMessage::class.java
//        ).observe(this){
//            findNavController().navigateUp()
//        }

        var sipManager = SIPManager.getInstance()

        binding.answerAudio.setOnClickListener {
//            findNavController().navigateUp()
            sipManager.answerAudioCall(currentCall!!)
//            findNavController().navigateUp()
            var dir = CallIncomingFragmentDirections.actionCallIncomingFragmentToCallVoiceFragment(
                number!!, currentCall!!
            )
            findNavController().navigate(dir)

        }
        binding.answerVideo.setOnClickListener {
            sipManager.answerVideoCall(currentCall!!)
            var dir = CallIncomingFragmentDirections.actionCallIncomingFragmentToCallVideoFragment(
                number!!
            )
            findNavController().navigate(dir)
        }
        binding.hangUp.setOnClickListener {
            sipManager.hangup(currentCall!!)
        }
    }

    override fun observeViewModels() {
    }

}