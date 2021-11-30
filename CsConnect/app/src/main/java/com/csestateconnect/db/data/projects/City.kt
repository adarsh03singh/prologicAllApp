package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val latitude: String?,
    val longitude: String?,
    val name: String?,
    @SerializedName("number_code")
    val numberCode: String?
)