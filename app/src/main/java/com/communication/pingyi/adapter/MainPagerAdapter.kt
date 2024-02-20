package com.communication.pingyi.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.communication.pingyi.ui.contact.contact.ContactFragment
import com.communication.pingyi.ui.me.me.MeFragment
import com.communication.pingyi.ui.message.HomeMessageFragment
import com.communication.pingyi.ui.webview.WebviewFragment

/**
 * Created by LG
 * on 2022/3/17  11:22
 * Description：
 */
const val ITEM_HOME = 0
const val ITEM_CONTACTS = 1
const val ITEM_MESSAGE = 2
const val ITEM_ME = 3

class MainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        ITEM_HOME to { WebviewFragment() },
        ITEM_CONTACTS to { ContactFragment() },
        ITEM_MESSAGE to { HomeMessageFragment() },
        ITEM_ME to { MeFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}