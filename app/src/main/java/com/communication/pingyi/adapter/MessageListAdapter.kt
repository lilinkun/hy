package com.communication.pingyi.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.communication.lib_core.tools.Constance.Companion.CHAT_ACCOUNT
import com.communication.lib_core.tools.EVENTBUS_VOICE_CLICK
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.api.CHAT_IP
import com.communication.pingyi.R
import com.communication.pingyi.model.ConversationMsgList
import com.communication.pingyi.model.VoiceContent
import com.jeremyliao.liveeventbus.LiveEventBus
import java.io.IOException
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


        private fun showImagePopup(anchorView: View, imageResUrl: String) {
            val inflater: LayoutInflater = anchorView.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.popup_image, null)

            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)

            val imageView: ImageView = popupView.findViewById(R.id.iv_large_image)
            Glide.with(anchorView.context).load(imageResUrl).into(imageView)

            popupWindow.showAtLocation(anchorView, android.view.Gravity.CENTER, 0, 0)

            // 点击弹窗外部关闭弹窗
            popupView.setOnClickListener {
                popupWindow.dismiss()
            }
        }


        private fun imageReplace(content_url : String): String {
            val content = JSONObject.parseObject(content_url).getString("url")
            var url = "http://" + CHAT_IP + ":81" + content.replace("/soft/upload","/profile")
            return url
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
        return when(getItem(position).userName){
            CHAT_ACCOUNT -> TYPE_TWO
             else -> TYPE_ONE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeOneViewHolder -> holder.bind(getItem(position))
            is TypeTwoViewHolder -> holder.bind(getItem(position))
        }
    }

    class TypeOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.tv_content)
        private val time: TextView = itemView.findViewById(R.id.c_time)
        private val image : ImageView = itemView.findViewById(R.id.ic_content)
        fun bind(item: ConversationMsgList) {

            time.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(item.sendTime) )

            when(item.messageType){
                "0" -> {
                    image.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                    if (item.content.contains("/static/img/emote")){
                        val spannableString = Utils.convertHtmlToSpannable(item.content,itemView.context)
                        textView.text = spannableString
                    }else{
                        textView.text = item.content
                    }
                }
                "1" -> {
                    image.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    Glide.with(itemView.context).load(imageReplace(item.content)).into(image)
                    image.setOnClickListener {
                    showImagePopup(image,imageReplace(item.content))
                    }
                }
                "3" -> {}
            }
//            name.text = item.userName
//            if (item.messageType == "0"){
//                name.visibility = View.GONE
//            }
        }
    }

    class TypeTwoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.c_content)
        private val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
        private val image : ImageView = itemView.findViewById(R.id.ic_content)
        private val ic_voice : ImageView = itemView.findViewById(R.id.iv_voice)
        private val time: TextView = itemView.findViewById(R.id.c_time)
        private val llContent: LinearLayout = itemView.findViewById(R.id.ll_content)
        private val rlContent: RelativeLayout = itemView.findViewById(R.id.rl_content)
        fun bind(item: ConversationMsgList) {
            //messageType必须是. 文字0,图片1,语音3、视频4,定位6"
            if (item.messageType == "1"){
                image.visibility = View.VISIBLE
                textView.visibility = View.GONE
                Glide.with(itemView.context).load(imageReplace(item.content)).into(image)
                image.setOnClickListener {
                    showImagePopup(image,imageReplace(item.content))
                }
            }else if (item.messageType == "0"){
                textView.text = item.content
                textView.visibility = View.VISIBLE
                image.visibility = View.GONE
                if (item.content.contains("/static/img/emote")){
                    val spannableString = Utils.convertHtmlToSpannable(item.content,itemView.context)
                    textView.text = spannableString
                }else{
                    textView.text = item.content
                }
            }else if (item.messageType == "3"){
                llContent.visibility = View.GONE
                rlContent.visibility = View.VISIBLE
                val jsonObject = JSONObject.parseObject(item.content)
                tvDuration.text = ((jsonObject.getInteger("length")/1000).toInt()).toString() + ""
                ic_voice.setBackgroundResource(R.drawable.voice_animation_right)
                var voiceAnimation = ic_voice.background as AnimationDrawable
                rlContent.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        val voiceContent : VoiceContent = VoiceContent(voiceAnimation,jsonObject.getString("url"))
                        LiveEventBus.get(EVENTBUS_VOICE_CLICK, VoiceContent::class.java).post(voiceContent)
                    }

                })
            }
            time.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(item.sendTime) )
        }


    }


}
