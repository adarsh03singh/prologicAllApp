package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class HeadOfficeLocation(
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val latitude: String?,
    val longitude: String?,
    val name: String?,
    @SerializedName("pin_code")
    val pinCode: String?
)