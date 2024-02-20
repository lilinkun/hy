package com.communication.pingyi.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.communication.lib_core.PyMessageRed
import com.communication.lib_core.tools.EVENTBUS_APP_CLICK
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CLICK
import com.communication.lib_core.tools.EVENTBUS_UNREAD_MESSAGE
import com.communication.lib_http.api.WEB_MESSAGE
import com.communication.pingyi.R
import com.communication.pingyi.activity.WebviewActivity
import com.communication.pingyi.adapter.*
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/16  17:42
 * Description：
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    lateinit var viewPager : ViewPager2

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
                goToWebActivity(WEB_MESSAGE+it)
            }
        })

        LiveEventBus.get(
            EVENTBUS_UNREAD_MESSAGE,
            Int::class.java
        ).observe(this,{
            initMessage(it)
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
        val bottomNavBar = binding.bottomNavBar
        viewPager.adapter = MainPagerAdapter(this)
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = 4
        viewPager.isUserInputEnabled = false//禁止滑动

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