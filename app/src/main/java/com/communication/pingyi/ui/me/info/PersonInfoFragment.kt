package com.communication.pingyi.ui.me.info

import androidx.navigation.fragment.navArgs
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentInfoBinding
import com.google.gson.Gson

/**
 * Created by LG
 * on 2022/3/23  16:42
 * Description：
 */
class PersonInfoFragment : BaseFragment<FragmentInfoBinding>() {

    private val args: PersonInfoFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.fragment_info

    override fun initView() {

        val person = args.personal
        val personInfoBean: PersonInfoBean = Gson().fromJson(person,PersonInfoBean::class.java)

        binding.let {

            it.infoName.setContent(personInfoBean.userName)

            it.infoUserName.setContent(personInfoBean.nickName)

        }

    }

    override fun observeViewModels() {
    }
}