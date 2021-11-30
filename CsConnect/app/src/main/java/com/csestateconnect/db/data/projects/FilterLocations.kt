package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterLocations(
    @SerializedName("doc_count")
    val docCount: Int?,
    val locations: Locations?
)