package com.communication.pingyi.tools

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.communication.lib_http.base.MMKVTool
import com.communication.pingyi.activity.LoginActivity

class AndroidActivityJavascriptInterface(activity : Activity) {
    private var context : Activity


    init {
        this.context = activity
    }

    @JavascriptInterface
    fun goToLogin() {
        if (context != null) {
            val name = MMKVTool.getUsername()
            MMKVTool.clearAll()
            val intent = Intent(context, LoginActivity::class.java)
            MMKVTool.saveUsername(name)
            intent.putExtra("name",name)
            context.startActivity(intent)
            ActivityUtil.finishAll()
        }
    }

    @JavascriptInterface
    fun goBack() {
         context.runOnUiThread {

            context.setResult(Activity.RESULT_OK)
            context.finish()
        }
    }

    @JavascriptInterface
    fun getToken(): String {
        return MMKVTool.getToken()
    }

    @JavascriptInterface
    fun getUserName(): String {
        return MMKVTool.getUsername()
    }

    @JavascriptInterface
    fun getGroupId(groupId : String){
        val targetId = groupId
        val bundle = Bundle()
    }


}

