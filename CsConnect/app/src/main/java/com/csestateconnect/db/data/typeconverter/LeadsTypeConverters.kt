package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.csestateconnect.db.data.leads.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class LeadsTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Result> {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects1: List<Result>): String {
        return Gson().toJson(someObjects1)
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
    fun AnyListToString(someObjects2: Any?): String {
        return Gson().toJson(someObjects2)
    }
}

class LeadsResultTypeConverters {

    @TypeConverter
    fun fromStringcntry(value: String?): Country? {
        val listType = object : TypeToken<Country?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: Country?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringCity(value: String?): City? {
        val listType = object : TypeToken<City?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: City?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringloc(value: String?): Location? {
        val listType = object : TypeToken<Location?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: Location?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringls(value: String?): LeadStatus? {
        val listType = object : TypeToken<LeadStatus?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: LeadStatus?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringmbr(value: String?): ManagedByRm? {
        val listType = object : TypeToken<ManagedByRm?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: ManagedByRm?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringuser(value: String?): User? {
        val listType = object : TypeToken<User?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: User?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringtags(value: String?): Tags? {
        val listType = object : TypeToken<Tags?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListComm(list: Tags?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToAct(data: String?): List<CreateLeadActivity>? {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<CreateLeadActivity>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun actToString(someObjects3: List<CreateLeadActivity>?): String {
        return Gson().toJson(someObjects3)
    }

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<CreateInteractionDate>? {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<CreateInteractionDate>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects4: List<CreateInteractionDate>?): String {
        return Gson().toJson(someObjects4)
    }

    @TypeConverter
    fun stringToplList(data: String?): List<PreferredLocation>? {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<PreferredLocation>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun plListToString(someObjects5: List<PreferredLocation>?): String {
        return Gson().toJson(someObjects5)
    }

    @TypeConverter
    fun stringToSomeps(data: String?): List<PreferredStatu>? {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<PreferredStatu>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun psListToString(someObjects6: List<PreferredStatu>?): String {
        return Gson().toJson(someObjects6)
    }

    @TypeConverter
    fun stringToSomeppt(data: String?): List<PreferredPropertyType>? {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<PreferredPropertyType>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun pptListToString(someObjects7: List<PreferredPropertyType>?): String {
        return Gson().toJson(someObjects7)
    }

    @TypeConverter
    fun stringToany(data: String?): Any? {
        if (data == null) {
            return null }

        val listType = object : TypeToken<Any?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun anyToString(someObjects8: Any?): String {
        return Gson().toJson(someObjects8)
    }
}