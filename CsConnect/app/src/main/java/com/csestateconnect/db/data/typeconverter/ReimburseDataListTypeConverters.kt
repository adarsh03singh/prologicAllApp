package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.csestateconnect.db.data.listOfClients.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ReimburseDataListTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.csestateconnect.db.data.reimbursements.reimburseList.Result> {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<com.csestateconnect.db.data.reimbursements.reimburseList.Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects1: List<com.csestateconnect.db.data.reimbursements.reimburseList.Result>): String {
        return Gson().toJson(someObjects1)
    }

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