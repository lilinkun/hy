package com.communication.pingyi.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.communication.pingyi.ui.message.alarm.AlarmFragment
import com.communication.pingyi.ui.message.event.EventFragment
import io.rong.imkit.conversationlist.ConversationListFragment

class MessagePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        0 to { ConversationListFragment() },
        1 to { EventFragment() },
        2 to { AlarmFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}