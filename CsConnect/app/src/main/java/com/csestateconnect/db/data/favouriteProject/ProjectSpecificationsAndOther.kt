package com.csestateconnect.db.data.favouriteProject


import com.google.gson.annotations.SerializedName

data class ProjectSpecificationsAndOther(
    @SerializedName("area_name")
    val areaName: String?,
    @SerializedName("code_name")
    val codeName: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?,
    val type: String?
)