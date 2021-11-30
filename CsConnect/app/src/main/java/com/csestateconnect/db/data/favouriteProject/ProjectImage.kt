package com.csestateconnect.db.data.favouriteProject


import com.google.gson.annotations.SerializedName

data class ProjectImage(
    val id: Int?,
    @SerializedName("image_type")
    val imageType: ImageType?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val name: String?
)