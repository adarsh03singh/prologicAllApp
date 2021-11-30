package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account_Detail")
data class CreateAccountDetailEntity(
    @PrimaryKey(autoGenerate = false)
    val user: Int,
    val account_holder_name: String,
    val account_number: String,
    val bank_name: String,
    val ifsc_code: String,
    val pan_card_number: String,
    val address: String,
    val pan_card_image: String,
    val canceled_cheque_image: String,
    val verified: Boolean,
    val account_status: String,
    val status_reason: String,
    val branch: String,
    val correspondence_address: String
)