package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_CONTACT_SEARCH_USER_CLICK
import com.communication.lib_core.tools.EVENTBUS_CONTACT_USER_CLICK
import com.communication.lib_http.httpdata.contact.ContactUserBean
import com.communication.lib_http.httpdata.contact.SearchUserBean
import com.communication.pingyi.databinding.ItemContactUserBinding
import com.communication.pingyi.databinding.ItemSearchUserBinding
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/4/7  10:53
 * Description：
 */
class ContactSearchAdapter : ListAdapter<SearchUserBean, RecyclerView.ViewHolder>(ContactSearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyUserViewHolder(
            ItemSearchUserBinding.inflate(
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
        private val binding: ItemSearchUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchUserBean, position: Int) {
            binding.apply {
                user = item
                setClickListener {
                    if (checkDoubleClick()) {
                        LiveEventBus.get(EVENTBUS_CONTACT_SEARCH_USER_CLICK).post(item)
                    }
                }

                executePendingBindings()
            }
        }
    }
}

private class ContactSearchDiffCallback : DiffUtil.ItemCallback<SearchUserBean>() {

    override fun areItemsTheSame(oldItem: SearchUserBean, newItem: SearchUserBean): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SearchUserBean, newItem: SearchUserBean): Boolean {
        return oldItem == newItem
    }
}
