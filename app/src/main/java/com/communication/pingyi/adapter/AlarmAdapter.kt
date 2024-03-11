package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.tools.EVENTBUS_ALARM_CLICK
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CLICK
import com.communication.lib_http.httpdata.message.AlarmTodoBean
import com.communication.pingyi.databinding.ItemAlarmBinding
import com.jeremyliao.liveeventbus.LiveEventBus

class AlarmAdapter : ListAdapter<AlarmTodoBean, RecyclerView.ViewHolder>(AlarmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyViewHolder(
            ItemAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = getItem(position)
        (holder as PyViewHolder).bind(app)
    }

    class PyViewHolder(
        private val binding: ItemAlarmBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlarmTodoBean) {
            binding.apply {
                message = item
                setClickListener {

                    LiveEventBus.get(EVENTBUS_ALARM_CLICK).post(item.id)

                }
                executePendingBindings()
            }
        }
    }
}


private class AlarmDiffCallback : DiffUtil.ItemCallback<AlarmTodoBean>() {
    override fun areItemsTheSame(oldItem: AlarmTodoBean, newItem: AlarmTodoBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: AlarmTodoBean,
        newItem: AlarmTodoBean
    ): Boolean {
        return oldItem == newItem
    }
}