package com.prologic.strains.model.business_hour



class BusinessHourResult : ArrayList<Item>()
data class Item(
    val day: String,
    val from: String,
    val to: String
)


