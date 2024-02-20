package com.communication.pingyi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_core.checkDoubleClick
import com.communication.lib_core.tools.EVENTBUS_APP_CLICK
import com.communication.lib_http.api.WEB_EVENT
import com.communication.lib_http.api.WEB_RUNTIME
import com.communication.lib_http.httpdata.home.AppsItem
import com.communication.lib_http.httpdata.home.AppsItemTitle
import com.communication.pingyi.R
import com.communication.pingyi.databinding.ItemHomeAppsBinding
import com.communication.pingyi.ext.pyToast
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by LG
 * on 2022/3/24  14:33
 * Description：
 */
class HomeAppListAdapter : ListAdapter<AppsItem, RecyclerView.ViewHolder>(AppItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PyViewHolder(
            ItemHomeAppsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = getItem(position)
        (holder as PyViewHolder).bind(app, position)
    }

    class PyViewHolder(
        private val binding: ItemHomeAppsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppsItem, position: Int) {
            binding.apply {
                appItem = item.meta
                var url = ""
                when(item.meta.title){
                    "运行驾驶舱" -> {
                        icon.setImageResource(R.drawable.ic_operation_cockpit)
                        url = WEB_RUNTIME
                    }
                    "事件监测" -> {
                        icon.setImageResource(R.drawable.ic_event_monitoring)
                        url = WEB_EVENT
                    }
                    "视频监控" -> {
                        icon.setImageResource(R.drawable.ic_video_surveillance)
                    }
                    "基础设施监测" -> {
                        icon.setImageResource(R.drawable.ic_infrastructure_monitoring)
                    }
                    "机电运维" -> {
                        icon.setImageResource(R.drawable.ic_electromechanical_maintenance)
                    }
                    "公路养护" -> {
                        icon.setImageResource(R.drawable.ic_highway_maintenance)
                    }
                    else -> {
                        icon.setImageResource(R.drawable.ic_highway_maintenance)
                        url = WEB_EVENT
                    }
                }
                setClickListener {
                    if (checkDoubleClick()) {
                        if(url.isNullOrEmpty()) {
                            binding.root.context?.let {
                                pyToast(it.getString(R.string.error_fun_empty))
                            }

                        }else{
                            LiveEventBus.get(EVENTBUS_APP_CLICK).post(url)
                        }
                    }
                }

                executePendingBindings()
            }
        }
    }
}

private class AppItemDiffCallback : DiffUtil.ItemCallback<AppsItem>() {

    override fun areItemsTheSame(oldItem: AppsItem, newItem: AppsItem): Boolean {
        return oldItem.meta.title == newItem.meta.title
    }

    override fun areContentsTheSame(oldItem: AppsItem, newItem: AppsItem): Boolean {
        return oldItem == newItem
    }
}
