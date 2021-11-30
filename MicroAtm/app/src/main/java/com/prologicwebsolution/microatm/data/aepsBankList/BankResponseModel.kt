package com.prologicwebsolution.microatm.data.aepsBankList



data class BankResModel(
    val codeValues: ArrayList<BankCodeValue>,
    val msg: String,
    val respcode: String,
    val status: Boolean
)

data class BankCodeValue(
    val code: String,
    val value: String
)