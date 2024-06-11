package com.communication.pingyi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_CONTACT_USER_CLICK
import com.communication.lib_core.tools.Utils
import com.communication.pingyi.databinding.ItemChatListBinding
import com.communication.pingyi.model.ConversationUserInfo
import com.jeremyliao.liveeventbus.LiveEventBus

class UserListAdapter : ListAdapter<ConversationUserInfo, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ConversationUserInfo>() {
            override fun areItemsTheSame(oldItem: ConversationUserInfo, newItem: ConversationUserInfo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ConversationUserInfo, newItem: ConversationUserInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserListAdapter.PyUserViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = getItem(position)
        (holder as PyUserViewHolder).bind(app, position)
    }

    class PyUserViewHolder(
        private val binding: ItemChatListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConversationUserInfo, position: Int) {
            binding.apply {
                message = item

                val sp = Utils.convertHtmlToSpannable(item.lastMessage, itemView.context)

                rcConversationContent.text = sp
                setClickListener {
                    if (checkDoubleClick()) {
                        LiveEventBus.get(EVENTBUS_CONTACT_USER_CLICK).post(item)
                    }
                }

                executePendingBindings()
            }
        }
    }

}