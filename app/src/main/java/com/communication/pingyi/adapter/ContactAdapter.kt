package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_APP_CLICK
import com.communication.lib_core.tools.EVENTBUS_CONTACT_CLICK
import com.communication.lib_http.httpdata.contact.ContactItem
import com.communication.lib_http.httpdata.home.AppsItem
import com.communication.pingyi.databinding.ItemContactBinding
import com.communication.pingyi.databinding.ItemHomeAppsBinding
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/31  16:14
 * Description：
 */
class ContactAdapter : ListAdapter<ContactItem, RecyclerView.ViewHolder>(ContactDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = getItem(position)
        (holder as PyViewHolder).bind(app, position)
    }

    class PyViewHolder(
        private val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactItem, position: Int) {
            binding.apply {
                contact = item
                setClickListener {
                    if (checkDoubleClick()) {
                        LiveEventBus.get(EVENTBUS_CONTACT_CLICK).post(item)
                    }
                }
                executePendingBindings()
            }
        }
    }
}

private class ContactDiffCallback : DiffUtil.ItemCallback<ContactItem>() {

    override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
        return oldItem == newItem
    }
}
