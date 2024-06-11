package com.communication.pingyi.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.communication.lib_core.tools.EVENTBUS_CHAT_MESSAGE
import com.communication.lib_core.tools.EVENTBUS_CHAT_USER
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CONTENT
import android.Manifest
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.provider.LiveFolders.INTENT
import android.util.Log
import androidx.loader.content.CursorLoader
import com.communication.lib_core.tools.EVENTBUS_SEND_MESSAGE
import com.communication.lib_core.tools.EVENTBUS_VOICE_CLICK
import com.communication.lib_core.tools.EVENTBUS_VOICE_MESSAGE_CONTENT
import com.communication.lib_http.api.CHAT_IP
import com.communication.pingyi.R
import com.communication.pingyi.model.Conversation
import com.communication.pingyi.model.ConversationUserInfo
import com.communication.pingyi.model.VoiceContent
import com.communication.pingyi.tools.ActivityUtil
import com.communication.pingyi.tools.PhotoUtils
import com.communication.pingyi.ui.message.message.ChatViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.ImManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.net.URI


class ConversationAcitivity : ConversationBaseAcitivity() {
    protected var mTargetId: String? = null
    protected var mConversationType: Conversation.ConversationType? = null

    private var mediaPlayer: MediaPlayer? = null
    lateinit var user : ConversationUserInfo

    val imManager: ImManager = ImManager.getInstance()

    val mChatViewModel : ChatViewModel by viewModel<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtil.addActivity(this)

        setContentView(R.layout.activity_conversation)


        val bundle = intent.extras

        if (bundle != null) {
            user = bundle.getSerializable("user") as ConversationUserInfo
            setTitle(user)
        }

        imManager.getMsgList(user.userId.toString(),"100")



        LiveEventBus.get(EVENTBUS_VOICE_MESSAGE_CONTENT,File::class.java).observe(this){

            imManager.sendMessage("3",user.userId.toString(),it.absolutePath,false,getAudioDuration(it),"","")

        }

        LiveEventBus.get(EVENTBUS_MESSAGE_CONTENT,String::class.java).observe(this){

            imManager.sendMessage("0",user.userId.toString(),it,false,0,"","")

        }


        LiveEventBus.get(EVENTBUS_CHAT_MESSAGE,Boolean::class.java).observe(this){
            if (it){
                imManager.getMsgList(user.userId.toString(),"100")
            }
        }

        LiveEventBus.get(EVENTBUS_CHAT_USER,ConversationUserInfo::class.java).post(user)

        LiveEventBus.get(EVENTBUS_VOICE_CLICK, VoiceContent::class.java).observe(this){
            playVoice(it.url,it.voiceAnimation)
        }

        LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).observe(this){
            if (it == 1) {
//                checkStoragePermission()
                PhotoUtils.startAlbum(this)
            }else if (it == 2) {
                PhotoUtils.startCamera(this)
            }
        }

    }

    private fun playVoice(filePath: String,voiceAnimation : AnimationDrawable) {
        var path  = "http://"+ CHAT_IP+":82"+ filePath.replace("/soft/upload","/profile");
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(path)
                prepare()
                start()
                voiceAnimation.start()
                setOnCompletionListener {
                    voiceAnimation.stop()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取语音的长度
     */
    private fun getAudioDuration(file: File): Int {
        val mediaPlayer = MediaPlayer()
        return try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            val duration = mediaPlayer.duration
            mediaPlayer.release()
            duration
        } catch (e: IOException) {
            e.printStackTrace()
            0
        }
    }

    private fun setTitle(user : ConversationUserInfo) {
        mTitleBar.middleView.text = user.userName
    }
    private val PICK_IMAGE_REQUEST = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PhotoUtils.RESULT_CODE_PHOTO && resultCode == RESULT_OK && data != null && data.data != null) {
            data.let {
//                val clipData = it.clipData
//                if (clipData != null) {
//                    // 多选情况
//                    for (i in 0 until clipData.itemCount) {
//                        val uri = clipData.getItemAt(i).uri
//                        handleImage(uri)
//                    }
//                } else {
                    // 单选情况
                    it.data?.let { Uri->
                        val aPath = getRealPathFromURI(this,Uri)
                        if (aPath != null) {
                            handleImage(aPath)
                        }
                    }
//                }
            }
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        cursor?.use {
            val column_index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(column_index)
        }
        return null
    }

    private fun handleImage(uri: String) {
        // 处理选中的图片 URI
        Log.d("Selected Image URI", uri.toString())
        // 可以将 URI 添加到一个列表中，显示在 UI 上或上传到服务器等
        if (uri.toString().contains("video")){
            imManager.sendMessage("4",user.userId.toString(),uri.toString(),false,0,"","")
        }else{
            imManager.sendMessage("1",user.userId.toString(),uri.toString(),false,0,"","")
        }

    }

}