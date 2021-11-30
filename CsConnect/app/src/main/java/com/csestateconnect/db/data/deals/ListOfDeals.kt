package com.csestateconnect.db.data.deals

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.DealsTypeConverters

@TypeConverters(DealsTypeConverters::class)
@Entity(tableName = "list_of_deals")
data class ListOfDeals(
    @PrimaryKey(autoGenerate = false)
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>
)