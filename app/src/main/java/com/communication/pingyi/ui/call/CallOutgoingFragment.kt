package com.communication.pingyi.ui.call

import android.app.Activity
import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.communication.lib_core.tools.EVENTBUS_CHAT_CONNECT
import com.communication.lib_core.tools.EVENTBUS_CHAT_OUT_EVENT
import com.communication.pingyi.databinding.CallOutgoingFragmentBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.EventMessage
import com.matt.linphonelibrary.core.SIPManager
import com.matt.linphonelibrary.core.SIPManager.linphoneManager

class CallOutgoingFragment : Fragment(), SurfaceHolder.Callback {
    private lateinit var binding: CallOutgoingFragmentBinding
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null

    val sipManager = SIPManager.getInstance()


    var isVideo = false
    var number = ""
    var currentCall: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = CallOutgoingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        LiveEventBus.get(EVENTBUS_CHAT_OUT_EVENT, EventMessage::class.java).observe(this){
            isVideo = it.message.callIsVideo
            currentCall = it.message.callId
            number = it.message.callName.toString()
            binding.number.text = it.message.callName.toString()
        }

        LiveEventBus.get(EVENTBUS_CHAT_CONNECT,Int::class.java).observe(this){
            if (number != ""){
                if (it == 4){

                    sipManager.answerVideoCall(currentCall!!)
                    var dir = CallOutgoingFragmentDirections.actionCallOutgoingFragmentToCallVideoFragment(
                        number!!
                    )
                    findNavController().navigate(dir)
                }else{

                    sipManager.answerAudioCall(currentCall!!)
//            findNavController().navigateUp()
                    var dir = CallOutgoingFragmentDirections.actionCallOutgoingFragmentToCallVoiceFragment(
                        number!!, currentCall!!
                    )
                    findNavController().navigate(dir)
                }
            }
        }

        if (isVideo) {
            binding.surfaceView.visibility = View.VISIBLE
            surfaceHolder = binding.surfaceView.holder
            surfaceHolder?.setFormat(PixelFormat.TRANSPARENT)
            surfaceHolder?.setKeepScreenOn(true)
            surfaceHolder?.addCallback(this)
        } else {
            binding.surfaceView.visibility = View.INVISIBLE
        }


        binding.hangUp.setOnClickListener {
            sipManager?.hangup(currentCall!!)
            activity?.finish()
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            val cameraInfo = CameraInfo()
            val cameraCount = Camera.getNumberOfCameras()
            for (camIdx in 0 until cameraCount) {
                Camera.getCameraInfo(camIdx, cameraInfo)
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    camera = Camera.open(camIdx)
                    setCameraDisplayOrientation(
                        requireActivity(),
                        camIdx,
                        camera!!
                    )
                }
            }
            if (camera == null) {
                camera = Camera.open()
                setCameraDisplayOrientation(
                    requireActivity(),
                    0,
                    camera!!
                )
            }
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: Exception) {
            if (null != camera) {
                camera?.release()
                camera = null
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        camera?.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (null != camera) {
            camera?.stopPreview()
            camera?.release()
            camera = null
        }
    }

    override fun onPause() {
        if (camera != null) {
            camera?.setPreviewCallback(null)
            camera?.stopPreview()
            camera?.release()
            camera = null
        }
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: Camera) {
        val info = CameraInfo()
        //获取摄像头信息
        Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        //获取摄像头当前的角度
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            //前置摄像头
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else {
            // back-facing  后置摄像头
            result = (info.orientation - degrees + 360) % 360
        }
        camera.setDisplayOrientation(result)
    }


}