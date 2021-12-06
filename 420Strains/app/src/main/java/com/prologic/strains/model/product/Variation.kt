package com.prologic.strains.model.product

import java.io.Serializable

data class Variation(
    val id: String,
    val price: String,
    val regular_price: String,
    val sale_price: String,
    val image: List<Image>,
    val in_stock: Boolean,
    val attributes: List<VariationAttribute>
) : Serializable {
    fun getProductPrice(): String {
        var value = ""
        if (sale_price.isNotEmpty())
            value = sale_price
        else if (regular_price.isNotEmpty())
            value = regular_price
        else if (price.isNotEmpty())
            value = price

        return value
    }
}
