package com.prologicwebsolution.microatm.data.loginData

data class Userdata(
    val BC_id: String,
    val IMPS_charges: Int,
    val NEFT_charges: Int,
    val email: String,
    val name: String,
    val phone1: String,
    val user_id: Int
)