package com.csestateconnect.db.data.events


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.AnyTypeConverters
import com.google.gson.annotations.SerializedName


//@TypeConverters(AnyTypeConverters::class)
//@Entity(tableName = "CreateEventTable")
data class CreateEventData(
    @PrimaryKey(autoGenerate = true)
    val active: Boolean?,
    @SerializedName("auto_reminder")
    val autoReminder: Boolean?,
    val category: Int?,
    val date: String?,
    val description: String?,
    val priority: Int?,
    val subject: String?,
    val time: String?
)