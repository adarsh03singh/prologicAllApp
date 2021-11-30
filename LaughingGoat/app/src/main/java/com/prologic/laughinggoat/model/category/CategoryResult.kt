package com.prologic.laughinggoat.model.category

import java.io.Serializable

class CategoryResult : ArrayList<CategoryItem>()

data class CategoryItem(
    val id: String,
    val name: String,
    val slug: String,
    val image: Image,
) : Serializable

data class Image(
    val src: String
) : Serializable





