package com.communication.lib_core

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSeekBar

/**
 * Created by LG
 * on 2022/4/12  9:26
 * Description：
 */
class PyUpdateDialog(context: Context) : AlertDialog(context, R.style.Py_Dialog) {

    private lateinit var titleTxt: TextView
    private lateinit var messageTxt: TextView
    private lateinit var negativeBn: TextView
    private lateinit var positiveBn: TextView
    private lateinit var columnLine: View
    private lateinit var layoutConfirm: View
    private lateinit var layoutProgress: View
    private lateinit var seekBar: AppCompatSeekBar
    private lateinit var progressPercent: TextViewYH
    private lateinit var progressWait: View
    private lateinit var progressPrepare: TextViewYH

    private var title = ""
    private var message = ""
    private var positive = ""
    private var negative = ""

    private var isSingle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.py_update_dialog)
        initView()
        refreshView()
    }

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
        init()
        refreshView()
    }

    private fun initView() {
        titleTxt = findViewById(R.id.titleTxt)!!
        negativeBn = findViewById(R.id.negativeBn)!!
        positiveBn = findViewById(R.id.positiveBn)!!
        messageTxt = findViewById(R.id.messageTxt)!!
        columnLine = findViewById(R.id.columnLine)!!
        layoutConfirm = findViewById(R.id.layoutConfirm)!!
        layoutProgress = findViewById(R.id.layoutProgress)!!
        seekBar = findViewById(R.id.seekBar)!!
        progressPercent = findViewById(R.id.progressPercent)!!

        progressWait = findViewById(R.id.progressWait)!!
        progressPrepare = findViewById(R.id.progressPrepare)!!


        negativeBn.setOnClickListener {
            dismiss()
            clickNegativeCallBack?.callback()
        }
        positiveBn.setOnClickListener {
            layoutConfirm.visibility = View.GONE
            layoutProgress.visibility = View.VISIBLE
            clickPositiveCallBack?.callback()
        }
    }

    fun setTitle(title: String): PyUpdateDialog {
        this.title = title
        return this
    }

    fun setMessage(message: String): PyUpdateDialog {
        this.message = message
        return this
    }

    fun setPositive(positive: String): PyUpdateDialog {
        this.positive = positive
        return this
    }

    fun setNegative(negative: String): PyUpdateDialog {
        this.negative = negative
        return this
    }

    fun setSingle(single: Boolean): PyUpdateDialog {
        isSingle = single
        return this
    }

    fun canCancel(canCancel: Boolean): PyUpdateDialog {
        setCanceledOnTouchOutside(canCancel)
        setCancelable(canCancel)
        return this
    }

    private var clickNegativeCallBack: IClickOnlyCallBack? = null
    fun setNegativeCallBack(clickNegativeCallBack: IClickOnlyCallBack): PyUpdateDialog {
        this.clickNegativeCallBack = clickNegativeCallBack
        return this
    }

    private var clickPositiveCallBack: IClickOnlyCallBack? = null
    fun setPositiveCallBack(clickPositiveCallBack: IClickOnlyCallBack): PyUpdateDialog {
        this.clickPositiveCallBack = clickPositiveCallBack
        return this
    }

    private fun init() {
        layoutConfirm.visibility = View.VISIBLE
        layoutProgress.visibility = View.GONE
        progressPercent.visibility = View.GONE
        progressWait.visibility = View.VISIBLE
        progressPrepare.text = context.getString(R.string.update_version_prepare)
        seekBar.progress = 0
        seekBar.max = 100
    }

    fun setProgress(i: Int) {
        seekBar.progress = i
        if (i < 15) {
            progressPercent.visibility = View.GONE
            progressWait.visibility = View.VISIBLE
        } else {
            progressPrepare.text = context.getString(R.string.update_version_download)
            progressPercent.visibility = View.VISIBLE
            progressPercent.text = "${i}%"
        }

    }

}