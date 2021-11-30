package com.csestateconnect.db.data.leads

data class User(
    val id: Int,
    val phone_number: String,
    val first_name: String,
    val last_name: String,
    val email: String
)