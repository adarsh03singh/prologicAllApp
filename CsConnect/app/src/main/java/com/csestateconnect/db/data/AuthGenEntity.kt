package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Otp_Table")
data class AuthGenEntity(
    @PrimaryKey
    val pk: Int,

    val phone_number: String
)

@Entity(tableName = "Otp_Validate")
data class AuthValEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val is_existing_user: Boolean,

    val profile_updated: Boolean,

    val token: String,

    val staff: Boolean
)
