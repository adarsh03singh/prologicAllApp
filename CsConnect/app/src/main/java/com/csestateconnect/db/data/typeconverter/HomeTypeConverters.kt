package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.csestateconnect.db.data.homedata.Brokers
import com.csestateconnect.db.data.homedata.Commission
import com.csestateconnect.db.data.homedata.Projects
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class HomeTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.csestateconnect.db.data.homepagedata.banner.Result> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<com.csestateconnect.db.data.homepagedata.banner.Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects1: List<com.csestateconnect.db.data.homepagedata.banner.Result>): String {
        return Gson().toJson(someObjects1)
    }

}

class HomedataTypeConverters {

    @TypeConverter
    fun fromStringProj(value: String?): Projects? {
        val listType = object : TypeToken<Projects?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListProj(list: Projects?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringBrok(value: String?): Brokers? {
        val listType = object : TypeToken<Brokers?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListBrok(list: Brokers?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringComm(value: String?): Commission? {
        val listType = object : TypeToken<Commission?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: Commission?): String? {
        return Gson().toJson(list)
    }
}