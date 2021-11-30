package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.AnyTypeConverters

@TypeConverters(AnyTypeConverters::class)
@Entity(tableName = "CreateProfileTable")
data class CreateProfileData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val first_name: String,
    val last_name: String,
    val email: String,
    val country: String?,
    val city: String?,
    val state: String,
    val latitude: String,
    val longitude: String,
    val zip_code: String
)

