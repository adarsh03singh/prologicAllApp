package com.csestateconnect.db.data.listOfConcerns

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.ConcernsTypeConverters

@TypeConverters(ConcernsTypeConverters::class)
@Entity(tableName = "concern_list")
data class GetListOfConcerns(
    @PrimaryKey(autoGenerate = false)
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>?
)

