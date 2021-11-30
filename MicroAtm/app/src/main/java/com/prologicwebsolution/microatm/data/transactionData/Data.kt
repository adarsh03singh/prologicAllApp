package com.prologicwebsolution.microatm.data.transactionData

data class Data(
    val ID: String,
    val TransactionMode: Any,
    val amount: String,
    val amount_credited: String,
    val bankremarks: String,
    val cardno: String,
    val clientrefid: String,
    val date: String,
    val distributor_commission: String,
    val invoicenumber: String,
    val mid: String,
    val refstan: String,
    val requesttxn: Any,
    val respcode: Any,
    val retailer_commission: String,
    val rrn: String,
    val statuscode: String,
    val tid: String,
    val txnType: String,
    val txnamount: String,
    val udf1: Any,
    val udf2: Any,
    val udf3: Any,
    val udf4: Any,
    val user_id: String,
    val vendorid: Any,
    val wallet_transaction_id: String
)