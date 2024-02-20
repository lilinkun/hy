package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_CONTACT_CLICK
import com.communication.lib_core.tools.EVENTBUS_CONTACT_ORG_CLICK
import com.communication.lib_http.httpdata.contact.ContactItem
import com.communication.pingyi.R
import com.communication.pingyi.databinding.ItemContactBinding
import com.communication.pingyi.databinding.ItemContactOrgBinding
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/4/7  16:14
 * Description：
 */
class OrgListAdapter : ListAdapter<ContactItem, RecyclerView.ViewHolder>(OrgDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyViewHolder(
            ItemContactOrgBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = getItem(position)
        val count = itemCount - 1
        (holder as PyViewHolder).bind(app, position,count)
    }

    class PyViewHolder(
        private val binding: ItemContactOrgBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContactItem, position: Int,itemCount : Int) {
            binding.apply {
                contact = item
                if (itemCount != position){
                    tvTitle.setTextColor(ContextCompat.getColor(root.context, R.color.blue_dark))

                    setClickListener {
                        if (checkDoubleClick()) {
                            LiveEventBus.get(EVENTBUS_CONTACT_ORG_CLICK).post(item)
                        }
                    }
                    tvIcon.visibility = View.VISIBLE
                }else {
                    tvTitle.setTextColor(ContextCompat.getColor(root.context, R.color.grey_normal))
                    tvIcon.visibility = View.INVISIBLE
                }
                executePendingBindings()
            }
        }
    }
}

private class OrgDiffCallback : DiffUtil.ItemCallback<ContactItem>() {

    override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
        return false
    }
}