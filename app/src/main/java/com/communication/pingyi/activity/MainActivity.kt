package com.communication.pingyi.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.communication.lib_core.tools.EVENTBUS_LOGIN_FAIL
import com.communication.lib_core.tools.EVENTBUS_TOKEN_INVALID
import com.communication.lib_core.tools.EVENTBUS_USER_INFO
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.api.mBaseModel
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.httpdata.login.LoginInfo
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseActivity
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.jpush.PushReceiver
import com.communication.pingyi.tools.ActivityUtil
import com.communication.pingyi.ui.login.account.LoginViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import io.rong.imkit.GlideKitImageEngine
import io.rong.imkit.RongIM
import io.rong.imkit.config.RongConfigCenter
import io.rong.imkit.conversation.extension.RongExtensionManager
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.sight.SightExtensionModule
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    val mLoginViewModel : LoginViewModel by viewModel<LoginViewModel>()
    lateinit var mLoginInfo : LoginInfo;

    private val connectionStatusListener = object : RongIMClient.ConnectionStatusListener {
        //开发者需要根据连接状态码，进行不同业务处理
        override fun onChanged(status: RongIMClient.ConnectionStatusListener.ConnectionStatus?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityUtil.addActivity(this)
//        registerReceiver(reciver, IntentFilter("com.jiguang.demo.message"))

        RongIM.setConnectionStatusListener(connectionStatusListener)

        RongExtensionManager.getInstance().registerExtensionModule(SightExtensionModule())

//        connectIM("z00eycorwV9pQnuF2rVycVuWn8ZzuNtuvrvfqh7f5j8=@yx9p.cn.rongnav.com;yx9p.cn.rongcfg.com")





        RongConfigCenter.featureConfig().kitImageEngine = object : GlideKitImageEngine() {
            override fun loadConversationListPortrait(
                context: Context,
                url: String,
                imageView: ImageView,
                conversation: Conversation?
            ) {
                Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(imageView)
            }
        }

        RongConfigCenter.featureConfig().kitImageEngine = object : GlideKitImageEngine(){
            override fun loadConversationPortrait(
                context: Context,
                url: String,
                imageView: ImageView,
                message: Message?
            ){
                Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(imageView)
            }
        }


        intent?.apply {
            extras?.getString("message")?.let {
                LiveEventBus.get("message",Boolean::class.java).post(true)
            }
        }

        LiveEventBus.get(EVENTBUS_LOGIN_FAIL,Boolean::class.java).observe(this,{
            MMKVTool.saveReconnect(true)
        })


        LiveEventBus.get(EVENTBUS_USER_INFO,PersonInfoBean::class.java).observe(this,{

            it?.imToken?.let { it1 -> connectIM(it1) }
//            connectIM("EZwwh9PYPQZpQnuF2rVycVuWn8ZzuNtuEfJoG4gGsUA=@yx9p.cn.rongnav.com;yx9p.cn.rongcfg.com")
        })


        LiveEventBus.get(
            EVENTBUS_TOKEN_INVALID,
            String::class.java
        ).observe(this,{

                    mBaseModel == null

                    val reconnect = MMKVTool.getReconnect()

                    if (reconnect) {

                        if (!it.contains("解析错误")) {
                            pyToast(it)
                        }
                        val name = MMKVTool.getUsername()
                        MMKVTool.clearAll()
                        val intent = Intent(this, LoginActivity::class.java)
                        MMKVTool.saveUsername(name)
                        intent.putExtra("name", name)
                        startActivity(intent)
                        finish()

                    }else{


                        val currentVersionCode =  Utils.getVersionCode(this).toString()
                        mLoginInfo = LoginInfo(
                            MMKVTool.getBrand(), MMKVTool.getUsername(),MMKVTool.getPassword(),
                            MMKVTool.getDeviceId(), currentVersionCode)
                        mLoginViewModel.createOrGetAuthorization(mLoginInfo)
                    }

        })

    }
//    private val reciver: PushReceiver = PushReceiver()


    fun connectIM(token : String){
        val timeLimit : Int = 1000
        RongIM.connect(token, timeLimit, object : RongIMClient.ConnectCallback(){
            override fun onSuccess(t: String?) {
                /*if (t != null) {
                    pyToast(t+"mainactivity")
                }else{
                    pyToast("连接成功")
                }*/
            }

            override fun onError(e: RongIMClient.ConnectionErrorCode?) {
                pyToast(e!!.name)
            }

            override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {

                if (RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS.equals(code)){

                }else{
                    pyToast("数据库打开失败")
                }

            }

        })
    }



    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(reciver)
    }
}