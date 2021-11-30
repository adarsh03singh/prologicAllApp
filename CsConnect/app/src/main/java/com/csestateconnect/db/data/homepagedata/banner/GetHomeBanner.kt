package com.csestateconnect.db.data.homepagedata.banner

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.HomeTypeConverters
import com.csestateconnect.db.data.typeconverter.HomedataTypeConverters
import com.csestateconnect.db.data.typeconverter.ProfileConverters

@TypeConverters(HomeTypeConverters::class)
@Entity(tableName = "home_banner")
data class GetHomeBanner(
    @PrimaryKey(autoGenerate = false)
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>?
)