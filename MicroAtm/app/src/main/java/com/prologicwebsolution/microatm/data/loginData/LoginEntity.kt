package com.prologicwebsolution.microatm.data.loginData

import com.prologicwebsolution.microatm.data.loginData.Userdata


data class LoginEntity(
    val id: Int,
    val msg: String,
    val status: String,
    val userdata: Userdata
)