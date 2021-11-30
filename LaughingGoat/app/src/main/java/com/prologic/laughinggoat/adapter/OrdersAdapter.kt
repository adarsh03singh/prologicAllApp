package com.prologic.laughinggoat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.ecom.prologic.viewmodel.OrdersListViewModel
import com.prologic.laughinggoat.databinding.OrdersAdapterBinding
import com.prologic.laughinggoat.model.order_list.OrdersResult
import com.prologic.laughinggoat.utils.OrderListClickListener
import com.prologic.laughinggoat.utils.currency


class OrdersAdapter(val viewModel: OrdersListViewModel) :
    RecyclerView.Adapter<OrdersAdapter.ProductHolder>() {
    lateinit var orderListClickListener: OrderListClickListener
    var arrayList = OrdersResult()

    fun setOnOrderListClickListener(orderListClickListener: OrderListClickListener) {
        this.orderListClickListener = orderListClickListener
    }

    fun setUpdateAdapter(arrayList: OrdersResult) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun getItemCount() = arrayList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding =
            OrdersAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bindHolder(position)
    }

    inner class ProductHolder(val binding: OrdersAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindHolder(position: Int) {
            val item = arrayList[position]
            binding.orderId.text = item.id.toString()
            binding.createdAt.text = item.date_created
            binding.orderStatus.text = item.status
            binding.paymentMethod.text = item.payment_method
            binding.totalAmt.text = currency + item.total
            val billing_data = item.billing
            binding.name.text = billing_data.first_name + " " + billing_data.last_name
            binding.email.text = billing_data.email
            binding.mobile.text = billing_data.phone
            var address = ""
            address += billing_data.address_1 + " " + billing_data.address_2 + " "
            if (billing_data.company.isNotEmpty())
                address += billing_data.company
            address += billing_data.city + " " + billing_data.state + " " + billing_data.country +  " " + billing_data.postcode + ""
            binding.address.text = address
            binding.click.setOnClickListener{
                orderListClickListener.onClick(item)
            }
        }
    }


}