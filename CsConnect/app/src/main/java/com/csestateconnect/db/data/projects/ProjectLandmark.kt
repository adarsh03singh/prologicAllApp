package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class ProjectLandmark(
    val address: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    @SerializedName("landmark_category")
    val landmarkCategory: LandmarkCategory?,
    val latitude: String?,
    val longitude: String?,
    val name: String?
)