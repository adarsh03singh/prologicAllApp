package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class UnitInsideAreaComponent(
    val area: Float?,
    @SerializedName("area_view")
    val areaView: String?,
    val id: Int?,
    @SerializedName("inside_area_category")
    val insideAreaCategory: InsideAreaCategory?,
    val length: Float?,
    @SerializedName("length_view")
    val lengthView: String?,
    val name: String?,
    val width: Float?,
    @SerializedName("width_view")
    val widthView: String?
)