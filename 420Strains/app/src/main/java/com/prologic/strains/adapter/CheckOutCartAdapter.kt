package com.prologic.strains.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.prologic.strains.databinding.CheckoutCartAdapterBinding
import com.prologic.strains.db.ProductRoom



class CheckOutCartAdapter() : RecyclerView.Adapter<CheckOutCartAdapter.ProductHolder>() {
    var arrayList: List<ProductRoom> = listOfNotNull()
    fun setAdapterUpdate(arrayList: List<ProductRoom>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding =  CheckoutCartAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: CheckoutCartAdapterBinding) :     RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(position: Int) {
            val productItem = arrayList[position]
            binding.item = productItem

        }
    }
}