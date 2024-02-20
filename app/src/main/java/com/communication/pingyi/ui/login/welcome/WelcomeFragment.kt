package com.communication.pingyi.ui.login.welcome

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.communication.lib_http.base.MMKVTool
import com.communication.pingyi.R
import com.communication.pingyi.base.AppContext.getAssets
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentWelcomeBinding


/**
 * Created by LG
 * on 2022/3/14  14:09
 * Description：
 */
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(){

    lateinit var alphaObjectAnimator : ObjectAnimator

    override fun getLayoutResId(): Int = R.layout.fragment_welcome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
            super.onStart()
            binding.apply {
                    alphaObjectAnimator = ObjectAnimator.ofFloat(rlWelcome, "alpha", 1f, 0.3f)
                    alphaObjectAnimator.duration = 3000
                    alphaObjectAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                        }
                        override fun onAnimationEnd(animation: Animator?) {
                            if (MMKVTool.getUsername().isBlank() || MMKVTool.getPassword().isBlank()) {
                                navigateToLogin()
                            } else {
                                goToMainActivity()
                            }
                        }

                        override fun onAnimationCancel(animation: Animator?) {}
                        override fun onAnimationRepeat(animation: Animator?) {}
                    })

                    alphaObjectAnimator.startDelay = 500

                    alphaObjectAnimator.start()

//                    invalidateAll()

            }




    }

    override fun observeViewModels() {

    }


    private fun navigateToLogin() {
        val dir = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
        findNavController().navigate(dir)
    }



    private fun goToMainActivity() {
        val intent = Intent("com.communication.pingyi.main")
        requireContext().startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun initView() {

    }

}