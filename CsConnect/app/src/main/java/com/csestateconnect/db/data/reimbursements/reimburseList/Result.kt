package com.csestateconnect.db.data.reimbursements.reimburseList


import com.google.gson.annotations.SerializedName

data class Result(
    val amount: String?,
    val approved: Boolean?,
    val broker: Int?,
    @SerializedName("get_docs")
    val getDocs: List<Any>?,
    val id: Int?,
    val type: Type?
)