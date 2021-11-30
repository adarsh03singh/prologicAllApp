package com.csestateconnect.db.data.favouriteProject


import com.google.gson.annotations.SerializedName

data class ProjectWowFactor(
    @SerializedName("code_name")
    val codeName: String?,
    val id: Int?,
    val name: String?
)