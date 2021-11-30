package com.prologicwebsolution.microatm.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prologicwebsolution.microatm.data.transactionData.Data
import java.util.*

class TransactionTypeConverter {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Data> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Data>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Data>): String {
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