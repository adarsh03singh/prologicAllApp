package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class PhaseCompletionStatus(
    val color: String?,
    @SerializedName("completion_percentage")
    val completionPercentage: Int?,
    val id: Int?,
    val name: String?
)