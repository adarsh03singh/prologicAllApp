package com.prologic.strains.model.order_list

data class ShippingLine(
    val id: Int,
    val instance_id: String,
    val meta_data: List<Any>,
    val method_id: String,
    val method_title: String,
    val taxes: List<Any>,
    val total: String,
    val total_tax: String
)