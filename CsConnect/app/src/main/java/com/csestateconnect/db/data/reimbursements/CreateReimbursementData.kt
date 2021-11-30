package com.csestateconnect.db.data.reimbursements


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CreateReimbursementData(
    @PrimaryKey(autoGenerate = true)
    val amount: String?,
    val type: Int?,
    val approved: Boolean?,
    val broker: Int?
)