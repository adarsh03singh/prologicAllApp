package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deal_status")
data class GetDealStatus(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val color: String,
    val staff_only: Boolean
)