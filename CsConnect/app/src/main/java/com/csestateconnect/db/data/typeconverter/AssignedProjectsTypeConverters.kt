package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.csestateconnect.db.data.assignedProjects.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class AssignedProjectsTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Result>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Result>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Result>?): String {
        return Gson().toJson(someObjects)
    }

    // Any
    @TypeConverter
    fun stringToAnyList(data: String?): Any? {
        if (data == null) {
            return null }

        val listType = object : TypeToken<Any?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun AnyListToString(someObjects: Any?): String {
        return Gson().toJson(someObjects)
    }

}
