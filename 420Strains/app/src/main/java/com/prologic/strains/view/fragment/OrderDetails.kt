package com.ecom.prologic.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


import com.prologic.strains.model.order_list.OrderItem

import com.ecom.prologic.viewmodel.OrderDetailsViewModel
import com.ecom.prologic.viewmodel.OrdersListViewModel
import com.google.gson.Gson
import com.prologic.strains.R
import com.prologic.strains.databinding.OrdersDetailsBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MainActivity
import kotlinx.android.synthetic.main.orders_details.*
import kotlinx.android.synthetic.main.orders_details.view.*
import kotlinx.android.synthetic.main.orders_list.*


class OrderDetails : Fragment() {
    lateinit var viewModel: OrderDetailsViewModel
    lateinit var binding: OrdersDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.orders_details, container, false)
        viewModel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent_data = arguments?.getString(intent_data)

        if (intent_data != null) {
            val orderItem = gson.fromJson(intent_data, OrderItem::class.java)
            viewModel.orderItem.value = orderItem
        }
        viewModel.isLoaderVisible.observe(viewLifecycleOwner, Observer { it ->

        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { it ->
            if (it.isNotEmpty())
                AlertError.show(requireActivity(), it)
        })
        viewModel.orderItem.observe(viewLifecycleOwner, Observer { it ->
            setView(it)
            setHeader()
        })
        billing_details.setOnClickListener {


        }
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
        }
    }

    private fun setHeader() {
        shooterFragment = this
        val title = "Order Id : " + viewModel.orderItem.value!!.id
        (activity as MainActivity).setHeader(title, true, false, true, false)
    }

    private fun setView(orderItem: OrderItem) {
        orderId.text = orderItem.id.toString()
        orderStatus.text = orderItem.status
        paymentMethod.text =
            orderItem.payment_method.uppercase() + " (" + orderItem.payment_method_title + ")"
        dateCreated.text = orderItem.date_created
        totalAmount.text = currency + orderItem.total

        val billing_data = orderItem.billing
        billing_name.text = billing_data.first_name + " " + billing_data.last_name
        billing_email.text = billing_data.email
        billing_mobile.text = billing_data.phone
        var billing_add = ""
        billing_add += billing_data.address_1 + " " + billing_data.address_2 + " "
        if (billing_data.company.isNotEmpty())
            billing_add += billing_data.company
        billing_add += billing_data.city + " " + billing_data.state + " " + billing_data.country + " " + billing_data.postcode + ""
        billing_address.text = billing_add

        val shipping_data = orderItem.shipping
        shipping_name.text = shipping_data.first_name + " " + shipping_data.last_name
        var shipping_add = ""
        shipping_add += shipping_data.address_1 + " " + shipping_data.address_2 + " "
        if (shipping_data.company.isNotEmpty())
            shipping_add += shipping_data.company
        shipping_add += shipping_data.city + " " + shipping_data.state + " " + shipping_data.country + " " + shipping_data.postcode + ""
        shipping_address.text = shipping_add

        addViewItem.removeAllViews()
        orderItem.line_items.forEach {
            val view = layoutInflater.inflate(R.layout.order_item_list, addViewItem, false)
            val name = view.findViewById<TextView>(R.id.name)
            val price = view.findViewById<TextView>(R.id.price)
            val quantity = view.findViewById<TextView>(R.id.quantity)
            val item_price = view.findViewById<TextView>(R.id.item_price)
            name.text = it.name
            price.text = currency + it.price.toString()
            quantity.text = it.quantity.toString()
            item_price.text = currency + it.total
            addViewItem.addView(view)
        }
        itemTotal.text = currency + orderItem.total
        shippingAmt.text = currency + orderItem.shipping_total
        // taxAmt.text = currency + orderItem.total_tax
        orderTotal.text = currency + orderItem.total
        paidAmt.text = currency + orderItem.total
        if (orderItem.discount_total.toDouble() > 0) {
            discountLay.visibility = View.VISIBLE
            discountAmt.text = currency + orderItem.discount_total
        } else {
            discountLay.visibility = View.GONE
            discountAmt.text = currency + "0"
        }

        val tax_lines = orderItem.tax_lines
        if (tax_lines.size == 1) {
            taxLay.visibility = View.VISIBLE
            val taxLine = tax_lines.get(0)
            taxName.text = taxLine.rate_code + " :"
            taxAmt.text = currency + taxLine.tax_total
        } else {
            taxLay.visibility = View.GONE
            taxName.text = ""
            taxAmt.text = ""
        }

        val shipping_lines = orderItem.shipping_lines
        if (shipping_lines.size == 1) {
            shippingLay.visibility = View.VISIBLE
            val shipping_line = shipping_lines.get(0)
            shippingName.text = shipping_line.method_title + " :"
            shippingAmt.text = currency + shipping_line.total
        } else {
            shippingLay.visibility = View.GONE
            shippingName.text = ""
            shippingAmt.text = ""
        }
    }

}

