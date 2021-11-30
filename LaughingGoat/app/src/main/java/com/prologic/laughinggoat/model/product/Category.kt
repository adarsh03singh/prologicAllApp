package com.prologic.laughinggoat.model.product

import java.io.Serializable

data class Category(
    val id: String,
    val name: String,
    val slug: String
) : Serializable