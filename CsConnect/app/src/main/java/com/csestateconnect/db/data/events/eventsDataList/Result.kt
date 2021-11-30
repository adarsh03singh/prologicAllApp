package com.csestateconnect.db.data.events.eventsDataList


import com.google.gson.annotations.SerializedName

data class Result(
    val active: Boolean?,
    @SerializedName("auto_reminder")
    val autoReminder: Boolean?,
    val category: Category?,
    @SerializedName("created_at")
    val createdAt: String?,
    val date: String?,
    val description: String?,
    val id: Int?,
    val priority: Int?,
    val subject: String?,
    val time: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val user: User?
)