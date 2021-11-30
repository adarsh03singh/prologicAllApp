package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.csestateconnect.db.data.deals.DealStatus
import com.csestateconnect.db.data.deals.Project
import com.csestateconnect.db.data.leads.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DealsTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.csestateconnect.db.data.deals.Result> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<com.csestateconnect.db.data.deals.Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<com.csestateconnect.db.data.deals.Result>): String {
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

class DealsResultTypeConverters {
    @TypeConverter
    fun fromStringdealStatus(value: String?): DealStatus? {
        val listType = object : TypeToken<DealStatus?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: DealStatus?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringproject(value: String?): Project? {
        val listType = object : TypeToken<Project?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: Project?): String? {
        return Gson().toJson(list)
    }
}