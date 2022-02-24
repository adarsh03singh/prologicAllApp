package com.prologic.strains.model.product

import java.io.Serializable

class VariationResult : ArrayList<Variation>()
data class Variation(
    val id: String,
    val price: String,
    val regular_price: String,
    val sale_price: String,
    val image: Image,
    val on_sale: Boolean,
    val stock_status: String,
    val stock_quantity: Int,
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

data class VariationAttribute(
    val name: String,
    val option: String
)
