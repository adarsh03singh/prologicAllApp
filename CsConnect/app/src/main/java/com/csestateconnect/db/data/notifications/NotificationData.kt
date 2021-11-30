package com.csestateconnect.db.data.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.NotificationTypeConverters

@TypeConverters(NotificationTypeConverters::class)
@Entity(tableName = "notification_data")
data class NotificationData(
    @PrimaryKey(autoGenerate = false)
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>?
)