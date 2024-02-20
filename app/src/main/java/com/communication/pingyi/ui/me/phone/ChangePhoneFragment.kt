package com.communication.pingyi.ui.me.phone

import com.communication.lib_core.checkDoubleClick
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentChangePhoneBinding
import com.communication.pingyi.ext.pyToast


class ChangePhoneFragment : BaseFragment<FragmentChangePhoneBinding>() {
    override fun getLayoutResId(): Int = R.layout.fragment_change_phone

    override fun initView() {


        binding.apply {


            title.setBtnRightOnClick {
                if (checkDoubleClick()) {
                    pyToast("asdasda")
                }
            }


        }


    }

    override fun observeViewModels() {
    }
}