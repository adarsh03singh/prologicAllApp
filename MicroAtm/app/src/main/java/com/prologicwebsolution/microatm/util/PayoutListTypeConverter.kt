package com.prologicwebsolution.microatm.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prologicwebsolution.microatm.data.transactionData.Data
import java.util.*

class PayoutListTypeConverter {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.prologicwebsolution.microatm.data.payoutList.Data> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<com.prologicwebsolution.microatm.data.payoutList.Data>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<com.prologicwebsolution.microatm.data.payoutList.Data>): String {
        return Gson().toJson(someObjects)
    }

}