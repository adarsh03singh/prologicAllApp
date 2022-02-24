package com.prologic.strains.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prologic.strains.R
import com.prologic.strains.databinding.HomeProductAdapterBinding
import com.prologic.strains.databinding.ProductAdapterBinding
import com.prologic.strains.model.product.ProductResult
import com.prologic.strains.utils.ProductItemListener


class ProductAdapter(val viewType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var arrayList = ProductResult()
    lateinit var productItemListener: ProductItemListener

    fun setOnItemListener(productItemListener: ProductItemListener) {
        this.productItemListener = productItemListener
    }

    fun setUpdateAdapter(arrayList: ProductResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    fun setUpdateAdapter() {
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size
    override fun getItemViewType(position: Int) = viewType


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        if (viewType == 1) {
            val binding =
                ProductAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = ProductHolder(binding)
        } else if (viewType == 2) {
            val binding = HomeProductAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            holder = HomeProductHolder(binding)
        }
        return holder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductHolder)
            holder.bindHolder(position)
        else if (holder is HomeProductHolder)
            holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: ProductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item

            if (!item.stock_status.equals("instock")) {
                setAlertText(binding.alertText, "out_of_stocke")
            } else if (item.on_sale) {
                setAlertText(binding.alertText, "on_sale")
            } else {
                setAlertText(binding.alertText, "")
            }
            binding.click.setOnClickListener {
                productItemListener.onClick(item)
            }

        }


    }

    inner class HomeProductHolder(val binding: HomeProductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.item = item
            if (!item.stock_status.equals("instock")) {
                setAlertText(binding.alertText, "out_of_stocke")
            } else if (item.on_sale) {
                setAlertText(binding.alertText, "on_sale")
            } else {
                setAlertText(binding.alertText, "")
            }
            binding.click.setOnClickListener {
                productItemListener.onClick(item)
            }
        }


    }

    private fun setAlertText(textView: TextView, value: String) {
        if (value.equals("out_of_stocke")) {
            textView.visibility = View.VISIBLE
            textView.text = "Out Of Stock"
            textView.setBackgroundResource(R.color.red)
        } else if (value.equals("on_sale")) {
            textView.visibility = View.VISIBLE
            textView.text = "Sale"
            textView.setBackgroundResource(R.color.orange)
        } else {
            textView.visibility = View.GONE
            textView.text = ""
            textView.setBackgroundResource(R.color.trans)
        }
    }

}