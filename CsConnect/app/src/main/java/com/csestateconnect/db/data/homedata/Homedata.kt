package com.csestateconnect.db.data.homedata

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.HomedataTypeConverters

@TypeConverters(HomedataTypeConverters::class)
@Entity(tableName = "home_data")
data class Homedata(
    @PrimaryKey (autoGenerate = false)
    val id: Int,
    val projects: Projects,
    val brokers: Brokers,
    val commission: Commission
)
