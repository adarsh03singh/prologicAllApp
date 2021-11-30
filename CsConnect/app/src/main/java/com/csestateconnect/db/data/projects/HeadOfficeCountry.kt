package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class HeadOfficeCountry(
    @SerializedName("flag_image")
    val flagImage: String?,
    @SerializedName("icon_image")
    val iconImage: String?,
    val id: Int?,
    val latitude: String?,
    val longitude: String?,
    val name: String?,
    @SerializedName("number_code")
    val numberCode: String?,
    @SerializedName("three_digit_code")
    val threeDigitCode: String?,
    @SerializedName("two_digit_code")
    val twoDigitCode: String?
)