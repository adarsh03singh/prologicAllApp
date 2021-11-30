package com.prologicwebsolution.microatm.data.payoutList

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.prologicwebsolution.microatm.util.PayoutListTypeConverter


@TypeConverters(PayoutListTypeConverter::class)
@Entity(tableName = "payout_list_table")
data class PayoutListEntity(
        @PrimaryKey(autoGenerate = false)

        val id: Int,
    val `data`: List<Data>,
    val msg: String,
    val status: String
)