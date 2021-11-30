package com.csestateconnect.db.data.reimbursements


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.EventCategoriesListTypeConverters
import com.csestateconnect.db.data.typeconverter.ReimburseTypeListTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ReimburseTypeListTypeConverters::class)
@Entity(tableName = "reimburse_type_data_table")
data class GetReimbursementTypeEntity(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>?
)