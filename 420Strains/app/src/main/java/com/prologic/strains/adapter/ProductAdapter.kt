package com.prologic.strains.adapter

import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.databinding.ProductAdapterBinding
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.utils.ProductItemListener
import com.prologic.strains.utils.currency
import com.prologic.strains.utils.number2digits


import com.prologic.strains.viewmodel.ProductListViewModel


class ProductAdapter(val viewModel: ProductListViewModel) :
    RecyclerView.Adapter<ProductAdapter.ProductHolder>() {
    var productResult = ProductResult()
    lateinit var productItemListener: ProductItemListener

    fun setOnProductItemListener(productItemListener: ProductItemListener) {
        this.productItemListener = productItemListener
    }

    fun setUpdateAdapter(arrayList: ProductResult) {
        this.productResult = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount() = productResult.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding =
            ProductAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: ProductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(position: Int) {
            val item = productResult[position]
            binding.item = item
            binding.click.setOnClickListener { productItemListener.onClick(item) }

        }

    }


}