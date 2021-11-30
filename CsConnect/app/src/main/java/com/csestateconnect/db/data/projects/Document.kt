package com.csestateconnect.db.data.projects


import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("document_type")
    val documentType: DocumentType?,
    @SerializedName("document_url")
    val documentUrl: String?,
    val id: Int?
)