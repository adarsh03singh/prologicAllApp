package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NotificationTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.csestateconnect.db.data.notifications.Result> {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<com.csestateconnect.db.data.notifications.Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects1: List<com.csestateconnect.db.data.notifications.Result>): String {
        return Gson().toJson(someObjects1)
    }

}