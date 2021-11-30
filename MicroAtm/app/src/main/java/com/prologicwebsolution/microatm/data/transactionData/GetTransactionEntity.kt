package com.prologicwebsolution.microatm.data.transactionData

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.prologicwebsolution.microatm.util.TransactionTypeConverter

@TypeConverters(TransactionTypeConverter::class)
@Entity(tableName = "transaction_detail_table")
data class GetTransactionEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val msg: String,
    val status: String,
    val data: List<Data>
)