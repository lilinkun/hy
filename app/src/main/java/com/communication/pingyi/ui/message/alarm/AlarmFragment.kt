package com.communication.pingyi.ui.message.alarm

import android.os.Bundle
import android.view.View
import com.communication.lib_core.SpacesItemDecoration
import com.communication.lib_http.httpdata.message.AlarmRequestBean
import com.communication.pingyi.R
import com.communication.pingyi.adapter.AlarmAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentAlarmBinding
import com.communication.pingyi.ext.pyToastShort
import com.communication.pingyi.ui.message.HomeMessageFragment
import com.communication.pingyi.ui.message.MessageViewModel
import com.google.android.material.tabs.TabLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class AlarmFragment : BaseFragment<FragmentAlarmBinding>() , OnRefreshListener,
    OnLoadMoreListener {

    val mViewModel : MessageViewModel by viewModel<MessageViewModel>()

    private val alarmAdapter = AlarmAdapter()

    var alarmRequestBean : AlarmRequestBean? = null


    val list: MutableList<String> = mutableListOf()

    private val parentFragment: HomeMessageFragment? by lazy {
        parentFragment as? HomeMessageFragment
    }


    override fun getLayoutResId(): Int = R.layout.fragment_alarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list.add("S0090HS0012700001")

//        AlarmRequestBean alarmRequestBean = new AlarmRequestBean();

        // 获取TabLayout
       /* val tabLayout = parentFragment?.view?.findViewById<TabLayout>(R.id.tabs)
        // 修改第一个Tab的Badge
        val tab = tabLayout?.getTabAt(0)
        tab?.let {
            val badge = it.orCreateBadge
            badge.isVisible = true
            badge.number = 2
        }*/
        alarmRequestBean = AlarmRequestBean("S0090HS0012700001",1,200)

        if (alarmRequestBean != null) {
            mViewModel.getAlarmList(alarmRequestBean!!)
        }
    }

    override fun initView() {

        binding.apply {

            rvAlarm.adapter = alarmAdapter
            // 设置ItemDecoration，这里设置了每个Item的上下左右间距为20像素
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp20)
            context?.let { rvAlarm.addItemDecoration(
                SpacesItemDecoration(spacingInPixels)
            ) }


        }

        binding.refreshLayout.setEnableRefresh(true)
        binding.refreshLayout.setEnableLoadMore(true)
        binding.refreshLayout.setOnRefreshListener(this)
        binding.refreshLayout.setOnLoadMoreListener(this)

    }

    override fun observeViewModels() {

        with(mViewModel){
            alarmList.observe(viewLifecycleOwner){
                alarmAdapter.submitList(it)
                alarmAdapter.notifyDataSetChanged()

            }



            messageError.observe(viewLifecycleOwner){
                pyToastShort(it)
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


    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh(200)
        if (alarmRequestBean != null) {
            mViewModel.getAlarmList(alarmRequestBean!!)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore(true)
        if (alarmRequestBean != null) {
            mViewModel.getAlarmList(alarmRequestBean!!)
        }
    }

}