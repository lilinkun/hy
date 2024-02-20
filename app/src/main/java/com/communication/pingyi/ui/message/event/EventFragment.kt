package com.communication.pingyi.ui.message.event

import android.os.Bundle
import android.view.View
import com.communication.lib_core.SpacesItemDecoration
import com.communication.lib_core.tools.EVENTBUS_LOGIN_SUCCESS
import com.communication.lib_core.tools.EVENTBUS_MESSAGE_ITEM_CLICK
import com.communication.pingyi.R
import com.communication.pingyi.adapter.MessageAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentMessageBinding
import com.communication.pingyi.ext.pyToastShort
import com.communication.pingyi.ui.message.MessageViewModel
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by LG
 * on 2022/3/16  16:37
 * Description：
 */
class EventFragment : BaseFragment<FragmentMessageBinding>() , OnRefreshListener,
    OnLoadMoreListener {

    val mViewModel : MessageViewModel by viewModel<MessageViewModel>()

    private val messageAdapter = MessageAdapter()
    private lateinit var userId : String;


    override fun getLayoutResId(): Int = R.layout.fragment_message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getMessageList()

        LiveEventBus.get(
            EVENTBUS_MESSAGE_ITEM_CLICK,
            String::class.java
        ).observe(this,{
            mViewModel.readOnlyMessage(it)
        })


        LiveEventBus.get(
            EVENTBUS_LOGIN_SUCCESS,
            Boolean::class.java
        ).observe(this,{

                if(it) {
                    mViewModel.getMessageList()
                }
        })

    }


    override fun initView() {
        binding.apply {

            rvMessage.adapter = messageAdapter
            // 设置ItemDecoration，这里设置了每个Item的上下左右间距为20像素
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp20)
            context?.let { rvMessage.addItemDecoration(
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
            messageList.observe(viewLifecycleOwner){
                messageAdapter.submitList(it)
                messageAdapter.notifyDataSetChanged()

//                it?.apply {
//                    if (it.size > 0) {
//                        userId = it.get(0).eventId
//                        unReadCount = 0
//
//                        for (messageBean: EventTodoBean in it) {
//                            if (messageBean.eventStatus.equals("untreated")) {
//                                unReadCount++
//                            }
//                        }
//                        LiveEventBus.get(EVENTBUS_UNREAD_MESSAGE).post(unReadCount)
//                    }
//
//
//                }


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
        mViewModel.getMessageList()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore(true)
        mViewModel.getMessageList()
    }

}