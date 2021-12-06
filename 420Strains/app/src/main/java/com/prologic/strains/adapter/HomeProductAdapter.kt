package com.prologic.strains.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.databinding.HomeProductAdapterBinding
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.utils.FeaturedProductClickListener
import com.prologic.strains.utils.currency
import com.prologic.strains.utils.number2digits
import com.prologic.strains.viewmodel.HomeViewModel


class HomeProductAdapter(val viewModel: HomeViewModel) :
    RecyclerView.Adapter<HomeProductAdapter.ProductHolder>() {
    private lateinit var clickItemListener: FeaturedProductClickListener
    var arrayList = ProductResult()

    fun setClickItemListener(clickItemListener: FeaturedProductClickListener) {
        this.clickItemListener = clickItemListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(arrayList: ProductResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = HomeProductAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: HomeProductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            val priceValue = item.getProductPrice()
            if (priceValue.isNullOrEmpty())
                binding.price.text = currency + number2digits(priceValue.toDouble())
            else
                binding.price.text = ""
            binding.click.setOnClickListener {
                clickItemListener.onClick(item)
            }
        }


    }

}