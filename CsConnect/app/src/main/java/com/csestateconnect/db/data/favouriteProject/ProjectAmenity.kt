package com.csestateconnect.db.data.favouriteProject


import com.google.gson.annotations.SerializedName

data class ProjectAmenity(
    @SerializedName("amenity_category")
    val amenityCategory: AmenityCategory?,
    @SerializedName("code_name")
    val codeName: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)