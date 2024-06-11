package com.communication.pingyi.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.communication.lib_core.HyAppDialog
import com.communication.lib_core.tools.Constance.Companion.CHAT_ACCOUNT
import com.communication.lib_core.tools.EVENTBUS_CHATLISTUSER
import com.communication.lib_core.tools.EVENTBUS_CHATLOGIN
import com.communication.lib_core.tools.EVENTBUS_CHAT_CONNECT
import com.communication.lib_core.tools.EVENTBUS_CHAT_FINISH
import com.communication.lib_core.tools.EVENTBUS_CHAT_MESSAGE
import com.communication.lib_core.tools.EVENTBUS_EVENT_BACK
import com.communication.lib_core.tools.EVENTBUS_GETMSGLIST
import com.communication.lib_core.tools.EVENTBUS_GROUP_MEMBER
import com.communication.lib_core.tools.EVENTBUS_GROUP_NAME
import com.communication.lib_core.tools.EVENTBUS_LOGIN_FAIL
import com.communication.lib_core.tools.EVENTBUS_TOKEN_INVALID
import com.communication.lib_core.tools.EVENTBUS_USER_INFO
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.api.CHAT_IP
import com.communication.lib_http.api.CHAT_PORT
import com.communication.lib_http.api.mBaseModel
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.httpdata.login.LoginInfo
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.lib_http.httpdata.message.GroupName
import com.communication.lib_http.httpdata.message.MemberItem
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseActivity
import com.communication.pingyi.base.REQUEST_WRITE_EXTERNAL_STORAGE
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.model.ConversationMsgList
import com.communication.pingyi.model.ConversationUserInfo
import com.communication.pingyi.model.ConversationUserListBean
import com.communication.pingyi.tools.ActivityUtil
import com.communication.pingyi.ui.login.account.LoginViewModel
import com.communication.pingyi.ui.main.MainFragment
import com.communication.pingyi.ui.message.message.ChatViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.EventMessage
import com.matt.linphonelibrary.core.ImInterfaceMsg
import com.matt.linphonelibrary.core.ImManager
import com.matt.linphonelibrary.core.RecieveMsg
import com.matt.linphonelibrary.core.SIPManager
import com.matt.linphonelibrary.core.SipInterfaceMsg
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Type


class MainActivity : BaseActivity() {

    lateinit var sipManager: SIPManager
    lateinit var imManager: ImManager

    val mLoginViewModel : LoginViewModel by viewModel<LoginViewModel>()
    val mChatViewModel : ChatViewModel by viewModel<ChatViewModel>()
    lateinit var mLoginInfo : LoginInfo;

    val Tag = "hengyong-mainactivity"

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun receiveEvent(event: EventMessage){
        Log.i("SipReceiver", "receiveEvent: " + JSON.toJSONString(event));
        if(event.type.equals("register")){
            if(event.message.code.equals("0")) Log.i(Tag, "登录成功 ")
            if(event.message.code.equals("1")) Log.i(Tag, "未登录 ");
            if(event.message.code.equals("2")) Log.i(Tag, "登录失败 ");
            if(event.message.code.equals("3")) Log.i(Tag, "未登录 ");
        }
        if(event.type.equals("call")){
            var type : Int = 0
            if("IncomingReceived".equals(event.message.callState)){
//                showCall(1, event.message.callId, event.message.callName ?: "", event.message.callIsVideo)
//                LiveEventBus.get(
//                    EVENTBUS_CHAT_INCOME,
//                    EventMessage::class.java
//                ).post(event)

                type = 1


            }else if("OutgoingInit".equals(event.message.callState)){
//                showCall(2, event.message.callId, event.message.callName ?: "", event.message.callIsVideo)
                type = 2
            }else if("Connected".equals(event.message.callState)){
                if(event.message.callIsVideo){
//                    showCall(4, event.message.callId, event.message.callName ?: "", event.message.callIsVideo)
//                    LiveEventBus.get(
//                        EVENTBUS_CHAT_VIDEO,
//                        EventMessage::class.java
//                    ).post(event)
                    type = 4
                }else{
//                    showCall(3, event.message.callId, event.message.callName ?: "", event.message.callIsVideo)
//                    LiveEventBus.get(
//                        EVENTBUS_CHAT_VOICE,
//                        EventMessage::class.java
//                    ).post(event)
                    type = 3
                }
            }else{
//                showCall(0, event.message.callId, event.message.callName ?: "", event.message.callIsVideo)
//                LiveEventBus.get(
//                    EVENTBUS_CHAT_INCOMEEND,
//                    Boolean::class.java
//                ).post(true)
                type = 0
            }

            if (type == 0){
                LiveEventBus.get(EVENTBUS_CHAT_FINISH,Boolean::class.java).post(true)
            }else if(type == 3 || type == 4){
                LiveEventBus.get(EVENTBUS_CHAT_CONNECT,Int::class.java).post(type)
            }else{
                var intent = Intent(this, VoiceAndVideoActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("type", type)
                bundle.putInt("callId", event.message.callId)
                bundle.putString("callName", event.message.callName ?: "")
                bundle.putBoolean("callIsVideo", event.message.callIsVideo)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sipInterfaceMsg(sipInterfaceMsg: SipInterfaceMsg){
        Log.i("sipInterfaceMsg", "sipInterfaceMsg: " +JSON.toJSONString(sipInterfaceMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun imInterfaceMsg(imInterfaceMsg: ImInterfaceMsg){
        Log.i("imInterfaceMsg", "imInterfaceMsg: " +JSON.toJSONString(imInterfaceMsg));
        if (imInterfaceMsg.type.equals("login") && imInterfaceMsg.msg.contains("登录连接成功")){
            LiveEventBus.get(EVENTBUS_CHATLOGIN,String::class.java).post("登录成功");
        }else if(imInterfaceMsg.type.equals("getUserList")){
            val type = object : TypeToken<ConversationUserListBean<ConversationUserInfo>>() {}
            getMsg(imInterfaceMsg.msg,EVENTBUS_CHATLISTUSER,type)

        }else if (imInterfaceMsg.type.equals("getMsgList")){

            val type = object : TypeToken<ConversationUserListBean<ConversationMsgList>>() {}

            getMsg(imInterfaceMsg.msg,EVENTBUS_GETMSGLIST,type)
        }
    }

    fun <T> getMsg(msg : String,eventKey: String,typeToken: TypeToken<T>){
        // 创建 Gson 实例
        val gson = Gson()
        // 定义泛型类型
        val type: Type = typeToken.type
        // 将 JSON 字符串转换为对象
        val list: ConversationUserListBean<ConversationUserInfo>  = gson.fromJson(msg,type)
        LiveEventBus.get(eventKey,ConversationUserListBean::class.java).post(list)
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun recieveMsg(recieveMsg: RecieveMsg){
        Log.i("recieveMsg", "recieveMsg: " +JSON.toJSONString(recieveMsg));
        val jsonObject = JSONObject.parseObject(recieveMsg.msg)
        if (jsonObject.getInteger("code") == 2){
            LiveEventBus.get(EVENTBUS_CHAT_MESSAGE,Boolean::class.java).post(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityUtil.addActivity(this)
//        registerReceiver(reciver, IntentFilter("com.jiguang.demo.message"))

        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

            //通过的权限
            val grantedList = it.filterValues { it }.mapNotNull { it.key }
            //未通过的权限
            val deniedList = (it - grantedList).map { it.key }

            //拒绝并且点了“不再询问”权限
            val alwaysDeniedList = deniedList.filterNot {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }

            if (deniedList.isNotEmpty()) {
                finish()
            } else {
                sipManager=SIPManager.getInstance();
                imManager=ImManager.getInstance();
                EventBus.getDefault().register(this)
                Log.i("chenqc", "Init: ")
                sipManager.init(this);
                sipManager.initializeImManager(imManager)
            }

        }.launch(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
                arrayOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            }else {
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                )
            }
        )




        intent?.apply {
            extras?.getString("message")?.let {
                LiveEventBus.get("message",Boolean::class.java).post(true)
            }
        }

        LiveEventBus.get(EVENTBUS_LOGIN_FAIL,Boolean::class.java).observe(this) {
            MMKVTool.saveReconnect(true)
        }

        LiveEventBus.get(EVENTBUS_GROUP_MEMBER,MemberItem::class.java).observe(this){

            if (it.code == 200){
            }

        }

        LiveEventBus.get(EVENTBUS_GROUP_NAME,GroupName::class.java).observe(this){

            val groups = it.groups

            for(group in groups){
            }


        }


        imManager = ImManager.getInstance()


        LiveEventBus.get(EVENTBUS_USER_INFO,PersonInfoBean::class.java).observe(this) {
//            connectIM("12")
            it?.imToken?.let { it1 -> connectIM(it1) }
            getMessage()
//            connectIM("EZwwh9PYPQZpQnuF2rVycVuWn8ZzuNtuEfJoG4gGsUA=@yx9p.cn.rongnav.com;yx9p.cn.rongcfg.com")
        }

        LiveEventBus.get(
            EVENTBUS_TOKEN_INVALID,
            String::class.java
        ).observe(this){

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

        }

    }
//    private val reciver: PushReceiver = PushReceiver()


//    @MainThread
    fun getMessage() {
    }




    private fun connectIM(token : String){
        CHAT_ACCOUNT = token
        sipManager.register(token, "12345", CHAT_IP,CHAT_PORT, 0)
        imManager.login(CHAT_IP,token,"12345");

        /*val timeLimit : Int = 1000
        RongIM.connect(token, timeLimit, object : RongIMClient.ConnectCallback(){
            override fun onSuccess(t: String?) {
                *//*if (t != null) {
                    pyToast(t+"mainactivity")
                }else{
                    pyToast("连接成功")
                }*//*
                if (t != null) {
                    mChatViewModel.getGroupName(t)
                }

            }

            override fun onError(e: RongIMClient.ConnectionErrorCode?) {
            }

            override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {

                if (RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS == code){

                }else{
                    pyToast("数据库打开失败")
                }

            }

        })*/
    }



    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(reciver)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            // 获取FragmentManager

            // 获取FragmentManager
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            val navController = navHostFragment.childFragmentManager.primaryNavigationFragment

            if (navController is MainFragment){

                if (navController.viewPager.currentItem == 0){
                    if(!navController.bottomNavBar.isShown){


                        LiveEventBus.get(EVENTBUS_EVENT_BACK).post(true)

                        return true
                    }else{
                        HyAppDialog(this)
                            .setSingle(false)
                            .canCancel(true)
                            .setTitle(resources.getString(R.string.sure_exit))
                            .setPositive(resources.getString(R.string.sure))
                            .setPositiveCallBack {
                                finish()
                            }
                            .show()
                        return true
                    }
                }else{

                    HyAppDialog(this)
                        .setSingle(false)
                        .canCancel(true)
                        .setTitle(resources.getString(R.string.sure_exit))
                        .setPositive(resources.getString(R.string.sure))
                        .setPositiveCallBack {
                            finish()
                        }
                        .show()
                    return true
                }


            }

           return super.onKeyDown(keyCode, event)
        }


        return super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_WRITE_EXTERNAL_STORAGE ->{
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    pyToast("已申请权限")
//                }else{
//                    pyToast("申请权限失败")
//                }
            }
        }
    }
}