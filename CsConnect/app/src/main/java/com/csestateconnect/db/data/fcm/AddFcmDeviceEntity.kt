package com.csestateconnect.db.data.fcm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fcm_device")
data class AddFcmDeviceEntity(
    @PrimaryKey(autoGenerate = false)
    val registration_id: String

)