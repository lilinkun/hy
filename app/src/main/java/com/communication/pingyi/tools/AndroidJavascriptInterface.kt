package com.communication.pingyi.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.webkit.JavascriptInterface
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.communication.lib_core.PyAppDialog
import com.communication.lib_http.base.MMKVTool
import com.communication.pingyi.activity.LoginActivity
import com.communication.pingyi.activity.WebviewActivity
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.ui.webview.WebviewFragment

/**
 * Created by LG
 * on 2022/3/31  17:22
 * Description：
 */
class AndroidJavascriptInterface(context : Context) {
    lateinit var context : Context


    init {
        if (context != null) {
            this.context = context
        }
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

//        context?.runOnUiThread {
            //                    WebView webView = webviewActivity.getWebView();
            //
            //                    if (webView != null) {
//            context.setResult(Activity.RESULT_OK)
//            context.finish()
            //                    }
//        }
    }

    @JavascriptInterface
    fun getToken(): String {
        return MMKVTool.getToken()
    }

    @JavascriptInterface
    fun getUserName(): String {
        return MMKVTool.getUsername()
    }

//    @JavascriptInterface
//    fun getLocal(): String {
//        return webviewActivity.getLocationLL()
//    }


}

