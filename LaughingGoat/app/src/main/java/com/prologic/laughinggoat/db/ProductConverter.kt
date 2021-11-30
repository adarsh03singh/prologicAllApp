package com.prologic.laughinggoat.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prologic.laughinggoat.model.product.Category
import com.prologic.laughinggoat.model.product.VariationAttribute
import java.util.*

class ProductConverter {
    @TypeConverter
    fun stringToVariationAttribute(data: String?): List<VariationAttribute> {
        if (data == null)
            return Collections.emptyList()
        return Gson().fromJson(data, object : TypeToken<List<VariationAttribute>>() {}.type)
    }

    @TypeConverter
    fun variationAttributeToString(someObjects: List<VariationAttribute>): String {
        return Gson().toJson(someObjects)
    }

    @TypeConverter
    fun stringToCategory(data: String?): List<Category> {
        if (data == null)
            return Collections.emptyList()
        return Gson().fromJson(data, object : TypeToken<List<Category>>() {}.type)
    }

    @TypeConverter
    fun categoryToString(someObjects: List<Category>): String {
        return Gson().toJson(someObjects)
    }
}