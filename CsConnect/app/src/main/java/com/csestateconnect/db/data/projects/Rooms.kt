package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class Rooms(
    val buckets: List<BucketXXXXXXX?>?,
    @SerializedName("doc_count_error_upper_bound")
    val docCountErrorUpperBound: Int?,
    @SerializedName("sum_other_doc_count")
    val sumOtherDocCount: Int?
)