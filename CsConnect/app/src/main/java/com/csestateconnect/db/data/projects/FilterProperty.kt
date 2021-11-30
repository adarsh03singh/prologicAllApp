package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterProperty(
    @SerializedName("doc_count")
    val docCount: Int?,
    val `property`: Property?
)