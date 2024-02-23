package com.communication.pingyi.ui.message

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.communication.lib_core.PyAppDialog
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_ALARM_BADGE
import com.communication.lib_core.tools.EVENTBUS_CHECK_UPDATE_VERSION_BUTTON
import com.communication.lib_core.tools.EVENTBUS_EVENT_BADGE
import com.communication.lib_core.tools.EVENTBUS_LOGIN_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_BADGE
import com.communication.lib_core.tools.EVENTBUS_UNREAD_MESSAGE
import com.communication.lib_http.httpdata.message.AlarmTodoBean
import com.communication.lib_http.httpdata.message.EventTodoBean
import com.communication.lib_http.httpdata.version.VersionModel
import com.communication.pingyi.R
import com.communication.pingyi.adapter.MessageAdapter
import com.communication.pingyi.adapter.MessagePagerAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentHomeMessageBinding
import com.communication.pingyi.ext.pyToastShort
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import io.rong.imkit.RongIM
import io.rong.imkit.userinfo.RongUserInfoManager
import io.rong.imkit.userinfo.UserDataProvider
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.UserInfo
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeMessageFragment : BaseFragment<FragmentHomeMessageBinding>() , OnRefreshListener,
    OnLoadMoreListener {

    val mViewModel : MessageViewModel by viewModel<MessageViewModel>()

    private val messageAdapter = MessageAdapter()
    private lateinit var userId : String;
    private lateinit var mViewPager: ViewPager2

    var unReadCount = 0

    var eventCount  = -1
    var alarmCount  = -1
    var msgCount  = -1

    override fun getLayoutResId(): Int = R.layout.fragment_home_message


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getMessageList()


//        RongUserInfoManager.getInstance().setUserInfoProvider(object : UserDataProvider.UserInfoProvider{
//            override fun getUserInfo(userId: String?): UserInfo {
//
//                var imgUrl = ""
//                if(userId.equals("test1")){
//                    imgUrl = "http://192.168.100.240:9776/liguo.jpg"
//                }else{
//                    imgUrl = "http://192.168.100.240:9776/liuhao.jpg"
//                }
//                val userinfo = UserInfo(userId,"test1", Uri.parse(imgUrl))
//
//                return userinfo
//            }
//        },true)

        LiveEventBus.get(
            EVENTBUS_ALARM_BADGE,
            Int::class.java
        ).observe(this) {
            alarmCount = it
            if (it > 0) {
                changeBadge(2, it)
            }

            mViewModel.unreadCount(eventCount,alarmCount,msgCount)
        }

        LiveEventBus.get(
            EVENTBUS_EVENT_BADGE,
            Int::class.java
        ).observe(this) {
            eventCount = it
            if (it > 0) {

                changeBadge(1, it)
            }
            mViewModel.unreadCount(eventCount,alarmCount,msgCount)
        }


        LiveEventBus.get(
            EVENTBUS_LOGIN_SUCCESS,
            Boolean::class.java
        ).observe(this,{

            if(it) {
                mViewModel.getMessageList()
            }
        })



        LiveEventBus.get(
            EVENTBUS_MESSAGE_BADGE,
            Int::class.java
        ).observe(this,{
            msgCount = it
            if (it > 0) {

                changeBadge(0, it)
            }else{
                changeBadge(0, it)
            }
            mViewModel.unreadCount(eventCount,alarmCount,msgCount)
        })


    }


    override fun initView() {


        val bundle = Bundle()

        mViewPager = binding.viewPager
        mViewPager.adapter = MessagePagerAdapter(this)

        mViewPager.offscreenPageLimit = 3


        val tabMe = TabLayoutMediator(binding.tabs, mViewPager) { tab, position ->
            tab.text = getTabTitle(position)
//            val badge = tab.orCreateBadge
//            badge.setVisible(true)
//            badge.number = 4
        }

        tabMe.attach()

        binding.apply {

            ptMessageMain.setIconOnClick {
                if (checkDoubleClick()) {
                    if (unReadCount != 0) {
                        userId?.let {
                            allReadMessage(it)
                        }
                    }else{
                        pyToastShort(resources.getString(R.string.no_read_message))
                    }

                }
            }
        }

    }



    fun changeBadge(tabIndexToModify:Int,numInt : Int){

        // 在需要修改Badge的地方，例如某个事件触发后
        val tabIndexToModify = tabIndexToModify // 要修改的Tab的索引
        val modifiedBadge = binding.tabs.getTabAt(tabIndexToModify)?.orCreateBadge
        if (numInt == 0){
            modifiedBadge?.isVisible = false
        }else{
            modifiedBadge?.isVisible = true
            modifiedBadge?.number = numInt
        }
    }




    /**
     * 消息全设置为已读
     */
    private fun allReadMessage(userId : String) {
        PyAppDialog(requireContext())
            .setSingle(false)
            .canCancel(true)
            .setTitle(resources.getString(R.string.main_message))
            .setMessage(resources.getString(R.string.all_read_hint))
            .setPositive(resources.getString(R.string.sure))
            .setPositiveCallBack {
                mViewModel.readAllMessage(userId)
            }
            .show()
    }

    override fun observeViewModels() {

        with(mViewModel){
            messageList.observe(viewLifecycleOwner){
                messageAdapter.submitList(it)
                messageAdapter.notifyDataSetChanged()

                it?.apply {
                    if (it.size > 0) {
                        userId = it.get(0).eventId
                        unReadCount = 0

                        for (messageBean: EventTodoBean in it) {
                            if (messageBean.eventStatus.equals("untreated")) {
                                unReadCount++
                            }
                        }
//                        LiveEventBus.get(EVENTBUS_UNREAD_MESSAGE).post(unReadCount)
                    }


                }


            }

            messageError.observe(viewLifecycleOwner){
                pyToastShort(it)
            }


        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh(200)
        mViewModel.getMessageList()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore(true)
        mViewModel.getMessageList()
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            0 -> "IM消息"
            1 -> "待办事件"
            2 -> "待办告警"
            else -> ""
        }
    }

}