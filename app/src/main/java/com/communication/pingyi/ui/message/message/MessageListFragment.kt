/*
package com.communication.pingyi.ui.message.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.communication.pingyi.activity.ChatAcitivity
import io.rong.common.RLog
import io.rong.imkit.config.RongConfigCenter
import io.rong.imkit.conversationlist.ConversationListAdapter
import io.rong.imkit.conversationlist.ConversationListFragment
import io.rong.imkit.conversationlist.model.GatheredConversation
import io.rong.imkit.utils.RouteUtils
import io.rong.imkit.widget.adapter.ViewHolder
import java.util.Locale

class MessageListFragment : ConversationListFragment() {
    private val TAG = MessageListFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResolveAdapter(): ConversationListAdapter {
        return super.onResolveAdapter()
    }

    override fun onItemClick(view: View?, holder: ViewHolder?, position: Int) {

        if (position >= 0 && position < mAdapter.data.size) {
            val baseUiConversation = mAdapter.getItem(position)
            val listBehaviorListener = RongConfigCenter.conversationListConfig().listener
            if (listBehaviorListener != null && listBehaviorListener.onConversationClick(
                    requireView().context,
                    view,
                    baseUiConversation
                )
            ) {
                RLog.d(TAG, "ConversationList item click event has been intercepted by App.")
            } else {
                if (baseUiConversation != null && baseUiConversation.mCore != null) {
                    if (baseUiConversation is GatheredConversation) {
                        RouteUtils.routeToSubConversationListActivity(
                            requireView().context,
                            baseUiConversation.mGatheredType,
                            baseUiConversation.mCore.conversationTitle
                        )

                    } else {
//                        RouteUtils.routeToConversationActivity(
//                            requireView().context,
//                            baseUiConversation.conversationIdentifier
//                        )


                        val intent : Intent = Intent(context, ChatAcitivity::class.java)
                        intent.putExtra("ConversationType", baseUiConversation.conversationIdentifier.type.getName().lowercase(Locale.getDefault()))
                        intent.putExtra("targetId", baseUiConversation.conversationIdentifier.getTargetId())
                        intent.putExtra("ConversationIdentifier", baseUiConversation.conversationIdentifier)
                        startActivity(intent)
                    }
                } else {
                    RLog.e(TAG, "invalid conversation.")
                }
            }
        }
    }

}*/
