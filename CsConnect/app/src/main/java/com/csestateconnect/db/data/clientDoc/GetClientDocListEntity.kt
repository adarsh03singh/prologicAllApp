package com.csestateconnect.db.data.clientDoc


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.AnyTypeConverters
import com.csestateconnect.db.data.typeconverter.ClientDocListTypeConverters
import com.csestateconnect.db.data.typeconverter.ClientListTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ClientDocListTypeConverters::class)
@Entity(tableName = "clients_DocList_data")
data class GetClientDocListEntity(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result?>?
)