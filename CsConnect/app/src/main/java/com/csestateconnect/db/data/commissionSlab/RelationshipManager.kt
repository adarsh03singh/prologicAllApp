package com.csestateconnect.db.data.commissionSlab


import com.google.gson.annotations.SerializedName

data class RelationshipManager(
    @SerializedName("first_name")
    val firstName: String?,
    val id: Int?,
    @SerializedName("last_name")
    val lastName: String?
)