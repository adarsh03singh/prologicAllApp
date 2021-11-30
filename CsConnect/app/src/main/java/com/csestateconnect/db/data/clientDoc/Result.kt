package com.csestateconnect.db.data.clientDoc


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("broker_client")
    val brokerClient: Int?,
    @SerializedName("doc_url")
    val docUrl: String?,
    val id: Int?,
    val name: String?
)