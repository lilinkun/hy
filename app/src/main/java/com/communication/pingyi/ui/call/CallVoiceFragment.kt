package com.communication.pingyi.ui.call

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.CallVoiceFragmentBinding
import com.matt.linphonelibrary.core.SIPManager

class CallVoiceFragment : BaseFragment<CallVoiceFragmentBinding>() {

    var sipManager = SIPManager.getInstance()
    private val args: CallVoiceFragmentArgs by navArgs()

    var currentCall: Int? = null

    override fun getLayoutResId(): Int = R.layout.call_voice_fragment

    override fun initView() {
        currentCall = args.currentCall
        binding.number.text = args.number
        binding.chronometer.start()

        binding.micro.isSelected = sipManager.micEnabled()
        binding.speaker.isSelected = sipManager.speakerEnabled()

        binding.hangUp.setOnClickListener {
            sipManager.hangup(currentCall!!)
        }
        binding.micro.setOnClickListener {
            val boolean = !sipManager.micEnabled()
            binding.micro.isSelected = boolean
            sipManager.enableMic(boolean)
        }
        binding.speaker.setOnClickListener {
            Log.i("cqc","-------");
            val boolean = !sipManager.speakerEnabled()
            binding.micro.isSelected = boolean
            sipManager.enableSpeaker(boolean)
        }
    }

    override fun observeViewModels() {
    }


}