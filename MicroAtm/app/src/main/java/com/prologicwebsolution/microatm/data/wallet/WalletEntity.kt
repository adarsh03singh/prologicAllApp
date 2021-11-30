package com.prologicwebsolution.microatm.data.wallet

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_table")
data class WalletEntity(
        @PrimaryKey(autoGenerate = false)
        val id: Int,
    val balance: String,
    val commission: String,
    val status: String
)