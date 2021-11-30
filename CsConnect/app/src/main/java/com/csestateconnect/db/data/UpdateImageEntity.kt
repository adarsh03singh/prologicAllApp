package com.csestateconnect.db.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ProfileImage")
data class UpdateImageEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int? ,
    @SerializedName("profile_image")
    val profileImage: String?
)