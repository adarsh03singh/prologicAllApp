package com.prologic.laughinggoat.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.prologic.laughinggoat.databinding.CheckoutCartAdapterBinding
import com.prologic.laughinggoat.db.ProductRoom
import com.prologic.laughinggoat.utils.currency
import com.prologic.laughinggoat.utils.loadImage
import com.prologic.laughinggoat.utils.roundOfDecimal
import com.prologic.laughinggoat.utils.showImage


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
        private fun setPrice(regularPrice: String, salePrice: String) {
            if (salePrice.isNotEmpty()) {
                binding.salePrice.text= currency+salePrice
            } else {
                binding.salePrice.text= currency+regularPrice
            }
        }

        fun bindHolder(position: Int) {
            val productItem = arrayList[position]
            binding.productRoom = productItem
            setPrice(productItem.regular_price,productItem.sale_price)
            val quantity = productItem.quantity
            var item_price =currency +  roundOfDecimal(productItem.regular_price.toDouble() * quantity)
            if (productItem.sale_price.isNotEmpty())
                item_price =currency + roundOfDecimal(productItem.sale_price.toDouble() * quantity)
            binding.itemPrice.text= item_price
            binding.name.text=productItem.name
            showImage(binding.image, productItem.image, productItem.name)
            binding.quantity.text=quantity.toString()
            val attribute_variations=productItem.variation_attribute
            if (attribute_variations?.size ==0){
                binding.unit.text=""
                binding.unit.visibility=View.GONE
            }else{
                var unit=""
                for (item in productItem.variation_attribute!!){
                    unit+=item.name+" "+item.option+" "
                }
                binding.unit.text=unit
                binding.unit.visibility=View.VISIBLE
            }

        }
    }
}