package com.prologic.laughinggoat.model.shipping

data class ShippingResult(
    val id: Int,
    val title: String,
    val instance_id: Int,
    val order: Int,
    val enabled: Boolean,
    val method_description: String,
    val method_id: String,
    val method_title: String,
    val settings: Settings,
)