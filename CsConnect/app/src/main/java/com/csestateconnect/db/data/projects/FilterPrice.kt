package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterPrice(
    @SerializedName("doc_count")
    val docCount: Int?,
    val price: Price?
)