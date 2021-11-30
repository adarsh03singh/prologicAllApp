package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterBsp(
    val bsp: Bsp?,
    @SerializedName("doc_count")
    val docCount: Int?
)