package com.communication.pingyi.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.communication.lib_core.tools.EVENTBUS_CHAT_CONNECT
import com.communication.lib_core.tools.EVENTBUS_CHAT_EVENT
import com.communication.lib_core.tools.EVENTBUS_CHAT_FINISH
import com.communication.lib_core.tools.EVENTBUS_CHAT_OUT_EVENT
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseActivity
import com.communication.pingyi.tools.ActivityUtil
import com.communication.pingyi.ui.call.CallIncomingFragmentDirections
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.EventMessage

class VoiceAndVideoActivity : BaseActivity() {

    var type : Int = 0
    var event : EventMessage? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_and_video)
        ActivityUtil.addActivity(this)
        var bundle = intent.extras

        type = bundle?.getInt("type")!!

        var callId = bundle?.getInt("callId")
        var callName = bundle?.getString("callName")
        var callIsVideo = bundle?.getBoolean("callIsVideo")

        event = EventMessage()
        var message = EventMessage().Message()
        message.callId = callId
        message.callIsVideo = callIsVideo
        message.callName = callName
        event!!.message = message
        event!!.type = type.toString()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_chat) as NavHostFragment

        val navController = navHostFragment.findNavController()
        if (type == 0){
            finish()
            return
        }else if(type == 1 || type == 2){
            val graph = navController.navInflater.inflate(
                if (type == 1) {
                    R.navigation.nav_chat
                } else{
                    R.navigation.nav_out
                }
            )
            navController.graph = graph
        }else{
            LiveEventBus.get(EVENTBUS_CHAT_CONNECT,Int::class.java).post(type)
        }

        LiveEventBus.get(EVENTBUS_CHAT_FINISH,Boolean::class.java).observe(this){
            if (it){
                finish()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (type == 1) {
            LiveEventBus.get(EVENTBUS_CHAT_EVENT, EventMessage::class.java).post(event)
        }else if(type == 2){
            LiveEventBus.get(EVENTBUS_CHAT_OUT_EVENT, EventMessage::class.java).post(event)
        }
    }


}