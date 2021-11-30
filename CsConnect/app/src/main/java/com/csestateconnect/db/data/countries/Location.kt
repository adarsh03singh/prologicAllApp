package com.csestateconnect.db.data.countries

data class Location(
    val id: Int,
    val name: String,
    val latitude: String,
    val longitude: String,
    val pin_code: String,
    val icon_image: String,
    val created_at: String,
    val updated_at: String
)