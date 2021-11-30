package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class AmenityCategory(
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?
)