package com.prologic.strains.db

import androidx.room.*
import com.prologic.strains.model.product.Category

import com.prologic.strains.model.product.VariationAttribute
import com.prologic.strains.utils.currency
import com.prologic.strains.utils.number2digits

import java.io.Serializable

@TypeConverters(ProductConverter::class)
@Entity(tableName = "product_room", indices = [Index(value = ["unique_id"], unique = true)])
class ProductRoom : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var product_id: String = ""
    var variation_id: String = ""
    var quantity: Int = 0
    var unique_id: String = ""
    var description: String = ""
    var image: String = ""
    var name: String = ""
    var price: String = ""
    var regular_price: String = ""
    var sale_price: String = ""
    var short_description: String = ""
    var categories: List<Category>? = null
    var variation_attribute: List<VariationAttribute>? = null

    fun getProductName(): String {
        var value = name
        if (variation_attribute?.size!! > 0) {
            value += " ("
            if (variation_attribute!!.size == 1) {
                value += variation_attribute!![0].name + " " + variation_attribute!![0].option
            }
            if (variation_attribute!!.size == 2) {
                value += ", " + variation_attribute!![1].name + " " + variation_attribute!![1].option
            }
            value += ")"
        }
        return value
    }

    fun mPrice(): Double {
        var mPrice = 0.0
        if (sale_price.isNotEmpty())
            mPrice = sale_price.toDouble()
        else if (regular_price.isNotEmpty())
            mPrice = regular_price.toDouble()
        else if (price.isNotEmpty())
            mPrice = price.toDouble()
        return mPrice
    }

    fun totalPrice(): Double {
        var mPrice = 0.0
        if (sale_price.isNotEmpty())
            mPrice = sale_price.toDouble()
        else if (regular_price.isNotEmpty())
            mPrice = regular_price.toDouble()
        else if (price.isNotEmpty())
            mPrice = price.toDouble()
        return mPrice * quantity
    }

}


