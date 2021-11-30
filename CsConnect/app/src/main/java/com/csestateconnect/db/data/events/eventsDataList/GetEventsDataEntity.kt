package com.csestateconnect.db.data.events.eventsDataList


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.EventCategoriesListTypeConverters
import com.csestateconnect.db.data.typeconverter.EventDataListTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(EventDataListTypeConverters::class)
@Entity(tableName = "eventsDataList_table")
data class GetEventsDataEntity(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>?
)