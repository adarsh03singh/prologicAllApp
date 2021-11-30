package com.csestateconnect.db.data.countries


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.CountriesCityTypeConverters

@TypeConverters(CountriesCityTypeConverters::class)
@Entity(tableName = "countries_list")
data class Countries(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val three_digit_code: String,
    val two_digit_code: String,
    val number_code: String,
    val flag_image: String,
    val icon_image: String,
    val latitude: String,
    val longitude: String,
    val cities: List<City>,
    val created_at: String,
    val updated_at: String
)