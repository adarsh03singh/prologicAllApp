package com.csestateconnect.db.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "VerificationData_table")
data class GetVerificationData(
    @PrimaryKey(autoGenerate = false)
    val user: Int?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id_card_image")
    val idCardImage: String?,
    @SerializedName("kyc_status")
    val kycStatus: String?,
    @SerializedName("status_reason")
    val statusReason: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val verified: Boolean?
)