package com.csestateconnect.db.data.reimbursements.reimbursementDocs


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("doc_url")
    val docUrl: String?,
    val id: Int?,
    val name: String?,
    val reimbursement: Int?
)