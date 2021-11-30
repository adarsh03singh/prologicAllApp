package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterStatus(
    @SerializedName("doc_count")
    val docCount: Int?,
    val status: Status?
)