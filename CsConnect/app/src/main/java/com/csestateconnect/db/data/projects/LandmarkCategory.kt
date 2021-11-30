package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class LandmarkCategory(
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?
)