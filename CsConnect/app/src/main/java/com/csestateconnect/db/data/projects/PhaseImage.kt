package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class PhaseImage(
    val id: Int?,
    @SerializedName("image_type")
    val imageType: ImageTypeXX?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val name: String?,
    @SerializedName("record_type")
    val recordType: String?
)