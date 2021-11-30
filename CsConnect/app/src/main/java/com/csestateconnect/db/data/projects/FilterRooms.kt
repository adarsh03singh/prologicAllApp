package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class FilterRooms(
    @SerializedName("doc_count")
    val docCount: Int?,
    val rooms: Rooms?
)