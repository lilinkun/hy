package com.communication.pingyi.ui.me.about

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.communication.lib_core.tools.EVENTBUS_CHECK_UPDATE_VERSION_BUTTON
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.httpdata.version.VersionModel
import com.communication.pingyi.R
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentAboutBinding
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.ext.pyToastShort
import com.communication.pingyi.tools.UpdateVersionTool
import com.communication.pingyi.ui.update_version.UpdateVersionViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by LG
 * on 2022/4/8  14:43
 * Description：
 */
class AboutFragment : BaseFragment<FragmentAboutBinding>() {

//    val aViewModel : AboutViewModel by viewModel<AboutViewModel>()
    private val mUpdateVersionViewModel by viewModel<UpdateVersionViewModel>()

    lateinit var versionModel : VersionModel
    var isUpdate = false

    override fun getLayoutResId(): Int = R.layout.fragment_about

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUpdateVersionViewModel.checkUpdate(true)

        LiveEventBus.get(
            EVENTBUS_CHECK_UPDATE_VERSION_BUTTON,
            VersionModel::class.java
        ).observe(this,{
            updateVersion(it,true)
            versionModel = it
        })

    }

    override fun initView() {

        binding.apply {
            tvAboutVersion.text = "Version " + context?.let { Utils.getVersionName(it) }

            tvAboutTitle.setOnClickListener {

                findNavController().navigateUp()

            }

            tvCheckUpdate.setOnClickListener {
                if (isActive()) {
                    if (isUpdate) {
                        UpdateVersionTool.update(requireActivity(), versionModel)
                    }else{
                        pyToast(resources.getString(R.string.update_version_latest))
                    }
                }
            }

//            tvMeAbout.showCircle(true)

        }

    }

    override fun observeViewModels() {

        with(mUpdateVersionViewModel){

            isLoading.observe(viewLifecycleOwner){
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

        }

    }

    private fun updateVersion(info: VersionModel?, isBtnClick: Boolean = false) {
        if (info == null) {
            if (isBtnClick) {
                pyToastShort(resources.getString(R.string.update_version_latest))
            }
            return
        }
        info.versionCode?.let {
            val currentVersionCode = Utils.getVersionCode(requireContext())
            if (currentVersionCode >= it.toLong()) {
                if (isBtnClick) {
//                    pyToast(resources.getString(R.string.update_version_latest))
                }
                return
            }else{
                isUpdate = true
                binding.tvCheckUpdate.showCircle(true)
            }
        }
    }
}