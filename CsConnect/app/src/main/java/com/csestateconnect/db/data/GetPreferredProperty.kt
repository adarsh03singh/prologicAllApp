package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferred_property")
data class GetPreferredProperty(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val icon_image: String
)
