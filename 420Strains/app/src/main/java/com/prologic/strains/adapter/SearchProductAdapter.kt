package com.prologic.strains.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.databinding.SearchProductAdpBinding
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.utils.ProductItemListener
import com.prologic.strains.utils.hideSoftKeyBoard
import com.prologic.strains.viewmodel.SearchProductViewModel


class SearchProductAdapter(val viewModel: SearchProductViewModel) :
    RecyclerView.Adapter<SearchProductAdapter.ProductHolder>() {
    lateinit var productItemListener: ProductItemListener
    var arrayProductData = ProductResult()

    fun setOnProductListItemClickListener(productItemListener: ProductItemListener) {
        this.productItemListener = productItemListener
    }

    fun setUpdateAdapter(arrayList: ProductResult) {
        this.arrayProductData = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayProductData.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding =
            SearchProductAdpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: SearchProductAdpBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(position: Int) {
            val item = arrayProductData[position]
            binding.name.text = item.name
            val categories = ArrayList<String>()
            item.categories.forEach {
                categories.add(it.name)
            }
            binding.categories.text = categories.joinToString(", ")

            binding.click.setOnClickListener {
                productItemListener.onClick(item)
                hideSoftKeyBoard(it)
            }

        }

    }


}