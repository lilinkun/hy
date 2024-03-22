package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.communication.lib_core.tools.EVENTBUS_GROUP_CLICK
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CLICK
import com.communication.lib_http.httpdata.message.EventTodoBean
import com.communication.pingyi.R
import com.communication.pingyi.databinding.ItemMessageBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import extension.globalContext

class MessageAdapter : ListAdapter<EventTodoBean,RecyclerView.ViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyViewHolder(
            ItemMessageBinding.inflate(
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
        private val binding: ItemMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventTodoBean, position: Int) {
            binding.apply {
                message = item
//                when(item.messageType){
//                    0 -> { tvMessageType.setText("事件")
//                tvMessageType.setBackground(ContextCompat.getDrawable(root.context,R.mipmap.ic_message_event))
//                    1 -> {tvMessageType.setText("工单")
//                    tvMessageType.setBackground(ContextCompat.getDrawable(root.context,R.mipmap.ic_message_orders))}
//                    2 -> {tvMessageType.setText("养护")
//                    tvMessageType.setBackground(ContextCompat.getDrawable(root.context,R.mipmap.ic_message_maintain))}
//
//                if(item.isRead == 0){
//                    ivCircle.visibility = View.VISIBLE
//                }else{
//                    ivCircle.visibility = View.GONE
//                }
                ivGroupMessage.setOnClickListener {
                    LiveEventBus.get(EVENTBUS_GROUP_CLICK).post(item.groupId)
                }

                if (!item.imgUrl.isNullOrEmpty()) {
                    Glide.with(root.context)
                        .load(item.imgUrl)
                        .error(R.drawable.ic_default_img)
                        .into(ivMessageType)

                }

                setClickListener {
//                    if (checkDoubleClick()) {
//                        if(item.isRead == 0) {
//                            LiveEventBus.get(EVENTBUS_MESSAGE_ITEM_CLICK).post(item.id.toString())
//                        }
                        LiveEventBus.get(EVENTBUS_MESSAGE_CLICK).post(item.eventId)
                    }
//                }
                executePendingBindings()
            }
        }
    }
}


private class MessageDiffCallback : DiffUtil.ItemCallback<EventTodoBean>() {
    override fun areItemsTheSame(oldItem: EventTodoBean, newItem: EventTodoBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EventTodoBean,
        newItem: EventTodoBean
    ): Boolean {
        return oldItem == newItem
    }
}