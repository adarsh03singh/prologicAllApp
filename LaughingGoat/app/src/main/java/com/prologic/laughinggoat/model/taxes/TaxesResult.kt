package com.prologic.laughinggoat.model.taxes

class TaxesResult : ArrayList<Item>()
data class Item(
    val city: String,
    val `class`: String,
    val compound: Boolean,
    val country: String,
    val id: String,
    val name: String,
    val order: Int,
    val postcode: String,
    val priority: Int,
    val rate: String,
    val shipping: Boolean,
    val state: String
)