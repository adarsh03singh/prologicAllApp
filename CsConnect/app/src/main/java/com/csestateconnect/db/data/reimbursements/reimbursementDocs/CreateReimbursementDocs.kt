package com.csestateconnect.db.data.reimbursements.reimbursementDocs


import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CreateReimbursementDocs(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("doc_url")
    val docUrl: String?,
    val name: String?,
    val reimbursement: Int?
)