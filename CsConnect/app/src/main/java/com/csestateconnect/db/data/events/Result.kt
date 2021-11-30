package com.csestateconnect.db.data.events


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("created_at")
    val createdAt: String?,
    val id: Int?,
    val name: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)