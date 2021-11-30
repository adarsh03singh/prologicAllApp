package com.prologic.laughinggoat.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.laughinggoat.databinding.ProductAdapterBinding
import com.prologic.laughinggoat.model.product.ProductResult
import com.prologic.laughinggoat.utils.ProductItemListener
import com.prologic.laughinggoat.utils.currency
import com.prologic.laughinggoat.viewmodel.ProductListViewModel

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
            binding.productItem = item
            val attributes = item.attributes
            if (attributes.size > 0) {
                val sb = StringBuilder()
                val separator = "\n"
                attributes[0].options.forEach {
                    sb.append(it).append(separator)
                }
                val string = sb.removeSuffix(separator).toString()
                binding.attributes.text = string
                binding.attributes.visibility = View.VISIBLE
            } else {
                binding.attributes.text = ""
                binding.attributes.visibility = View.GONE
            }
            val price = item.getProductPrice()
            binding.price.text = currency + price
            if (price.isNotEmpty()) {
                binding.price.visibility = View.VISIBLE
            } else {
                binding.price.visibility = View.GONE
            }
            binding.click.setOnClickListener { productItemListener.onClick(item) }
           /* binding.image.setOnClickListener {
             //   showImage(binding.image, item.images.get(0).src, item.name)
            }*/
        }

    }


}