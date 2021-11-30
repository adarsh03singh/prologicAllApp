package com.csestateconnect.db.data.getProfileData


import com.google.gson.annotations.SerializedName

data class Rm(
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    val id: Int?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)