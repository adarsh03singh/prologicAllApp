package com.prologicwebsolution.microatm.data.payoutList

data class Data(
    val account_no: String,
    val charge: String,
    val id: String,
    val process_time: String,
    val reqtype: String,
    val request: String,
    val request_amount: String,
    val request_time: String,
    val response: String,
    val status: String
)