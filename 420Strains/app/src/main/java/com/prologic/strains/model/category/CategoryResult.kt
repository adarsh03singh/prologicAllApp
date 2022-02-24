package com.prologic.strains.model.category

import java.io.Serializable

class CategoryResult : ArrayList<CategoryItem>()

data class CategoryItem(
    val id: String,
    val name: String,
    val count: String,
) : Serializable {
    fun getNameCount(): String {
        return name + " (" + count + ")"
    }
}





