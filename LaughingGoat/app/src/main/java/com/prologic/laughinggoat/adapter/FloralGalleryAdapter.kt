package com.prologic.laughinggoat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.prologic.laughinggoat.databinding.FloralGalleryAdapterBinding
import com.prologic.laughinggoat.model.FloralGalleryResult

import com.prologic.laughinggoat.utils.FloralGalleryItemListener

import com.prologic.laughinggoat.viewmodel.HomeViewModel


class FloralGalleryAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<FloralGalleryAdapter.ProductHolder>() {
    private lateinit var clickItemListener: FloralGalleryItemListener
    var arrayList = FloralGalleryResult()

    fun setClickItemListener(clickItemListener: FloralGalleryItemListener) {
        this.clickItemListener = clickItemListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpdateAdapter(audioResult: FloralGalleryResult) {
        this.arrayList = audioResult
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding =
            FloralGalleryAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: FloralGalleryAdapterBinding) :
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