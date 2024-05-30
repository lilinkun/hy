package com.communication.pingyi.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.communication.lib_core.TitleBar
import com.communication.pingyi.R

open class ConversationBaseAcitivity : AppCompatActivity(){

    protected lateinit var mTitleBar: TitleBar
    protected lateinit var mContentView : ViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.setContentView(R.layout.activity_conversation_base)
        this.mTitleBar = findViewById<TitleBar>(R.id.rc_title_bar)
        this.mContentView = findViewById<ViewFlipper>(R.id.rc_base_container)
        this.mTitleBar.setOnBackClickListener(object : TitleBar.OnBackClickListener {
            override fun onBackClick() {
                this@ConversationBaseAcitivity.finish()
            }
        })
        this.mTitleBar.setBackgroundColor(this.resources.getColor(R.color.rc_white_color))
    }


    override fun setContentView(resId: Int) {
        val view = LayoutInflater.from(this).inflate(resId, null as ViewGroup?)
        val lp = LinearLayout.LayoutParams(-1, -1, 1.0f)
        mContentView.addView(view, lp)
    }




}