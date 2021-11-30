package com.csestateconnect.db.data.clientDoc


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "Verification_table")
data class CreateClientDocData(
    @SerializedName("broker_client")
    val brokerClient: Int?,
    @SerializedName("doc_url")
    val docUrl: String?,
    val name: String?
)