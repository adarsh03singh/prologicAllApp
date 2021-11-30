package com.prologic.laughinggoat.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.HomeProductAdapterBinding
import com.prologic.laughinggoat.model.product.ProductResult
import com.prologic.laughinggoat.utils.FeaturedProductClickListener
import com.prologic.laughinggoat.utils.currency
import com.prologic.laughinggoat.utils.loadImage
import com.prologic.laughinggoat.utils.showImage
import com.prologic.laughinggoat.viewmodel.HomeViewModel


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
            binding.name.setText(item.name)
            loadImage(binding.image, item.images.get(0).src)
            val price = item.getProductPrice()
            binding.price.text = currency+ price
            if (price.isNotEmpty()) {
                binding.price.visibility = View.VISIBLE
            } else {
                binding.price.visibility = View.GONE
            }

            binding.click.setOnClickListener {
                clickItemListener.onClick(item)
            }
        }


    }

}