package com.prologicwebsolution.microatm.data.payoutWithdrawl

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payout_WithdrawData_table")
data class PayoutDataEntity(
    @PrimaryKey(autoGenerate = false)

    val id: Int,
    val balance: Double,
    val msg: String,
    val status: String
)