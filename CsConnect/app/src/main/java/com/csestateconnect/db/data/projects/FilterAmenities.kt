package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterAmenities(
    val amenities: Amenities?,
    @SerializedName("doc_count")
    val docCount: Int?
)