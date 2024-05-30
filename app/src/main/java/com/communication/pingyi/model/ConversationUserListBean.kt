package com.communication.pingyi.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class ConversationUserListBean<T>(val code: Int,val msg:String,val data : List<T>)

//查询用户列表 userId: 用户id   userName: 用户名  onlineStatus: 0:离线 1：在线  lastMessage: 最后一条消息  updateTime：最后一次发送时间  count：未读数量
data class ConversationUserInfo(val userId : Int,val userName : String,val onlineStatus : Int,val lastMessage : String , val updateTime : String,val count : Int) : Serializable

//查询历史消息列表 id: 消息id chateKey: 消息key  messageType: 消息类型（文字0,图片1,语音3、视频4、定位6）   fromId: 消息发起方  content: 消息内容     name：发送方用户名   id: 消息id sendTime:发送时间（时间戳）   isReady：0：未读  1：已读
@Parcelize
data class ConversationMsgList(
    val avatar: String,
    val chatKey: String,
    val content: String,
    val fromId: String,
    val id: String,
    val isReady: String,
    val messageType: String,
    val sendTime: Long,
    val userName: String
) : Parcelable
