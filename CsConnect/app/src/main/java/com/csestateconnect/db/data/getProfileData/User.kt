package com.csestateconnect.db.data.getProfileData


import com.google.gson.annotations.SerializedName

data class User(
    val bankaccount: Bankaccount?,
    val kyc: Kyc?
)