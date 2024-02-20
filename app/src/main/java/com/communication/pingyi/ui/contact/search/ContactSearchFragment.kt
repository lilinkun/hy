package com.communication.pingyi.ui.contact.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import com.communication.pingyi.R
import com.communication.pingyi.adapter.ContactSearchAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentSearchBinding
import com.communication.pingyi.ext.hideKeyboard
import com.communication.pingyi.ui.contact.contact.ContactViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.communication.lib_core.RecycleViewDivider
import com.communication.lib_core.tools.EVENTBUS_CONTACT_SEARCH_USER_CLICK
import com.communication.lib_http.httpdata.contact.SearchUserBean
import com.jeremyliao.liveeventbus.LiveEventBus


/**
 * Created by LG
 * on 2022/4/6  15:08
 * Description：
 */
class ContactSearchFragment : BaseFragment<FragmentSearchBinding>() {

    val mViewModel : ContactViewModel by viewModel<ContactViewModel>()

    val searchAdapter = ContactSearchAdapter()

    override fun getLayoutResId(): Int = R.layout.fragment_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LiveEventBus.get(
            EVENTBUS_CONTACT_SEARCH_USER_CLICK,
            SearchUserBean::class.java
        ).observe(this,{

            val username = it.addressUser.userName
            val phone = it.addressUser.phoneNumber
            val dept = it.addressUser.dept.deptName
            val role = it.addressUser.postJob

            val dir = ContactSearchFragmentDirections.actionContactSearchFragmentToContactDetailFragment(username,phone,role,dept)
            findNavController().navigate(dir)


        })


    }


    override fun initView() {

        binding.apply {

            rvSearch.adapter = searchAdapter

            context?.let { rvSearch.addItemDecoration(RecycleViewDivider(it,LinearLayoutManager.VERTICAL)) }


            ivContactSearchBack.setOnClickListener{
                hideKeyboard(it)
                findNavController().navigateUp()
            }

            etContactSearch.apply {
                addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        p0?.let {
                            if (it.toString().length > 0) {
                                rvSearch.visibility = View.VISIBLE
                                mViewModel.searchUser(it.toString())
                            }else{
                                rvSearch.visibility = View.INVISIBLE
                            }
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }

                })

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    }
                    false
                }
            }
        }

    }

    override fun observeViewModels() {

        with(mViewModel){
            user_info.observe(viewLifecycleOwner){
                searchAdapter.submitList(it)
                searchAdapter.notifyDataSetChanged()
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
}