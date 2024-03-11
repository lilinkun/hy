package com.communication.pingyi.ui.contact.ContactDetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.communication.lib_http.httpdata.contact.ContactUserBean
import com.communication.pingyi.R
import com.communication.pingyi.activity.ChatAcitivity
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentContactDetailBinding
import com.communication.pingyi.ext.pyToast
import com.google.gson.Gson
import io.rong.callkit.RongCallKit
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.ConversationIdentifier
import java.util.Locale

class ContactDetailFragment : BaseFragment<FragmentContactDetailBinding>(){

    override fun getLayoutResId(): Int = R.layout.fragment_contact_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivity()?.getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    override fun initView() {

        val phone = arguments?.getString("phone")
        val username = arguments?.getString("username")
        val role = arguments?.getString("role")
        val dept = arguments?.getString("dept")


        val contactBean = Gson().fromJson(role,ContactUserBean::class.java)



        binding.apply{
            if (phone != null) {
                includeInfo.infoPhone.setContent(phone)
            }
            if (dept != null) {
                includeInfo.infoOrganization.setContent(dept)
            }

            if (role != null) {
                includeInfo.infoJob.setContent(contactBean.postJob)
            }
            includeInfo.infoPhone.setShowIcon(true)
            includeInfo.infoOrganization.setShowIcon(true)
            includeInfo.infoJob.setShowIcon(true)

            includeInfo.infoPhone.setShowImg(true)
            includeInfo.infoOrganization.setShowImg(true)
            includeInfo.infoJob.setShowImg(true)

            tvUsername.setText(username)
            tvPersonal.setText(username?.substring(1, username.length))

            ivContactPhone.setOnClickListener {

                val intent = Intent()
                intent.action  =  Intent.ACTION_DIAL
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }

            ivContactVoice.setOnClickListener {
                if (!contactBean.sysUserName.isNullOrEmpty()) {
                    RongCallKit.startSingleCall(
                        activity,
                        contactBean.sysUserName,
                        RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO
                    );
                }else{
                    pyToast("此人无即时通讯功能")
                }
            }

            ivContactVideo.setOnClickListener {
                if (!contactBean.sysUserName.isNullOrEmpty()) {
                    RongCallKit.startSingleCall(
                        activity,
                        contactBean.sysUserName,
                        RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO
                    );
                }else{
                    pyToast("此人无即时通讯功能")
                }
            }


            ivContactMessage.setOnClickListener {
                if (!contactBean.sysUserName.isNullOrEmpty()) {
                    val targetId = contactBean.sysUserName;
                    val bundle = Bundle()
                    val conversationIdentifier = ConversationIdentifier(Conversation.ConversationType.PRIVATE, targetId);

                    val intent: Intent = Intent(context, ChatAcitivity::class.java)
                    intent.putExtra("targetId", conversationIdentifier.targetId)
                    intent.putExtra(
                        "ConversationType", conversationIdentifier.type.getName().lowercase(
                            Locale.getDefault()
                        )
                    )
                    intent.putExtra("ConversationIdentifier", conversationIdentifier)
                    if (bundle != null) {
                        intent.putExtras(bundle)
                    }

                    startActivity(intent)

//                    RouteUtils.routeToConversationActivity(context, conversationIdentifier, false, bundle)
                }else{
                    pyToast("此人无即时通讯功能")
                }
            }

            ivSendSms.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SENDTO
                intent.data = Uri.parse("smsto:$phone")
                startActivity(intent)
            }

            icBack.setOnClickListener {
                findNavController().navigateUp()
            }


            includeInfo.infoSex.visibility = View.VISIBLE
            if (contactBean.sex == 0){
                includeInfo.tvInfoSex.setContent("男")
            }else{
                includeInfo.tvInfoSex.setContent("女")
            }

            includeInfo.infoType.visibility = View.VISIBLE

        }



    }

    override fun onDestroy() {
        super.onDestroy()
        getActivity()?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun observeViewModels() {

    }
}