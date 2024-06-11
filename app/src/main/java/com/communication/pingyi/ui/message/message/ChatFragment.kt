package com.communication.pingyi.ui.message.message

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.media.MediaRecorder
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.communication.lib_core.FileImageSpan
import com.communication.lib_core.tools.EVENTBUS_CHAT_USER
import com.communication.lib_core.tools.EVENTBUS_GETMSGLIST
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_CONTENT
import com.communication.lib_core.tools.EVENTBUS_SEND_MESSAGE
import com.communication.lib_core.tools.EVENTBUS_VOICE_MESSAGE_CONTENT
import com.communication.lib_http.httpdata.contact.ExpressionBean
import com.communication.pingyi.R
import com.communication.pingyi.adapter.ExpressAdapter
import com.communication.pingyi.adapter.MessageListAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentChatBinding
import com.communication.pingyi.model.ConversationMsgList
import com.communication.pingyi.model.ConversationUserInfo
import com.communication.pingyi.model.ConversationUserListBean
import com.jeremyliao.liveeventbus.LiveEventBus
import com.matt.linphonelibrary.core.SIPManager
import java.io.File
import java.io.IOException


class ChatFragment : BaseFragment<FragmentChatBinding>() {

    val chatAdapter = MessageListAdapter()

    lateinit var expressAdapter : ExpressAdapter

    lateinit var user : ConversationUserInfo

    val sipManager: SIPManager = SIPManager.getInstance()

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    override fun getLayoutResId(): Int = R.layout.fragment_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(EVENTBUS_GETMSGLIST, ConversationUserListBean::class.java).observe(this){
            var list : List<ConversationMsgList> = it.data as List<ConversationMsgList>
            chatAdapter.submitList(list)
            chatAdapter.notifyDataSetChanged()
            binding.rcMessageList.scrollToPosition(chatAdapter.getItemCount() - 1);
        }

        LiveEventBus.get(EVENTBUS_CHAT_USER, ConversationUserInfo::class.java).observe(this){
            user = it
        }
    }

    override fun initView() {
        binding.apply {
            rcMessageList.layoutManager = LinearLayoutManager(activity)
            rcMessageList.adapter = chatAdapter


            messageInput.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (count > 0){
                        tvSend.visibility = View.VISIBLE
                        addButton.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

            messageInput.onFocusChangeListener =
                OnFocusChangeListener { v, hasFocus ->

                    binding.rcMessageList.scrollToPosition(chatAdapter.getItemCount() - 1);
                    if (hasFocus){

                    }else{

                    }

                }

            // 监听软键盘弹出和隐藏
            val rootView = activity?.window?.decorView?.rootView
            rootView?.viewTreeObserver?.addOnGlobalLayoutListener {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                if (keypadHeight > screenHeight * 0.15) {
                    // 软键盘弹出
                    scrollToBottom()
                    functionalButtonsLayout.visibility = View.GONE
                    glBottom.visibility = View.GONE
                }else{
                    tvSend.visibility = View.GONE
                    addButton.visibility = View.VISIBLE
                }
            }

            setSendClick{

                if (messageInput.text.toString().trim().isNotEmpty()) {

                    LiveEventBus.get(EVENTBUS_MESSAGE_CONTENT, String::class.java)
                        .post(getTextAndExpressions())
                    messageInput.setText("")
               }

            }

            setAddClick {
                hideKeyboard()
                if (!functionalButtonsLayout.isShown) {
                    functionalButtonsLayout.visibility = View.VISIBLE
                    glBottom.visibility = View.VISIBLE
                    rvExpression.visibility = View.GONE
                }else{
                    functionalButtonsLayout.visibility = View.GONE
                    glBottom.visibility = View.GONE
                }

            }

            setVideoClick {

                val number = user.userName.toString()
                sipManager.callNumber(number, true)

            }
            setVoiceClick {
                val number = user.userName.toString()
                sipManager.callNumber(number, false)

            }
            setPosClick {
                LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).post(3)
            }
            setPhotoClick {
                LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).post(1)
            }
            setCamareClick {
                LiveEventBus.get(EVENTBUS_SEND_MESSAGE,Int::class.java).post(2)
            }

            setColumnClick {
                functionalButtonsLayout.visibility = View.GONE
                hideKeyboard()
                if (binding.tvVoiceBtn.isShown){
                    binding.tvVoiceBtn.visibility = View.GONE
                }else{
                    binding.tvVoiceBtn.visibility = View.VISIBLE
                }
                binding.voiceBtn.setImageResource(R.drawable.rc_ext_toggle_keyboard)
            }

            expressAdapter = ExpressAdapter { expressionBean -> insertExpressionToInput(expressionBean.expressionId,expressionBean.expressionName) }

            binding.rvExpression.adapter = expressAdapter
            binding.rvExpression.layoutManager = GridLayoutManager(activity,8)
            // 获取资源 ID 列表
            val typedArray = resources.obtainTypedArray(R.array.rc_emoji_res)
            val emojiResIds = IntArray(typedArray.length()) { typedArray.getResourceId(it, 0) }
            val emojiResString = Array(typedArray.length()) { typedArray.getString(it)
                ?.let { it1 -> getExpressText(it1) } }
            typedArray.recycle()
            val lists = emojiResIds.toList()

            val emojiValues = emojiResString.toList()

            var expressionBeans : MutableList<ExpressionBean> = lists.zip(emojiValues){ list,ex -> ExpressionBean(list,
                ex.toString()
            )
            } as MutableList<ExpressionBean>


            expressAdapter.submitList(expressionBeans)


            setEmojiClick {
                hideKeyboard()
                if (rvExpression.isShown) {
                    functionalButtonsLayout.visibility = View.GONE
                    rvExpression.visibility = View.GONE
                }else{
                    functionalButtonsLayout.visibility = View.VISIBLE
                    rvExpression.visibility = View.VISIBLE
                    glBottom.visibility = View.GONE
                }
            }

            setLayoutClick {
                functionalButtonsLayout.visibility = View.GONE
                rvExpression.visibility = View.GONE
                hideKeyboard()
            }



            tvVoiceBtn.setOnTouchListener(object : View.OnTouchListener{
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            // 按下事件
                            context?.let { tvVoiceBtn.setBackgroundColor(it.resources.getColor(R.color.grey)) }

                            startRecording()

                            tvVoiceBtn.setText("松开发送")

                        }
                        MotionEvent.ACTION_UP -> {
                            // 松开事件
                            context?.let { tvVoiceBtn.setBackgroundColor(it.resources.getColor(R.color.white)) }
                            stopRecording()
                            tvVoiceBtn.setText("按住说话")
                            LiveEventBus.get(EVENTBUS_VOICE_MESSAGE_CONTENT,File::class.java).post(
                                audioFile

                            )
                        }
                    }
                    return true
                }

            })
        }


    }

    private fun startRecording() {
        audioFile = File.createTempFile("audio_", ".m4a", context?.cacheDir)
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    fun getExpressText(expression : String) : String{
        var exValue = expression.split("/")[2]
        return exValue

    }

//    private fun insertExpressionToInput(resId: Int) {
//        val drawable = context?.let { ContextCompat.getDrawable(it, resId) }
//        drawable?.setBounds(0, 0, 50, 50)
//        val span = ImageSpan(drawable!!, ImageSpan.ALIGN_BOTTOM)
//
//        val start = binding.messageInput.selectionStart
//        val end = binding.messageInput.selectionEnd
//
//        val editable = binding.messageInput.editableText
//        editable.replace(start, end, "")
//        editable.insert(start, SpannableString(" ").apply { setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) })
//    }

    private fun insertExpressionToInput(resId: Int, fileName: String) {
        val drawable = context?.let { ContextCompat.getDrawable(it, resId) }
        drawable?.setBounds(0, 0, 50, 50)
        val span = FileImageSpan(drawable!!, fileName)

        val start = binding.messageInput.selectionStart
        val end = binding.messageInput.selectionEnd

        val editable = binding.messageInput.editableText
        editable.replace(start, end, "")
        editable.insert(start, SpannableString(" ").apply { setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) })
    }


    private fun getTextAndExpressions() : String {
        val editable = binding.messageInput.editableText
        val spans = editable.getSpans(0, editable.length, FileImageSpan::class.java)
        var result : String = ""

        var lastSpanEnd = 0
        for (span in spans) {
            val spanStart = editable.getSpanStart(span)
            val spanEnd = editable.getSpanEnd(span)

            // 获取表情之前的文字
            if (spanStart > lastSpanEnd) {
                val textBeforeSpan = editable.subSequence(lastSpanEnd, spanStart).toString()
                result += textBeforeSpan
            }

            // 获取表情的文件名
            result += getExpressEditText(span.fileName)

            lastSpanEnd = spanEnd
        }

        // 获取最后一个表情之后的文字
        if (lastSpanEnd < editable.length) {
            val textAfterLastSpan = editable.subSequence(lastSpanEnd, editable.length).toString()
            result += (textAfterLastSpan)
        }

        return result
    }

    fun getExpressEditText(fileName: String) : String{
        return "<img src=\"/img/emote/" + fileName + "\">"
    }


    private fun hideKeyboard(){
        val imm : InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken,0)
    }

    override fun observeViewModels() {
    }

    private fun scrollToBottom() {
        chatAdapter.itemCount.takeIf { it > 0 }?.let {
            binding.rcMessageList.scrollToPosition(it - 1)
        }
    }

    private fun loadData() {
        // 加载数据并刷新适配器
        // adapter.setData(data)
        // adapter.notifyDataSetChanged()

        // 滚动到最新一条
        scrollToBottom()
    }
}