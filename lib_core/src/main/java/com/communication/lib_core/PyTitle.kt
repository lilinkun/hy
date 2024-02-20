package com.communication.lib_core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Created by LG
 * on 2022/3/23  16:43
 * Description：
 */
class PyTitle : RelativeLayout {
    private lateinit var titleName: TextView
    private lateinit var btnRight: TextView
    private lateinit var btnBack: RelativeLayout
    private lateinit var btnIcon: RelativeLayout

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PyTitle)
        val text = ta.getString(R.styleable.PyTitle_py_title_name)
        val rightText = ta.getString(R.styleable.PyTitle_py_next_text)
        val showBack = ta.getBoolean(R.styleable.PyTitle_py_title_showBack, false)
        ta.recycle()
        text?.apply {
            titleName.text = this
        }
        rightText?.apply {
            btnRight.text = this
            btnRight.visibility = View.VISIBLE
        }
        if (showBack) {
            btnBack.visibility = View.VISIBLE
        } else {
            btnBack.visibility = View.INVISIBLE
        }
    }

    constructor(context : Context) : super(context){
        init(context)
    }

    private fun init(context: Context) {
        (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater).inflate(R.layout.py_title,this)
        titleName = findViewById(R.id.txt_title)
        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackClickListener?.callback()
        }
        btnIcon = findViewById(R.id.btn_icon)
        btnIcon.setOnClickListener {
            onIconClickListener?.callback()
        }
        btnRight = findViewById(R.id.btnRight)
        btnRight.setOnClickListener {
            onBtnRightClickListener?.callback()
        }

    }
    fun setTitle(text: String?) {
        titleName.text = text
    }

    fun getTitle():String {
        return titleName.text.toString()
    }

    fun setBtnRight(text: String) {
        btnRight.text = text
    }

    fun setBtnRightShow(b: Boolean) {
        if (b) {
            btnRight.visibility = View.VISIBLE
        } else {
            btnRight.visibility = View.GONE
        }
    }

    private var onBackClickListener: IClickOnlyCallBack? = null
    fun setBackOnClick(onBackClickListener: IClickOnlyCallBack) {
        btnBack.visibility = View.VISIBLE
        this.onBackClickListener = onBackClickListener
    }

    private var onIconClickListener: IClickOnlyCallBack? = null
    fun setIconOnClick(onIconClickListener: IClickOnlyCallBack) {
        btnIcon.visibility = View.VISIBLE
        this.onIconClickListener = onIconClickListener
    }

    private var onBtnRightClickListener: IClickOnlyCallBack? = null
    fun setBtnRightOnClick(onBtnRightClickListener: IClickOnlyCallBack) {
        btnRight.visibility = View.VISIBLE
        this.onBtnRightClickListener = onBtnRightClickListener
    }

    fun reset() {
        btnRight.visibility = View.GONE
        btnRight.text = ""
        onBtnRightClickListener = null
    }


}