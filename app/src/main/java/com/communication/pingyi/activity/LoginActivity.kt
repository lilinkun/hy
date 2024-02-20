package com.communication.pingyi.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseActivity
import com.communication.pingyi.ui.login.spalash.SpalashFragmentArgs

/**
 * Created by LG
 * on 2022/3/14  11:22
 * Description：
 */
class LoginActivity : BaseActivity() {

    var relogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra("name")?.let {
            relogin = true
        }

        setContentView(R.layout.activity_login)


    }

    override fun onStart() {
        super.onStart()

        val args = SpalashFragmentArgs(relogin = relogin)
        val nav = Navigation.findNavController(this,R.id.nav_host)
        nav.setGraph(R.navigation.nav_login,args.toBundle())
    }

    override fun onBackPressed() {
        finish()
    }

}