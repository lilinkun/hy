package com.communication.pingyi.ui.contact.contact

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.communication.lib_core.RecycleViewDivider
import com.communication.lib_core.tools.EVENTBUS_CONTACT_CLICK
import com.communication.lib_core.tools.EVENTBUS_LOGIN_SUCCESS
import com.communication.lib_http.httpdata.contact.ContactItem
import com.communication.pingyi.R
import com.communication.pingyi.adapter.ContactAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentContactsBinding
import com.communication.pingyi.ui.main.MainFragmentDirections
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by LG
 * on 2022/3/17  14:11
 * Description：
 */
class ContactFragment : BaseFragment<FragmentContactsBinding>(), OnRefreshListener {

    val mViewModel : ContactViewModel by viewModel<ContactViewModel>()
    lateinit var orgList : MutableList<ContactItem>

    private val mContactAdapter = ContactAdapter()


    override fun getLayoutResId(): Int = R.layout.fragment_contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getContactList()

        LiveEventBus.get(
            EVENTBUS_CONTACT_CLICK,
            ContactItem::class.java
        ).observe(this,{
            if (isActive()) {
                val dir = MainFragmentDirections.actionMainFragmentToOrgListFragment(it.id.toString(),it.label)
                findNavController().navigate(dir)
            }
        })

        LiveEventBus.get(
            EVENTBUS_LOGIN_SUCCESS,
            Boolean::class.java
        ).observe(this,{

            if(it) {
                mViewModel.getContactList()
            }
        })

    }

    override fun initView() {

        binding.apply {

            rvContact.adapter = mContactAdapter
            
            context?.let { rvContact.addItemDecoration(
                RecycleViewDivider(it,
                    LinearLayoutManager.VERTICAL)
            ) }

            tvContactSearch.setOnClickListener {
                val dir = MainFragmentDirections.actionMainFragmentToContactSearchFragment()
                findNavController().navigate(dir)
            }

        }

        binding.refreshLayout.setEnableRefresh(true)
        binding.refreshLayout.setOnRefreshListener(this)
    }

    override fun observeViewModels() {
        with(mViewModel){
            org_list.observe(viewLifecycleOwner){
                orgList = it
                mContactAdapter.submitList(orgList)
                mContactAdapter.notifyDataSetChanged()

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
        mViewModel.getContactList()
    }

}