package com.csestateconnect.db.data.leads

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.LeadsTypeConverters

@TypeConverters(LeadsTypeConverters::class)
@Entity(tableName = "list_of_leads")
data class ListOfLeads(

    @PrimaryKey(autoGenerate = false)
    val count: Int,

    val next: String?,

    val previous: String?,

    val results: List<Result>
)

