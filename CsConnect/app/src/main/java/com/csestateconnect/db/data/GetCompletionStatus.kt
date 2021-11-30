package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completion_status")
data class GetCompletionStatus(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val code_name: String,
    val completion_percentage: Double,
    val color: String
)