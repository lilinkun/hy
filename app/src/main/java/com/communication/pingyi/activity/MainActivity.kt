package com.communication.pingyi.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.communication.lib_core.HyAppDialog
import com.communication.lib_core.tools.EVENTBUS_EVENT_BACK
import com.communication.lib_core.tools.EVENTBUS_GROUP_MEMBER
import com.communication.lib_core.tools.EVENTBUS_GROUP_NAME
import com.communication.lib_core.tools.EVENTBUS_LOGIN_FAIL
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_BADGE
import com.communication.lib_core.tools.EVENTBUS_TOKEN_INVALID
import com.communication.lib_core.tools.EVENTBUS_USER_INFO
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.api.mBaseModel
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.httpdata.login.LoginInfo
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.lib_http.httpdata.message.GroupName
import com.communication.lib_http.httpdata.message.MemberItem
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseActivity
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.tools.ActivityUtil
import com.communication.pingyi.ui.login.account.LoginViewModel
import com.communication.pingyi.ui.main.MainFragment
import com.communication.pingyi.ui.message.message.ChatViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.ImManager
import io.rong.imkit.GlideKitImageEngine
import io.rong.imkit.IMCenter
import io.rong.imkit.RongIM
import io.rong.imkit.config.RongConfigCenter
import io.rong.imkit.conversation.extension.RongExtensionManager
import io.rong.imkit.feature.mention.RongMentionManager.IGroupMemberCallback
import io.rong.imkit.userinfo.RongUserInfoManager
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Group
import io.rong.imlib.model.Message
import io.rong.imlib.model.UserInfo
import io.rong.sight.SightExtensionModule
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    val mLoginViewModel : LoginViewModel by viewModel<LoginViewModel>()
    val mChatViewModel : ChatViewModel by viewModel<ChatViewModel>()
    lateinit var mLoginInfo : LoginInfo;

    lateinit var iGroupMemberCallback : IGroupMemberCallback

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

        RongIM.getInstance().setGroupMembersProvider { groupId, callback ->

            var group = RongUserInfoManager.getInstance().getGroupInfo(groupId)

            if (group == null){

                mChatViewModel.getGroupMember(groupId)

                iGroupMemberCallback = callback
            }
        }

        IMCenter.getInstance().addOnReceiveMessageListener(
            object : RongIMClient.OnReceiveMessageWrapperListener() {
                override fun onReceived(
                    message: Message,
                    left: Int,
                    hasPackage: Boolean,
                    offline: Boolean
                ): Boolean {

                    if (message.conversationType == Conversation.ConversationType.PRIVATE){
                        val userInfo = RongUserInfoManager.getInstance().getUserInfo(message.senderUserId)
                        if (userInfo == null){

                        }
                    }else if (message.conversationType == Conversation.ConversationType.GROUP){

                    }


                    return false
                }
            })

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
                val users = ArrayList<UserInfo>()
                val members = it.members
                for (index in members.indices){
                    val userInfo = UserInfo(members[index].toString(),members[index].toString(),null)
                    users.add(userInfo)
                }
                iGroupMemberCallback.onGetGroupMembersResult(users)
            }

        }

        LiveEventBus.get(EVENTBUS_GROUP_NAME,GroupName::class.java).observe(this){

            val groups = it.groups

            for(group in groups){
                var gro = Group(group.id,group.name,null)
                RongUserInfoManager.getInstance().refreshGroupInfoCache(gro);
            }


        }


        var imManager : ImManager = ImManager.getInstance()


        LiveEventBus.get(EVENTBUS_USER_INFO,PersonInfoBean::class.java).observe(this) {

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
        RongIMClient.getInstance().getTotalUnreadCount(MyResultCallback())
    }

    class MyResultCallback() : RongIMClient.ResultCallback<Int>() {
        override fun onSuccess(p0: Int?) {
            Log.i("MyResultCallback", "$p0")

            LiveEventBus.get(EVENTBUS_MESSAGE_BADGE).post(p0)

        }
        override fun onError(p0: RongIMClient.ErrorCode?) {
            Log.i("MyResultCallback", p0.toString())
        }
    }

    private fun connectIM(token : String){
        val timeLimit : Int = 1000
        RongIM.connect(token, timeLimit, object : RongIMClient.ConnectCallback(){
            override fun onSuccess(t: String?) {
                /*if (t != null) {
                    pyToast(t+"mainactivity")
                }else{
                    pyToast("连接成功")
                }*/
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

        })
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
}