package com.prologic.laughinggoat.model.product

import java.io.Serializable

data class Variation(
    val id: String,
    val price: String,
    val regular_price: String,
    val sale_price: String,
    val image: List<Image>,
    val attributes: List<VariationAttribute>
    ): Serializable