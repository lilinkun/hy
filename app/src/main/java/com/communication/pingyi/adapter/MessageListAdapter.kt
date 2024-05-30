package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.pingyi.R
import com.communication.pingyi.model.ConversationMsgList
import java.text.SimpleDateFormat
import java.util.Date

class MessageListAdapter : ListAdapter<ConversationMsgList, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val TYPE_ONE = 1
        private const val TYPE_TWO = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ConversationMsgList>() {
            override fun areItemsTheSame(oldItem: ConversationMsgList, newItem: ConversationMsgList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ConversationMsgList, newItem: ConversationMsgList): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when(viewType){
            TYPE_ONE -> TypeOneViewHolder(inflater.inflate(R.layout.hy_item_caht_left_message, parent, false))
            TYPE_TWO -> TypeTwoViewHolder(inflater.inflate(R.layout.hy_item_caht_right_message, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).fromId){
             "109" -> TYPE_ONE
             else -> TYPE_TWO
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeOneViewHolder -> holder.bind(getItem(position))
            is TypeTwoViewHolder -> holder.bind(getItem(position))
        }
    }

    class TypeOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.c_title)
        private val time: TextView = itemView.findViewById(R.id.c_time)
        fun bind(item: ConversationMsgList) {
            textView.text = item.content
            time.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(item.sendTime) )
//            name.text = item.userName
//            if (item.messageType == "0"){
//                name.visibility = View.GONE
//            }
        }
    }

    class TypeTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.c_content)
        private val time: TextView = itemView.findViewById(R.id.c_time)
        fun bind(item: ConversationMsgList) {
            textView.text = item.content
            time.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(item.sendTime) )
        }
    }

}
