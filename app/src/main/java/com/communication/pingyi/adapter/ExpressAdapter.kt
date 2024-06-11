package com.communication.pingyi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.communication.lib_http.httpdata.contact.ExpressionBean
import com.communication.pingyi.databinding.AdapterExpresionBinding

class ExpressAdapter(private val onExpressionClick: (ExpressionBean) -> Unit) : ListAdapter<ExpressionBean, RecyclerView.ViewHolder>(DIFF_Express_CALLBACK){

    companion object {

        private val DIFF_Express_CALLBACK = object : DiffUtil.ItemCallback<ExpressionBean>() {
            override fun areItemsTheSame(oldItem: ExpressionBean, newItem: ExpressionBean): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ExpressionBean, newItem: ExpressionBean): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExpressionViewHolder(
            AdapterExpresionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val app = getItem(position)
        (holder as ExpressionViewHolder).bind(app, position)
        holder.itemView.setOnClickListener {
            onExpressionClick(app)
        }
    }

    class ExpressionViewHolder(
        private val binding: AdapterExpresionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExpressionBean, position: Int) {
            binding.apply {

                ivExpresion.setImageResource(item.expressionId)
//                executePendingBindings()
            }
        }
    }

}