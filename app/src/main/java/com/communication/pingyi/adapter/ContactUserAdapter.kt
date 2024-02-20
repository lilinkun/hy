package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_CONTACT_USER_CLICK
import com.communication.lib_http.httpdata.contact.ContactUserBean
import com.communication.pingyi.databinding.ItemContactUserBinding
import com.jeremyliao.liveeventbus.LiveEventBus


/**
 * Created by LG
 * on 2022/4/1  16:53
 * Description：
 */
class ContactUserAdapter : ListAdapter<ContactUserBean, RecyclerView.ViewHolder>(ContactUserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyUserViewHolder(
            ItemContactUserBinding.inflate(
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
        private val binding: ItemContactUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactUserBean, position: Int) {
            binding.apply {
                user = item
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

private class ContactUserDiffCallback : DiffUtil.ItemCallback<ContactUserBean>() {

    override fun areItemsTheSame(oldItem: ContactUserBean, newItem: ContactUserBean): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ContactUserBean, newItem: ContactUserBean): Boolean {
        return oldItem == newItem
    }
}
