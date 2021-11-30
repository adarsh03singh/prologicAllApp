package com.csestateconnect.db.data.events.eventsDataList


import com.google.gson.annotations.SerializedName

data class User(
    val email: String?,
    val id: Int?,
    @SerializedName("is_staff")
    val isStaff: Boolean?,
    @SerializedName("is_superuser")
    val isSuperuser: Boolean?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)