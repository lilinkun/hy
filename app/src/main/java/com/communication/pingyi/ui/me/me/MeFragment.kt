package com.communication.pingyi.ui.me.me

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.communication.lib_core.PyAppDialog
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_LOGIN_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_LOGOUT_SUCCESS
import com.communication.lib_http.base.MMKVTool
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.pingyi.R
import com.communication.pingyi.activity.LoginActivity
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentMeBinding
import com.communication.pingyi.ui.main.MainFragmentDirections
import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import io.rong.imkit.RongIM
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by LG
 * on 2022/3/17  11:22
 * Description：
 */
class MeFragment : BaseFragment<FragmentMeBinding>(), OnRefreshListener {

    private val meViewModel by viewModel<MeViewModel>()

    private var personalInfo : PersonInfoBean? = null

    override fun getLayoutResId(): Int = R.layout.fragment_me

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(
            EVENTBUS_LOGOUT_SUCCESS,
            Boolean :: class.java
        ).observe(this){
            if (lifecycle.currentState == Lifecycle.State.RESUMED){
                if (it){
                    logoutConfirm()
                }
            }
        }


        LiveEventBus.get(
            EVENTBUS_LOGIN_SUCCESS,
            Boolean::class.java
        ).observe(this){

            if(it) {
                meViewModel.getInfo()
            }
        }

        meViewModel.getInfo()


    }




    override fun initView() {

        binding.apply {

            meLogout.setOnClickListener {

                if (checkDoubleClick()) {
                    logout()
                }

            }

            tvMeChangePwd.setOnClickListener {
                if (checkDoubleClick()){
                    val dir = MainFragmentDirections.actionMainFragmentToChangePwdFragment()
                    findNavController().navigate(dir)
                }
            }

            tvMeAbout.setOnClickListener {
                if (checkDoubleClick()){
                    val dir = MainFragmentDirections.actionMainFragmentToAboutFragment()
                    findNavController().navigate(dir)
                }
            }

//            includeInfo.infoPhone.setOnClickListener{
//                if (checkDoubleClick()){
//                    val dir = MainFragmentDirections.actionMainFragmentToChangePhoneFragment()
//                    findNavController().navigate(dir)
//                }
//            }

            rlUserinfo.setOnClickListener {
                if (checkDoubleClick()){
                    personalInfo?.let {
//                        val personStr = Utils.serializeToString(it)
                        val personStr = Gson().toJson(it)
                        val dir = MainFragmentDirections.actionMainFragmentToPersonInfoFragment(personStr)
                        findNavController().navigate(dir)
                    }
                }
            }


            includeInfo.infoPhone.setShowIcon(true)
            includeInfo.infoOrganization.setShowIcon(true)
            includeInfo.infoJob.setShowIcon(true)

            /*meInfo.setOnClickListener {

                if (checkDoubleClick()) {
                    val dir = MainFragmentDirections.actionMainFragmentToPersonInfoFragment()
                    findNavController().navigate(dir)
                }

            }*/

        }
        binding.refreshLayout.setEnableRefresh(true)
        binding.refreshLayout.setOnRefreshListener(this)

    }

    @SuppressLint("SetTextI18n")
    override fun observeViewModels() {

        with(meViewModel){
            isLoading.observe(viewLifecycleOwner){
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

            personInfo.observe(viewLifecycleOwner){
                it?.let {
                    binding.apply {
                        personalInfo = it
                        info = it
                        includeInfo.infoPhone.setContent(it.phonenumber)
                        includeInfo.infoOrganization.setContent(it.dept.deptName)
                        if(it.roles.size > 0) {
                            includeInfo.infoJob.setContent(it.roles[0].roleName)
                        }
                        tvUsername.text = resources.getText(R.string.username_tip).toString() + it.userName
                        tvPersonal.text = it.nickName.substring( 1,it.nickName.length)
                    }
                }
            }

        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh(200)
        meViewModel.getInfo()
    }


    /**
     * 退出登陆
     */
    private fun logout() {
        PyAppDialog(requireContext())
            .setSingle(false)
            .canCancel(true)
            .setTitle(resources.getString(R.string.me_logout))
            .setMessage(resources.getString(R.string.me_logout_confirm))
            .setPositive(resources.getString(R.string.sure))
            .setPositiveCallBack {
                meViewModel.logout()
            }
            .show()
    }


    private fun logoutConfirm() {
        RongIM.getInstance().logout()
        MMKVTool.clearAll()
        val intent = Intent(context,LoginActivity::class.java)
        intent.putExtra("name","")
        startActivity(intent)
        requireActivity().finish()
    }

}