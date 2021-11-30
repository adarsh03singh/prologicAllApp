package com.csestateconnect.db.data.reimbursements.reimburseList


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.EventDataListTypeConverters
import com.csestateconnect.db.data.typeconverter.ReimburseDataListTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ReimburseDataListTypeConverters::class)
@Entity(tableName = "ReimbursDataList_table")
data class GetReimbursementListEntity(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>?
)