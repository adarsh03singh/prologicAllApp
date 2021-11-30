package com.prologicwebsolution.eventshare.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prologicwebsolution.eventshare.data.loginData.Userdata

class LoginTypeConverters {
    @TypeConverter
    fun fromStringCity(value: String?): Userdata? {
        val listType = object : TypeToken<Userdata?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListCity(list: Userdata?): String? {
        return Gson().toJson(list)
    }

}