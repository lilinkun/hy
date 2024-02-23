package com.communication.pingyi.ui.me.about.introduction

import androidx.navigation.fragment.findNavController
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentIntroductionBinding

class IntroductionFragment : BaseFragment<FragmentIntroductionBinding>(){

    override fun getLayoutResId(): Int = R.layout.fragment_introduction

    override fun initView() {

        binding.apply {

            titleIntroduction.setBackOnClick{
                findNavController().navigateUp()
            }

        }

    }

    override fun observeViewModels() {

    }
}