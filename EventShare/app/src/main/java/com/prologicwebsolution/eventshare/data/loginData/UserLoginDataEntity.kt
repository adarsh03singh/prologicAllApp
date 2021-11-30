package com.prologicwebsolution.eventshare.data.loginData

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.prologicwebsolution.eventshare.typeConverters.LoginTypeConverters

@TypeConverters(LoginTypeConverters::class)
@Entity(tableName = "user_data_table")
data class UserLoginDataEntity(

    @PrimaryKey

    val msg: String,
    val status: String,
    val userdata: Userdata
)