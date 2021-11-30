package com.csestateconnect.db.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "UserAcountDetail")
data class GetAccountDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val user: Int?,
    @SerializedName("account_holder_name")
    val accountHolderName: String?,
    @SerializedName("account_number")
    val accountNumber: String?,
    @SerializedName("account_status")
    val accountStatus: String?,
    val address: String?,
    @SerializedName("bank_name")
    val bankName: String?,
    val branch: String?,
    @SerializedName("canceled_cheque_image")
    val canceledChequeImage: String?,
    @SerializedName("correspondence_address")
    val correspondenceAddress: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("ifsc_code")
    val ifscCode: String?,
    @SerializedName("pan_card_image")
    val panCardImage: String?,
    @SerializedName("pan_card_number")
    val panCardNumber: String?,
    @SerializedName("status_reason")
    val statusReason: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val verified: Boolean?
)