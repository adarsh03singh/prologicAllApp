package com.prologic.strains.model.product

import java.io.Serializable

class ProductResult : ArrayList<ProductItem>()

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
    val stock_quantity: Int,
    val stock_status: String,
    val on_sale: Boolean,
    val variations: List<String>,
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

data class Image(
    val src: String
)

data class Attribute(
    val name: String,
    val options: List<String>
)

data class Category(
    val id: String,
    val name: String

)