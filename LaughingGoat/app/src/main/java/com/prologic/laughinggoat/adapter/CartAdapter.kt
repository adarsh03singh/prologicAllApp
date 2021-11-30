package com.prologic.laughinggoat.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.CartAdapterBinding
import com.prologic.laughinggoat.db.ProductRoom
import com.prologic.laughinggoat.utils.currency
import com.prologic.laughinggoat.utils.loadImage
import com.prologic.laughinggoat.utils.roundOfDecimal
import com.prologic.laughinggoat.utils.showImage
import com.prologic.laughinggoat.viewmodel.CartItemViewModel


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

        private fun setPrice(regularPrice: String, salePrice: String) {
            val regular_price = binding.regularPrice
            val sale_price = binding.salePrice
            regular_price.text = currency + regularPrice
            sale_price.text = currency + salePrice
            if (salePrice.isEmpty()) {
                sale_price.visibility = View.GONE
                regular_price.setTextColor(regular_price.context.resources.getColor(R.color.green))
            } else {
                regular_price.setTextColor(regular_price.context.resources.getColor(R.color.gray))
                sale_price.visibility = View.VISIBLE
            }
        }

        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            setPrice(item.regular_price, item.sale_price)
            val quantity = item.quantity
            var item_price =
                currency + roundOfDecimal(item.regular_price.toDouble() * quantity)
            if (item.sale_price.isNotEmpty())
                item_price = currency + roundOfDecimal(item.sale_price.toDouble() * quantity)
            binding.itemPrice.text = item_price
            binding.name.text = item.name
            showImage(binding.image, item.image, item.name)
            val attribute_variations = item.variation_attribute
            if (attribute_variations?.size == 0) {
                binding.unit.text = ""
                binding.unit.visibility = View.GONE
            } else {
                var unit = ""
                for (item in item.variation_attribute!!) {
                    unit += item.name + " " + item.option + " "
                }
                binding.unit.text = unit
                binding.unit.visibility = View.VISIBLE
            }

            binding.click.setOnClickListener {

            }
            binding.minus.setOnClickListener {
                viewModel.updateQuantity(item, (quantity - 1))
                //notifyItemChanged(position)
            }


        }


    }


}