package com.prologic.laughinggoat.model.shipping

data class Items(
    val id: String,
    val label: String,
    val description: String,
    val type: String,
    val value: String,
    val default: String,
    val tip: String,
    val placeholder: String,
    val options: Options
)