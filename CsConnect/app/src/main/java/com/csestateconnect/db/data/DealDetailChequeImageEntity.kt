package com.csestateconnect.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deal_cheque")
data class DealDetailChequeImageEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val cheque_image: String?
)