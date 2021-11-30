package com.csestateconnect.db.data.listOfClients


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.AnyTypeConverters
import com.google.gson.annotations.SerializedName


@TypeConverters(AnyTypeConverters::class)
@Entity(tableName = "CreateClientTable")
data class CreateClientData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val address: String?,
    val city: Int?,
    val country: Int?,
    val email: String?,
    val location: Int?,
    val name: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)