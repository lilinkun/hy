package com.communication.pingyi.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import com.communication.lib_core.tools.EVENTBUS_CHECK_UPDATE_VERSION
import com.communication.lib_core.tools.EVENTBUS_HOME_APPS_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_LOGIN_SUCCESS
import com.communication.lib_core.tools.Utils
import com.communication.lib_http.httpdata.home.AppsItem
import com.communication.lib_http.httpdata.home.HomeFlowBean
import com.communication.lib_http.httpdata.version.VersionModel
import com.communication.pingyi.R
import com.communication.pingyi.adapter.HomeAppListAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentHomeBinding
import com.communication.pingyi.tools.UpdateVersionTool
import com.communication.pingyi.ui.update_version.UpdateVersionViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by LG
 * on 2022/3/16  17:23
 * Description：
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnRefreshListener {

    private val mViewModel by viewModel<AppsViewModel>()
    private val mUpdateViewModel by viewModel<UpdateVersionViewModel>()


    private val mAppListAdapter = HomeAppListAdapter()


    override fun getLayoutResId(): Int = R.layout.fragment_home


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getHomeFlow()
        mViewModel.getHomeAppsList()
        mUpdateViewModel.checkUpdate(false)

        LiveEventBus.get(
            EVENTBUS_HOME_APPS_SUCCESS,
            String::class.java
        ).observe(this,{
            initHomeAppComplete()

        })

        LiveEventBus.get(
            EVENTBUS_CHECK_UPDATE_VERSION,
            VersionModel::class.java
        ).observe(this,{
            if (isActive()) {
                updateVersion(it)
            }
        })

        LiveEventBus.get(
            EVENTBUS_LOGIN_SUCCESS,
            Boolean::class.java
        ).observe(this,{

            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                if(it) {
                    mViewModel.getHomeFlow()
                    mViewModel.getHomeAppsList()
                }
            }
        })

    }
    override fun initView() {

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            appList.adapter = mAppListAdapter


           /* tvJumpThirdApp.setOnClickListener {
                val url = "qhapp://com.qh.tesla.qh/MainActivity"
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
                startActivity(intent)
            }*/

        }
        binding.refreshLayout.setEnableRefresh(true)
        binding.refreshLayout.setOnRefreshListener(this)
        
    }

    override fun observeViewModels() {
        with(mViewModel){
            appsLiveData.observe(viewLifecycleOwner){
                val appsItem : MutableList<AppsItem>? = appsLiveData.value?.children
                mAppListAdapter.submitList(appsItem)
//                mAppListAdapter.notifyDataSetChanged()
            }

            homeFlow.observe(viewLifecycleOwner){

                val cumulativeTraffic = homeFlow.value?.cumulativeTraffic?.let { it1 ->
                    Utils.numberThousandseparator(
                        it1.toInt())
                }

                val historyLowestVehicle = homeFlow.value?.historyLowestVehicle?.let {
                    Utils.numberThousandseparator(it.toInt())
                }

                val historyHighestVehicle = homeFlow.value?.historyHighestVehicle?.let {
                    Utils.numberThousandseparator(it.toInt())
                }

                val homeFlowBean = HomeFlowBean(cumulativeTraffic = cumulativeTraffic,historyLowestVehicle = historyLowestVehicle,historyHighestVehicle = historyHighestVehicle)
                binding.homeFlow = homeFlowBean
            }

            isLoading.observe(viewLifecycleOwner){
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }



    private fun initHomeAppComplete() {

//        mAppListAdapter

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh(200)
        mViewModel.getHomeFlow()
        mViewModel.getHomeAppsList()

    }


    private fun updateVersion(info: VersionModel?) {
        info?.versionCode?.let {
            val currentVersionCode = Utils.getVersionCode(requireContext())
            if (currentVersionCode >= it.toLong()) {
                return
            }
            UpdateVersionTool.update(requireActivity(), info)
        }
    }


}