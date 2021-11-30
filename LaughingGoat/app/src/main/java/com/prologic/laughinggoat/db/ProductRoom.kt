package com.prologic.laughinggoat.db

import androidx.room.*
import com.prologic.laughinggoat.model.product.Category

import com.prologic.laughinggoat.model.product.VariationAttribute

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
}


