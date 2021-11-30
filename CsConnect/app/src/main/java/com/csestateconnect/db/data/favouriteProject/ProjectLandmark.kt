package com.csestateconnect.db.data.favouriteProject


import com.google.gson.annotations.SerializedName

data class ProjectLandmark(
    val address: String?,
    @SerializedName("code_name")
    val codeName: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    @SerializedName("landmark_category")
    val landmarkCategory: LandmarkCategory?,
    val latitude: String?,
    val location: LocationX?,
    val longitude: String?,
    val name: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)