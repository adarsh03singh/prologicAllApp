package com.csestateconnect.db.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Verification_table")
data class CreateVerificationDataEntity(
    @PrimaryKey(autoGenerate = false)
    val user: Int?,
    val id_card_image: String?,
    val verified: Boolean?,
    val kyc_status: String?,
    val status_reason: String?
)
