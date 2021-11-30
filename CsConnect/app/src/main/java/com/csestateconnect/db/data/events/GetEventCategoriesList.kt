package com.csestateconnect.db.data.events


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.EventCategoriesListTypeConverters


@TypeConverters(EventCategoriesListTypeConverters::class)
@Entity(tableName = "events_categoriesList_data")
data class GetEventCategoriesList(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>?
)