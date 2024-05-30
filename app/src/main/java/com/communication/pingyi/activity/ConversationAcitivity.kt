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
import android.provider.LiveFolders.INTENT
import com.communication.lib_core.tools.EVENTBUS_SEND_MESSAGE
import com.communication.pingyi.R
import com.communication.pingyi.model.Conversation
import com.communication.pingyi.model.ConversationUserInfo
import com.communication.pingyi.tools.ActivityUtil
import com.communication.pingyi.ui.message.message.ChatViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.ImManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class ConversationAcitivity : ConversationBaseAcitivity() {
    protected var mTargetId: String? = null
    protected var mConversationType: Conversation.ConversationType? = null

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



        LiveEventBus.get(EVENTBUS_MESSAGE_CONTENT,String::class.java).observe(this){

            imManager.sendMessage("0",user.userId.toString(),it,false,0,"","")

        }

        LiveEventBus.get(EVENTBUS_CHAT_MESSAGE,Boolean::class.java).observe(this){
            if (it){
                imManager.getMsgList(user.userId.toString(),"100")
            }
        }

        LiveEventBus.get(EVENTBUS_CHAT_USER,ConversationUserInfo::class.java).post(user)


        LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).observe(this){
            if (it == 1) {
                checkStoragePermission()
            }
        }

    }

    private fun setTitle(user : ConversationUserInfo) {
        mTitleBar.middleView.text = user.userName
    }
    private val PICK_IMAGE_REQUEST = 1

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    val PICK_IMAGES_REQUEST = 1
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PICK_IMAGES_REQUEST)
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGES_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            try {
                // 获取图片并显示在ImageView中（假设你有一个ImageView来显示选中的图片）
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//                val imageView: ImageView = findViewById(R.id.imageView)
//                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}