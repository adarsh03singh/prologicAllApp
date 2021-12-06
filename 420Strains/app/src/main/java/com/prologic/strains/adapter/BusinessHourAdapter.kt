package com.prologic.strains.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.R
import com.prologic.strains.databinding.BusinessHourAdapterBinding
import com.prologic.strains.model.business_hour.BusinessHourResult
import com.prologic.strains.viewmodel.HomeViewModel

class BusinessHourAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<BusinessHourAdapter.MyViewHolder>() {
    private var arrayList = BusinessHourResult()
    private var day = ""
    private lateinit var context: Context

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(context: Context, day: String, arrayList: BusinessHourResult) {
        this.arrayList = arrayList
        this.day = day
        this.context = context
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BusinessHourAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class MyViewHolder(val binding: BusinessHourAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            if (item.day.equals("Sunday", ignoreCase = false)) {
                binding.day.setTextColor(context.resources.getColor(R.color.red))
                binding.from.setTextColor(context.resources.getColor(R.color.red))
                binding.to.setTextColor(context.resources.getColor(R.color.red))
            } else if (item.day.equals(day, ignoreCase = false)) {
                binding.day.setTextColor(context.resources.getColor(R.color.white))
                binding.from.setTextColor(context.resources.getColor(R.color.white))
                binding.to.setTextColor(context.resources.getColor(R.color.white))
            } else {
                binding.day.setTextColor(context.resources.getColor(R.color.colorAccent))
                binding.from.setTextColor(context.resources.getColor(R.color.colorAccent))
                binding.to.setTextColor(context.resources.getColor(R.color.colorAccent))
            }

        }
    }


}