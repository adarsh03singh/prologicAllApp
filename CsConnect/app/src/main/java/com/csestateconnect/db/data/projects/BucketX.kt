package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class BucketX(
    @SerializedName("doc_count")
    val docCount: Int?,
    val key: Int?
)