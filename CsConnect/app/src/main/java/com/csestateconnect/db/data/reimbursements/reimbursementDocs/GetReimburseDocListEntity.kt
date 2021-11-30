package com.csestateconnect.db.data.reimbursements.reimbursementDocs


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.ReimburseDocListTypeConverters
import com.csestateconnect.db.data.typeconverter.ReimburseTypeListTypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ReimburseDocListTypeConverters::class)
@Entity(tableName = "reimburse_docsList_table")
data class GetReimburseDocListEntity(
    @PrimaryKey(autoGenerate = false)
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>?
)