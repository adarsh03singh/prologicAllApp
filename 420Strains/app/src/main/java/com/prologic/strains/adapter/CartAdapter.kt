package com.prologic.strains.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.R
import com.prologic.strains.databinding.CartAdapterBinding
import com.prologic.strains.db.ProductRoom

import com.prologic.strains.viewmodel.CartItemViewModel


class CartAdapter(val viewModel: CartItemViewModel) :
    RecyclerView.Adapter<CartAdapter.ProductHolder>() {
    var arrayList: List<ProductRoom> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setUpdateAdapter(arrayList: List<ProductRoom>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = CartAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: CartAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            val quantity = item.quantity

            binding.minus.setOnClickListener {
                viewModel.updateQuantity(item, (quantity - 1))
                notifyItemChanged(position)
            }
            binding.plus.setOnClickListener {
                viewModel.updateQuantity(item, (quantity + 1))
                notifyItemChanged(position)
            }

        }


    }


}