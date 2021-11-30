package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class ProjectSpecificationsAndOther(
    @SerializedName("area_name")
    val areaName: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?,
    val type: String?
)