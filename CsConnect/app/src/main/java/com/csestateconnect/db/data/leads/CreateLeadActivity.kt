package com.csestateconnect.db.data.leads

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lead_activity")
data class CreateLeadActivity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val lead: Int,
    val short_description: String,
    val date: String
)