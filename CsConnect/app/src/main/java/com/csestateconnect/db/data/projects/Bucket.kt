package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class Bucket(
    @SerializedName("doc_count")
    val docCount: Int?,
    val key: String?,
    @SerializedName("checked")
    var checked: Boolean?
)