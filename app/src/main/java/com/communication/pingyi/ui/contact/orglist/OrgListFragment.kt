package com.communication.pingyi.ui.contact.orglist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.communication.lib_core.RecycleViewDivider
import com.communication.lib_core.tools.EVENTBUS_CONTACT_CLICK
import com.communication.lib_core.tools.EVENTBUS_CONTACT_ORG_CLICK
import com.communication.lib_core.tools.EVENTBUS_CONTACT_USER_CLICK
import com.communication.lib_http.httpdata.contact.ContactItem
import com.communication.lib_http.httpdata.contact.ContactUserBean
import com.communication.pingyi.R
import com.communication.pingyi.adapter.ContactAdapter
import com.communication.pingyi.adapter.ContactUserAdapter
import com.communication.pingyi.adapter.OrgListAdapter
import com.communication.pingyi.base.BaseFragment
import com.communication.pingyi.databinding.FragmentOrglistBinding
import com.communication.pingyi.ext.pyToast
import com.communication.pingyi.ui.contact.contact.ContactViewModel
import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by LG
 * on 2022/4/1  10:24
 * Description：
 */
class OrgListFragment : BaseFragment<FragmentOrglistBinding>(){

    private val args: OrgListFragmentArgs by navArgs()
    val mViewModel : ContactViewModel by viewModel<ContactViewModel>()

    lateinit var orgList : MutableList<ContactItem>
    lateinit var userList : MutableList<ContactUserBean>
    val contactItems : MutableList<ContactItem> = mutableListOf()
    var contactItem : ContactItem? = null

    val mContactItemAdapter = ContactAdapter()
    val mContactUserAdapter = ContactUserAdapter()
    val mContactOrgAdapter = OrgListAdapter()

    override fun getLayoutResId(): Int = R.layout.fragment_orglist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getContactUser(args.id)


        contactItem = ContactItem(args.id.toInt(),args.title,args.id.toInt())

        notice(contactItem)

        LiveEventBus.get(
            EVENTBUS_CONTACT_USER_CLICK,
            ContactUserBean::class.java
        ).observe(this,{

            //注意必须是javaObjectType，要不会被擦除泛型
            val bean = Gson().toJson(it)

            val dir = OrgListFragmentDirections.actionOrgListFragmentToContactDetailFragment(it.userName,it.phoneNumber,bean,it.dept.deptName)
            findNavController().navigate(dir)
        })

        LiveEventBus.get(
            EVENTBUS_CONTACT_ORG_CLICK,
            ContactItem::class.java
        ).observe(this,{
            mViewModel.getContactUser(it.id.toString())
            contactItems.removeAt(contactItems.indexOf(it)+1)
            mContactOrgAdapter.notifyDataSetChanged()
        })


        LiveEventBus.get(
            EVENTBUS_CONTACT_CLICK,
            ContactItem::class.java
        ).observe(this,{
            if (isActive()) {
                if (it.size > 0) {
                    contactItems.add(it)
                    mContactOrgAdapter.notifyDataSetChanged()
                    mViewModel.getContactUser(it.id.toString())
                }else{
                    pyToast("部门暂未开通")
                }
            }
        })


    }

    override fun initView() {

        binding.apply{

            rvContactItem.adapter = mContactItemAdapter
            rvContactUserItem.adapter = mContactUserAdapter

            rvContactOrg.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            rvContactOrg.adapter = mContactOrgAdapter


            mContactItemAdapter.notifyDataSetChanged()

            context?.let { rvContactItem.addItemDecoration(
                RecycleViewDivider(it,
                    LinearLayoutManager.VERTICAL)
            ) }


            context?.let { rvContactUserItem.addItemDecoration(
                RecycleViewDivider(it,
                    LinearLayoutManager.VERTICAL)
            ) }

            tvContactTitle.setText(args.title)
            tvContactTitle.setOnClickListener {
                findNavController().navigateUp()
            }

            tvContactSearch.setOnClickListener {
                val dir = OrgListFragmentDirections.actionOrgListFragmentToContactSearchFragment()
                findNavController().navigate(dir)
            }

        }


    }

    override fun observeViewModels() {
        with(mViewModel){
            org_user.observe(viewLifecycleOwner){

                org_user.value?.apply {
                    if (it.trees != null){
                        orgList = it.trees
                        binding.rvContactItem.visibility = View.VISIBLE
                        mContactItemAdapter.submitList(orgList)
                        mContactItemAdapter.notifyDataSetChanged()
                    }else{
                        binding.rvContactItem.visibility = View.GONE
                    }

                    if (it.users != null) {
                        userList = it.users
                        binding.rvContactUserItem.visibility = View.VISIBLE
                        mContactUserAdapter.submitList(userList)
                        mContactUserAdapter.notifyDataSetChanged()
                    }else{
                        binding.rvContactUserItem.visibility = View.GONE
                    }
                }



                isLoading.observe(viewLifecycleOwner) {
                    if (it) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun notice(contact: ContactItem?){
        contact?.let {
            contactItems.add(contact)
        }
        mContactOrgAdapter.submitList(contactItems)
    }
}