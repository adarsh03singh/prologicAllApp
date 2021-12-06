package com.prologic.strains.model.product

import com.prologic.strains.utils.currency
import kotlinx.android.synthetic.main.product_info.*
import java.io.Serializable

class ProductItem(
    val id: String,
    val name: String,
    val slug: String,
    val short_description: String,
    val description: String,
    val images: List<Image>,
    val categories: List<Category>,
    val sku: String,
    val price: String,
    val regular_price: String,
    val sale_price: String,
    val in_stock: Boolean,
    val variations: List<Variation>,
    val attributes: List<Attribute>,
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

    fun getImageUrl(): String {
        return images[0].src
    }
}

