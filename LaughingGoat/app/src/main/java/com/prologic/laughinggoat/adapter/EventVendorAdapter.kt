package com.prologic.laughinggoat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.laughinggoat.databinding.EventVendorAdapterBinding
import com.prologic.laughinggoat.model.event_vendor.EventVendorResult
import com.prologic.laughinggoat.utils.EventVendorItemListener
import com.prologic.laughinggoat.viewmodel.HomeViewModel


class EventVendorAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<EventVendorAdapter.MyViewHolder>() {
    private var arrayList = EventVendorResult()
    private lateinit var clickItemListener: EventVendorItemListener

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(arrayList: EventVendorResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    fun setClickItemListener(clickItemListener: EventVendorItemListener) {
        this.clickItemListener = clickItemListener
    }

    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventVendorAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class MyViewHolder(val binding: EventVendorAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            binding.click.setOnClickListener {
                clickItemListener.onClick(item, binding.content.text.toString())
            }

        }
    }


}