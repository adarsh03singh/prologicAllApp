package com.prologic.strains.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.databinding.CategoryAdapterBinding

import com.prologic.strains.model.category.CategoryResult
import com.prologic.strains.utils.CategoryItemListener

import com.prologic.strains.viewmodel.HomeViewModel


class CategoryAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    private var arrayList = CategoryResult()
    private lateinit var categoryItemListener: CategoryItemListener

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(arrayList: CategoryResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    fun setCategoryItemListener(categoryItemListener: CategoryItemListener) {
        this.categoryItemListener = categoryItemListener
    }

    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class MyViewHolder(val binding: CategoryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            binding.click.setOnClickListener {
                categoryItemListener.onClick(item)
            }

        }
    }


}