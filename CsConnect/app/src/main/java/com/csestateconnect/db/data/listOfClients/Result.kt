package com.csestateconnect.db.data.listOfClients


import com.google.gson.annotations.SerializedName

data class Result(
    val address: String?,
    val broker: Int?,
    val city: City?,
    val country: Country?,
    val email: String?,
    @SerializedName("get_docs")
    val getDocs: List<Any?>?,
    val id: Int?,
    val location: Location?,
    val name: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?
)