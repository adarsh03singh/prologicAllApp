package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class TowerImage(
    val id: Int?,
    @SerializedName("image_type")
    val imageType: ImageTypeX?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val name: String?,
    @SerializedName("record_type")
    val recordType: String?
)