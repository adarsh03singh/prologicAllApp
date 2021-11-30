package com.prologic.laughinggoat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.laughinggoat.databinding.BlogAdapterBinding
import com.prologic.laughinggoat.model.blog.BlogResult

import com.prologic.laughinggoat.utils.BlogItemListener


import com.prologic.laughinggoat.viewmodel.HomeViewModel


class BlogAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<BlogAdapter.MyViewHolder>() {
    private var arrayList = BlogResult()
    private lateinit var clickItemListener: BlogItemListener

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(arrayList: BlogResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    fun setClickItemListener(clickItemListener: BlogItemListener) {
        this.clickItemListener = clickItemListener
    }

    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BlogAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class MyViewHolder(val binding: BlogAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            binding.click.setOnClickListener {
                clickItemListener.onClick(item)
            }
        }
    }


}