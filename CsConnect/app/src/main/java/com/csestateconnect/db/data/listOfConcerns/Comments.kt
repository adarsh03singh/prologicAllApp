package com.csestateconnect.db.data.listOfConcerns

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concern_comments")
data class Comments(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val body: String,
    val comment_by: Int,
    val concern_id: Int,
    val created_at: String,
    val updated_at: String
)