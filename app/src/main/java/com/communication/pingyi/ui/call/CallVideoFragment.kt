package com.communication.pingyi.ui.call

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.CallVideoFragmentBinding
import com.matt.linphonelibrary.core.SIPManager

class CallVideoFragment : BaseFragment<CallVideoFragmentBinding>() {
    var sipManager = SIPManager.getInstance()

    val args : CallVideoFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.call_video_fragment

    override fun initView() {
        sipManager.setVideoWindowId(
            binding.textureView,
            binding.videoPreview
        )
        sipManager.setVideoZoom(
            requireContext(),
            binding.textureView
        )


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
            val boolean = !sipManager.speakerEnabled()
            binding.micro.isSelected = boolean
            sipManager.enableSpeaker(boolean)
        }

        // 开启/关闭摄像头按钮的点击事件
        binding.cameraToggle.setOnClickListener {
            val enabled = !sipManager.cameraEnabled(currentCall!!)
            sipManager.enableCamera(currentCall!!, enabled)
            if (enabled) {
                binding.cameraToggle.text = "关闭摄像头"
            } else {
                binding.cameraToggle.text = "开启摄像头"
            }
        }

        // 切换摄像头按钮的点击事件
        binding.cameraSwitch.setOnClickListener {
            sipManager.switchCamera()
        }

        //切换视频源
        binding.switchVideoSource.setOnClickListener{
            val confId = "temp12345"
            val number = "1998"
            val otherNumbers = "1002"
            sipManager.switchVideoSource(confId,number,otherNumbers)
        }

        //查询终端列表
        binding.queryTerminal.setOnClickListener{
            val confId = "temp12345"
            sipManager.queryTerminal(confId)
        }
    }

    override fun observeViewModels() {
    }

    companion object {
        var number = ""
        var currentCall: Int? = null

        fun newInstance(string: String, callId: Int): Fragment {
            number = string
            currentCall = callId
            return CallVideoFragment()
        }
    }

}