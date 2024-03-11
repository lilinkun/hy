package com.communication.pingyi.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.viewpager2.widget.ViewPager2
import com.communication.lib_core.PyMessageRed
import com.communication.lib_core.tools.EVENTBUS_ALARM_CLICK
import com.communication.lib_core.tools.EVENTBUS_APP_CLICK
import com.communication.lib_core.tools.EVENTBUS_EVENT_BOTTOM
import com.communication.lib_core.tools.EVENTBUS_GROUP_CLICK
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_BADGE
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CLICK
import com.communication.lib_core.tools.EVENTBUS_UNREAD_MESSAGE
import com.communication.lib_http.api.WEB_ALARM
import com.communication.lib_http.api.WEB_EVENT
import com.communication.pingyi.R
import com.communication.pingyi.activity.WebviewActivity
import com.communication.pingyi.adapter.ITEM_CONTACTS
import com.communication.pingyi.adapter.ITEM_HOME
import com.communication.pingyi.adapter.ITEM_ME
import com.communication.pingyi.adapter.ITEM_MESSAGE
import com.communication.pingyi.adapter.MainPagerAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentMainBinding
import com.communication.pingyi.ext.pyToast
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeremyliao.liveeventbus.LiveEventBus
import io.rong.imkit.manager.UnReadMessageManager
import io.rong.imkit.userinfo.RongUserInfoManager
import io.rong.imkit.userinfo.model.GroupUserInfo
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.ConversationIdentifier
import io.rong.imlib.model.Group
import io.rong.imlib.model.UserInfo

/**
 * Created by LG
 * on 2022/3/16  17:42
 * Description：
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    lateinit var viewPager : ViewPager2
    lateinit var bottomNavBar : BottomNavigationView

    override fun getLayoutResId(): Int = R.layout.fragment_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(
            EVENTBUS_APP_CLICK,
            String::class.java
        ).observe(this,{ key->
            key?.let {
                goToWebActivity(it)
            }
        })

        LiveEventBus.get(
            EVENTBUS_MESSAGE_CLICK,
            String::class.java
        ).observe(this,{ key->
            key?.let {
                goToWebActivity(WEB_EVENT+it + "&flag=false")
            }
        })
        LiveEventBus.get(
            EVENTBUS_ALARM_CLICK,
            Int::class.java
        ).observe(this,{ key->
            key?.let {
                goToWebActivity(WEB_ALARM+it)
            }
        })

        LiveEventBus.get(
            EVENTBUS_UNREAD_MESSAGE,
            Int::class.java
        ).observe(this,{
            initMessage(it)
        })

        LiveEventBus.get(
            EVENTBUS_GROUP_CLICK,
            String::class.java
        ).observe(this,{

            val targetId = it
            val bundle = Bundle()
            val conversationIdentifier = ConversationIdentifier(Conversation.ConversationType.GROUP, targetId);
            RouteUtils.routeToConversationActivity(context, conversationIdentifier, false, bundle)
        })


        LiveEventBus.get(
            EVENTBUS_EVENT_BOTTOM,
            Boolean::class.java
        ).observe(this,{
            if (it){
                bottomNavBar.visibility = View.VISIBLE
            }else{
                bottomNavBar.visibility = View.GONE
            }
        })
        val conversationType: Conversation.ConversationType = Conversation.ConversationType.PRIVATE
        val groupConversationType: Conversation.ConversationType = Conversation.ConversationType.GROUP

        var conversationTypes : MutableList<Conversation.ConversationType> = ArrayList()

        conversationTypes.add(conversationType)
        conversationTypes.add(groupConversationType)

        UnReadMessageManager.getInstance().addObserver(conversationTypes.toTypedArray(),object : UnReadMessageManager.IUnReadMessageObserver{
            override fun onCountChanged(count: Int) {

                LiveEventBus.get(EVENTBUS_MESSAGE_BADGE).post(count)
            }

        })


        RongUserInfoManager.getInstance().addUserDataObserver(object :RongUserInfoManager.UserDataObserver{
            override fun onUserUpdate(info: UserInfo?) {

            }

            override fun onGroupUpdate(group: Group?) {
            }

            override fun onGroupUserInfoUpdate(groupUserInfo: GroupUserInfo?) {
            }

        })

    }

    override fun onStart() {
        super.onStart()


        LiveEventBus.get("message",Boolean::class.java).observe(this,{

            jumpCurrent()

        })
    }

    override fun initView() {
        viewPager = binding.viewPager
        bottomNavBar = binding.bottomNavBar
        viewPager.adapter = MainPagerAdapter(this)
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = 4
        viewPager.isUserInputEnabled = false//禁止滑动
        bottomNavBar.visibility = View.GONE

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavBar.menu.getItem(position).isChecked = true
            }
        })

        bottomNavBar.itemIconTintList=null
        bottomNavBar.setOnNavigationItemSelectedListener {
            var index = ITEM_MESSAGE
            index = when (it.itemId) {
                R.id.nav_home -> ITEM_HOME
                R.id.nav_contacts -> ITEM_CONTACTS
                R.id.nav_message -> ITEM_MESSAGE
                R.id.nav_me -> ITEM_ME
                else -> ITEM_HOME
            }
            viewPager.setCurrentItem(index, false)
            true
        }

    }

    override fun observeViewModels() {
    }

    private fun initMessage(unReadCount : Int) {

        var count = unReadCount

        binding.bottomNavBar.children.forEach { menuView ->
            if (menuView is BottomNavigationMenuView) {
                menuView.forEachIndexed { index, itemView ->
                    if (index == 2) {
                        if (itemView is BottomNavigationItemView) {
                            itemView.forEach { v ->
                                if (v is PyMessageRed) {
                                    itemView.removeView(v)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (count != 0) {
            binding.bottomNavBar.children.forEach { menuView ->
                if (menuView is BottomNavigationMenuView) {
                    menuView.forEachIndexed { index, itemView ->
                        if (index == 2) {
                            if (itemView is BottomNavigationItemView) {
                                val t = PyMessageRed(requireContext())
                                t.setCount(count.toString())
                                itemView.addView(t)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun jumpCurrent(){

        viewPager.setCurrentItem(2, false)
    }


    private fun goToWebActivity(url : String) {
        val intent = Intent(requireContext(), WebviewActivity::class.java)
        intent.putExtra("url",url)
        startActivity(intent)
        /*val dir = MainFragmentDirections.actionMainFragmentToWebViewFragment(url = url)
        findNavController().navigate(dir)*/
    }


}