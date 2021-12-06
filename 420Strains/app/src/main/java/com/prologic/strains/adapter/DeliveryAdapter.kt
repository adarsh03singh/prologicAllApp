package com.prologic.strains.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.databinding.CategoryAdapterBinding
import com.prologic.strains.databinding.DeliveryAdapterBinding

import com.prologic.strains.model.category.CategoryResult
import com.prologic.strains.model.delivery.DeliveryResult
import com.prologic.strains.utils.CategoryItemListener

import com.prologic.strains.viewmodel.HomeViewModel


class DeliveryAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<DeliveryAdapter.MyViewHolder>() {
    private var arrayList = DeliveryResult()


    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(arrayList: DeliveryResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }


    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeliveryAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class MyViewHolder(val binding: DeliveryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            binding.click.setOnClickListener {
viewModel.getDeliveryArea(item)
            }

        }
    }


}