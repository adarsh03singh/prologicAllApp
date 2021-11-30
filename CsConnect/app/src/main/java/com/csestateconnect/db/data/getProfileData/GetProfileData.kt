package com.csestateconnect.db.data.getProfileData

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.csestateconnect.db.data.typeconverter.ProfileConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(ProfileConverters::class)
@Entity(tableName = "GetProfileTable")
data class GetProfileData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val city: City?,
    val country: Country?,
    @SerializedName("created_at")
    val createdAt: String?,
    val email: String?,
    @SerializedName("email_verified")
    val emailVerified: Boolean?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("profile_image")
    val profileImage: String?,
    val rm: Rm?,
    val state: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val user: User?,
    val username: String?,
    @SerializedName("zip_code")
    val zipCode: String?
)
