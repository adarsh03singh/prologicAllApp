package com.csestateconnect.db.data.clientDoc


import com.google.gson.annotations.SerializedName

data class UpdateClientdocData(
    val id: Int?,
    @SerializedName("doc_url")
    val docUrl: String?,
    val name: String?
)