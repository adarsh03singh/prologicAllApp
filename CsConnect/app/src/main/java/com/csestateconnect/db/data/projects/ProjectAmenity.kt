package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class ProjectAmenity(
    @SerializedName("amenity_category")
    val amenityCategory: AmenityCategory?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?
)