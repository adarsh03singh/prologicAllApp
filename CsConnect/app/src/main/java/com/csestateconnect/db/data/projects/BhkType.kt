package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class BhkType(
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val name: String?,
    val rooms: Int?
)