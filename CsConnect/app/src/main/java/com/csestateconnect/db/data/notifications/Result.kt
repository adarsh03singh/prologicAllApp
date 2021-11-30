package com.csestateconnect.db.data.notifications

data class Result(
    val id: Int,
    val user: User,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val body: String,
    val record_id: Int,
    val click_action: String,
    val icon_image: Any
)