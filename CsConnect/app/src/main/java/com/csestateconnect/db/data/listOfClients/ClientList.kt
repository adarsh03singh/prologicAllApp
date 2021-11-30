package com.csestateconnect.db.data.listOfClients


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.ClientListTypeConverters
import com.csestateconnect.db.data.typeconverter.NotificationTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ClientListTypeConverters::class)
@Entity(tableName = "clients_list_data")
data class ClientList(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result?>?
)