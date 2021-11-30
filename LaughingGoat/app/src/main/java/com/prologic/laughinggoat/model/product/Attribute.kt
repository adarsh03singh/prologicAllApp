package com.prologic.laughinggoat.model.product

import java.io.Serializable

data class Attribute(
    val name: String,
    val options: List<String>
): Serializable