package com.csestateconnect.db.data.countries

data class City(
    val id: Int,
    val name: String,
    val latitude: String,
    val longitude: String,
    val number_code: String,
    val icon_image: String,
    val locations: List<Location>,
    val created_at: String,
    val updated_at: String
)