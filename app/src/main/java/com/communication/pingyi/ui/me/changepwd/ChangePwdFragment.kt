package com.communication.pingyi.ui.me.changepwd

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_CHANGE_PASSWORD_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_TOAST_STRING
import com.communication.lib_core.tools.PUBLICKEY
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.httpdata.changepwd.ChangePwdBean
import com.communication.pingyi.R
import com.communication.pingyi.activity.LoginActivity
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentChangepwdBinding
import com.communication.pingyi.tools.RSAUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

/**
 * Created by LG
 * on 2022/3/30  22:48
 * Description：
 */
class ChangePwdFragment : BaseFragment<FragmentChangepwdBinding>(){

    val mViewModel : ChangePwdViewModel  by viewModel<ChangePwdViewModel>()

    override fun getLayoutResId(): Int = R.layout.fragment_changepwd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(
            EVENTBUS_CHANGE_PASSWORD_SUCCESS,
            Boolean::class.java
        ).observe(this,{
            if (checkDoubleClick()){
                logoutConfirm()
            }
        })


    }

    override fun initView() {

        binding.apply {


            title.setBackOnClick{
                if (checkDoubleClick()){
                    findNavController().navigateUp()
                }
            }


            title.setBtnRightOnClick{
                if (checkDoubleClick()){


                    if (hyPsdOld.isBlank()) {
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入旧密码")
                        return@setBtnRightOnClick
                    }



                    if (hyPsdNew.isBlank()) {
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入新密码")
                        return@setBtnRightOnClick
                    }

                if(!hyPsdNew.isTip()){
                    LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入6-20位符合条件的新密码")
                    return@setBtnRightOnClick
                }


                if (hyPsdSureNew.isBlank()) {
                    LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请确认新密码")
                    return@setBtnRightOnClick
                }

                if (hyPsdNew.getText() != hyPsdSureNew.getText()){
                    LiveEventBus.get(EVENTBUS_TOAST_STRING).post("两次新密码不一致")
                    return@setBtnRightOnClick
                }

                val newPwd = Base64.encodeToString(
                    RSAUtils.encryptByPublicKey(hyPsdNew.getText().toString(), PUBLICKEY),
                    Base64.NO_WRAP
                )
                val oldPwd = Base64.encodeToString(
                    RSAUtils.encryptByPublicKey(hyPsdOld.getText().toString(), PUBLICKEY),
                    Base64.NO_WRAP
                )

                val changePwd  = ChangePwdBean(newPwd,oldPwd)

                mViewModel.changePwd(changePwd)


            }
            }


            /*llBack.setOnClickListener {
                if (checkDoubleClick()){
                    findNavController().navigateUp()
                }
            }

            tvModifySave.setOnClickListener {
                if (checkDoubleClick()){


                    if (evPsdOldValue.text!!.isBlank()) {
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入旧密码")
                        return@setOnClickListener
                    }

                    *//*if(evPsdOldValue.text.length < 6 || evPsdOldValue.text.length > 20){
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入6-20位的旧密码")
                        return@setOnClickListener
                    }*//*

                    if (evPsdNewValue.text!!.isBlank()) {
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入新密码")
                        return@setOnClickListener
                    }

                    if(evPsdNewValue.text.length < 6 || evPsdNewValue.text.length > 20){
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请输入6-20位的新密码")
                        return@setOnClickListener
                    }

                    val p = Pattern.compile("[0-9]*")
                    val s = Pattern.compile("[a-zA-Z]*")

                    if ( p.matcher(evPsdNewValue.text.toString()).matches()){
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("新密码不能全是数字")
                        return@setOnClickListener
                    }

                    if ( s.matcher(evPsdNewValue.text.toString()).matches()){
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("新密码不能全是字母")
                        return@setOnClickListener
                    }

                    if (evPsdSureNewValue.text!!.isBlank()) {
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("请确认新密码")
                        return@setOnClickListener
                    }

                    if (evPsdNewValue.text.toString() != evPsdSureNewValue.text.toString()){
                        LiveEventBus.get(EVENTBUS_TOAST_STRING).post("两次新密码不一致")
                        return@setOnClickListener
                    }

                    val newPwd = Base64.encodeToString(
                        RSAUtils.encryptByPublicKey(evPsdNewValue.text.toString(), PUBLICKEY),
                        Base64.NO_WRAP
                    )
                    val oldPwd = Base64.encodeToString(
                        RSAUtils.encryptByPublicKey(evPsdOldValue.text.toString(), PUBLICKEY),
                        Base64.NO_WRAP
                    )

                    val changePwd  = ChangePwdBean(newPwd,oldPwd)

                    mViewModel.changePwd(changePwd)


                }
            }*/


        }

    }

    override fun observeViewModels() {
        with(mViewModel){
            isSuccess.observe(viewLifecycleOwner){
                if (it){

                    val name = MMKVTool.getUsername()
                    MMKVTool.clearAll()
                    val intent = Intent(activity, LoginActivity::class.java)
                    MMKVTool.saveUsername(name)
                    intent.putExtra("name",name)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }



    private fun logoutConfirm() {
        MMKVTool.clearAll()
        requireActivity().finish()
    }


}