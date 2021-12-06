package com.prologic.strains.model.order_list

data class LineItem(
    val name: String,
    val parent_name: String,
    val price: Double,
    val quantity: Int,
    val total: String,
    val total_tax: String
)