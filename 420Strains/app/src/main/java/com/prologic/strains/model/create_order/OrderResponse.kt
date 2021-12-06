package com.prologic.strains.model.create_order

data class OrderResponse(
    val id: Int,
    val number: String,
    val order_key: String,
    val status: String
)