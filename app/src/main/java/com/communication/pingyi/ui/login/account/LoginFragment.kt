package com.communication.pingyi.ui.login.account

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.communication.lib_core.EditTextYH
import com.communication.lib_core.SomeMonitorEditText
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_LOGIN_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_TOKEN_SUCCESS
import com.communication.lib_core.tools.PUBLICKEY
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.api.SERVER_BASE_URL
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.httpdata.login.LoginInfo
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentLoginBinding
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.tools.RSAUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


/**
 * Created by LG
 * on 2022/3/10  16:14
 * Description：
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    val mLoginViewModel : LoginViewModel by sharedViewModel<LoginViewModel>()

    lateinit var mLoginInfo : LoginInfo;
    lateinit var sign : String;

    lateinit var someMonitor : SomeMonitorEditText

    lateinit var etPassword : EditTextYH
    lateinit var ivPassword : ImageView

    override fun getLayoutResId(): Int = R.layout.fragment_login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(
            EVENTBUS_TOKEN_SUCCESS,
            Boolean::class.java
        ).observe(this){
            if (lifecycle.currentState == Lifecycle.State.RESUMED){
                if (it){
                    mLoginViewModel.createOrGetAuthorization(mLoginInfo)
                }
            }
        }

        LiveEventBus.get(
            EVENTBUS_LOGIN_SUCCESS,
            Boolean::class.java
        ).observe(this){

            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                if(it) {
                    goToMainActivity()
                }
            }
        }

    }

    private fun goToMainActivity() {
        val intent = Intent("com.communication.pingyi.main")
        startActivity(intent)
        activity?.finish()
    }


    override fun initView() {

        binding.let {

//            showVcode()

            MMKVTool.getUsername()?.apply {
                it.etUsername.setText(MMKVTool.getUsername())
            }

            it.btnLogin.setOnClickListener{

                if (checkDoubleClick(1000)) {
                    clickLogin()
                }

            }

            it.ivVerificationCode.setOnClickListener{
                if (checkDoubleClick(1000)){

//                    showVcode()
                }
            }

            val edits = LinkedList<EditTextYH>()

            edits.add(it.etUsername)
            edits.add(it.etPassword)

            etPassword = it.etPassword
            ivPassword = it.ivPassword

            someMonitor = SomeMonitorEditText(it.btnLogin,edits)

            ivPassword.setOnClickListener {
                if (etPassword.getTransformationMethod() is PasswordTransformationMethod) {
                    // 密码不可见，变为可见
                    etPassword.setTransformationMethod(null)
                    ivPassword.setImageResource(R.drawable.ic_show_psd)

                } else {
                    // 密码可见，变为不可见
                    etPassword.setTransformationMethod(PasswordTransformationMethod())
                    ivPassword.setImageResource(R.drawable.ic_hidden_psd)
                }

                // 光标定位到文本末尾，便于继续输入

                // 光标定位到文本末尾，便于继续输入
                etPassword.getText()?.let { it1 -> etPassword.setSelection(it1.length) }
            }

        }

    }

    private fun clickLogin() {
        if (isActive()) {
            checkLogin()
        }
    }

    private fun showVcode(){

        sign = Date().time.toString() + ""

        Glide.with(this).load(SERVER_BASE_URL+"/code?sign=" + sign).into(binding.ivVerificationCode)
    }

    override fun observeViewModels() {

        with(mLoginViewModel) {
            isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }


    private fun checkLogin() {
        binding.apply {
//            if (etUsername.text!!.isBlank()) {
//                pyToast("请输入手机号")
//                return
//            }
//            if (etUsername.text!!.length != 11) {
//                pyToast("请检查手机号是否正确")
//                return
//            }
            if (etPassword.text!!.isBlank()) {
                pyToast("请输入密码")
                return
            }

            val uname = etUsername.text.toString().trim()
            val pwd = etPassword.text.toString().trim()

            val psd = Base64.encodeToString(
                RSAUtils.encryptByPublicKey(pwd, PUBLICKEY),
                NO_WRAP
            )

//            val psd = Base64.encodeToString(pwd.toByteArray(charset("utf-8")), NO_WRAP)
            val uuid = ""
            val clientType = "1"

            val currentVersionCode = context?.let { Utils.getVersionCode(it).toString() }

            val brand = Utils.deviceBrand + Utils.systemModel

            mLoginInfo = LoginInfo(
                brand, uname,psd,
                context?.let { Utils.getDeviceId(it) }, currentVersionCode)

            LiveEventBus.get(EVENTBUS_TOKEN_SUCCESS).post(true)
        }
    }


}