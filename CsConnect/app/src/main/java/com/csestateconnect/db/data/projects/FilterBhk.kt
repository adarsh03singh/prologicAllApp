package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterBhk(
    val bhk: Bhk?,
    @SerializedName("doc_count")
    val docCount: Int?
)