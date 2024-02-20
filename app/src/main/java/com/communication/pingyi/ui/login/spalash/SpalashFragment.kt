package com.communication.pingyi.ui.login.spalash

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class SpalashFragment : Fragment(){

    var isLogin = false

    val args : SpalashFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            if(args.relogin){
                isLogin = true
            }

            if(isLogin){
                navigateToLogin()
            }else {
                navigateToWelcome()
            }
        }


        private fun navigateToLogin() {
            val dir = SpalashFragmentDirections.actionSpalashFragmentToLoginFragment()
            findNavController().navigate(dir)
        }


        private fun navigateToWelcome(){
            val dir = SpalashFragmentDirections.actionSpalashFragmentToWelcomeFragment()
            findNavController().navigate(dir)
        }
}