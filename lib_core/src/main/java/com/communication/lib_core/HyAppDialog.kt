package com.communication.lib_core

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class HyAppDialog (context: Context) : AlertDialog(context, R.style.Hy_Dialog) {

    private lateinit var titleTxt: TextView
    private lateinit var messageTxt: TextView
    private lateinit var negativeBn: TextView
    private lateinit var positiveBn: TextView
    private lateinit var columnLine: View
    private var title = ""
    private var message = ""
    private var positive = ""
    private var negative = ""

    /**
     * 底部是否只有一个按钮
     */
    private var isSingle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hy_app_dialog)
        initView()
        refreshView()
    }

    /**
     * 初始化界面控件的显示数据
     */
    private fun refreshView() {
        if (title.isNotBlank()) {
            titleTxt.text = title
        }
        if (message.isNotBlank()) {
            messageTxt.text = message
        }
        if (negative.isNotBlank()) {
            negativeBn.text = negative
        }
        if (positive.isNotBlank()) {
            positiveBn.text = positive
        }
        if (isSingle) {
            columnLine.visibility = View.GONE
            negativeBn.visibility = View.GONE
        } else {
            columnLine.visibility = View.VISIBLE
            negativeBn.visibility = View.VISIBLE
        }
    }

    override fun show() {
        super.show()
        refreshView()
    }

    /**
     * 初始化界面控件
     */
    private fun initView() {
        titleTxt = findViewById(R.id.titleTxt)!!
        negativeBn = findViewById(R.id.negativeBn)!!
        positiveBn = findViewById(R.id.positiveBn)!!
        messageTxt = findViewById(R.id.messageTxt)!!
        columnLine = findViewById(R.id.columnLine)!!

        negativeBn.setOnClickListener {
            dismiss()
            clickNegativeCallBack?.callback()
        }
        positiveBn.setOnClickListener {
            dismiss()
            clickPositiveCallBack?.callback()
        }
    }

    fun setTitle(title: String): HyAppDialog {
        this.title = title
        return this
    }

    fun setMessage(message: String): HyAppDialog {
        this.message = message
        return this
    }

    fun setPositive(positive: String): HyAppDialog {
        this.positive = positive
        return this
    }

    fun setNegative(negative: String): HyAppDialog {
        this.negative = negative
        return this
    }

    fun setSingle(single: Boolean): HyAppDialog {
        isSingle = single
        return this
    }

    fun canCancel(canCancel: Boolean): HyAppDialog {
        setCanceledOnTouchOutside(canCancel)
        setCancelable(canCancel)
        return this
    }

    private var clickNegativeCallBack: IClickOnlyCallBack? = null
    fun setNegativeCallBack(clickNegativeCallBack: IClickOnlyCallBack): HyAppDialog {
        this.clickNegativeCallBack = clickNegativeCallBack
        return this
    }

    private var clickPositiveCallBack: IClickOnlyCallBack? = null
    fun setPositiveCallBack(clickPositiveCallBack: IClickOnlyCallBack): HyAppDialog {
        this.clickPositiveCallBack = clickPositiveCallBack
        return this
    }

//    BtiAppDialog(requireContext())
//    .setSingle(true)
//    .canCancel(false)
//    .setTitle(resources.getString(R.string.save_success))
//    .setMessage(resources.getString(R.string.show_document))
//    .setPositive(resources.getString(R.string.confirm))
//    .setPositiveCallBack {
//        mViewModel.getCaseMessage()
//        binding.viewPager.currentItem = 2
//    }
//    .show()
}