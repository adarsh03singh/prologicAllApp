package com.prologic.laughinggoat.model.order_list


import com.prologic.laughinggoat.model.auth.Billing
import com.prologic.laughinggoat.model.auth.Shipping


data class OrderItem(
    val billing: Billing,
    val currency: String,
    val currency_symbol: String,
    val customer_id: Int,
    val customer_note: String,
    val date_completed: String,
    val date_completed_gmt: String,
    val date_created: String,
    val date_created_gmt: String,
    val date_modified: String,
    val date_modified_gmt: String,
    val date_paid: String,
    val date_paid_gmt: String,
    val id: Int,
    val line_items: List<LineItem>,
    val order_key: String,
    val parent_id: Int,
    val payment_method: String,
    val payment_method_title: String,
    val shipping: Shipping,
    val shipping_lines: List<ShippingLine>,
    val shipping_total: String,
    val status: String,
    val tax_lines: List<TaxLine>,
    val total: String,
    val total_tax: String,
    val discount_total: String
)