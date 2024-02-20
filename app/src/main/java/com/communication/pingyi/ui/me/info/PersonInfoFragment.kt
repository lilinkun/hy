package com.communication.pingyi.ui.me.info

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.httpdata.me.PersonInfoBean
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentInfoBinding
import com.communication.pingyi.ui.contact.orglist.OrgListFragmentArgs
import com.google.gson.Gson

/**
 * Created by LG
 * on 2022/3/23  16:42
 * Description：
 */
class PersonInfoFragment : BaseFragment<FragmentInfoBinding>() {

    private val args: PersonInfoFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.fragment_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val person = args.personal
        val personInfoBean: PersonInfoBean = Utils.deserializeFromString(person) as PersonInfoBean

        binding.let {

            it.info = personInfoBean

        }

    }

    override fun initView() {


    }

    override fun observeViewModels() {
    }
}