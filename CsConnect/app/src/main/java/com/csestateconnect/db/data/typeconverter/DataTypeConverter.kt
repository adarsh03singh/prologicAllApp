package com.csestateconnect.db.data.typeconverter

import androidx.room.TypeConverter
import com.csestateconnect.db.data.commissionSlab.Project
import com.csestateconnect.db.data.commissionSlab.RelationshipManager
import com.csestateconnect.db.data.countries.City
import com.csestateconnect.db.data.favouriteProject.FavouriteProject
import com.csestateconnect.db.data.getProfileData.Country
import com.csestateconnect.db.data.getProfileData.Rm
import com.csestateconnect.db.data.getProfileData.User
import com.csestateconnect.db.data.projects.Facets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import java.util.*


// WILL IMPLEMENT THESE FOR BETTER PERFORMANCE.
// It's better to declare Converters as a singleton in order not to create any extra objects.
// You shoud also mark all inner converter functions with @JvmStatic so Room can use them as regular static functions.


//  Moshi Generic Type Try
//class AlltypeMoshiTypeConverters {
//
////    TypeConverter
//    val moshi = Moshi.Builder().build()
//    inline fun <reified T> fromJson(value: String): T? {
//        val jsonAdapter = moshi.adapter(T::class.java)
//        return jsonAdapter.fromJson(value)
//    }
//
//    @TypeConverter
//    inline fun <reified T> toJson(value: T): String {
//        val jsonAdapter = moshi.adapter(T::class.java)
//        return jsonAdapter.toJson(value)
//    }
//}

//  Asterisk(*) Try
//class MyPersonalTypeConverters {
//
//    @TypeConverter
//    inline fun <reified T> stringToSomeObjectList(data: String?): List<*>? {
//        if (data == null) {
//            return null
//        }
//
//        val listType = object : TypeToken<List<*>?>() {
//        }.type
//
//        return Gson().fromJson(data, listType)
//    }
//
//    @TypeConverter
//    inline fun <reified T> someObjectListToString(someObjects: List<*>?): String {
//        return Gson().toJson(someObjects)
//    }
//}

//  Generic Type Try
////  Try typeconverter for all data, link - https://stackoverflow.com/questions/45949584/how-does-the-reified-keyword-in-kotlin-work/45952201
////  Not useful
//inline fun <reified T: Any> String.toKotlinObject(): T {
//    val mapper = jacksonObjectMapper() // implement jacson module github
//    return mapper.readValue(this, T::class.java)
//}


//class AlltypeTypeConverters {
//
//    @TypeConverter
//    inline fun <reified T> stringToSomeObjectList(data: String?): T? {
//        if (data == null) {
//            return null
//        }
//
//        val listType = object : TypeToken<T?>() {
//        }.type
//
//        return Gson().fromJson(data, listType)
//    }
//
//    @TypeConverter
//    inline fun <reified T> someObjectListToString(someObjects: T?): String {
//        return Gson().toJson(someObjects)
//    }
//}

class ConcernsTypeConverters {
    // List<Concern.. Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.csestateconnect.db.data.listOfConcerns.Result> {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<com.csestateconnect.db.data.listOfConcerns.Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects1: List<com.csestateconnect.db.data.listOfConcerns.Result>): String {
        return Gson().toJson(someObjects1)
    }
}

class CountriesCityTypeConverters {

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<City> {
        if (data == null) {
            return Collections.emptyList() }

        val listType = object : TypeToken<List<City>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<City>): String {
        return Gson().toJson(someObjects)
    }
}


class AnyTypeConverters {

    @TypeConverter
    fun stringToSomeObjectList(data: String?): Any? {
        if (data == null) {
            return null }

        val listType = object : TypeToken<Any?>() {}.type
        return Gson().fromJson(data, listType)
    }


}

class ProfileConverters {


    @TypeConverter
    fun fromStringCity(value: String?): com.csestateconnect.db.data.getProfileData.City? {
        val listType = object : TypeToken<com.csestateconnect.db.data.getProfileData.City?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListCity(list: com.csestateconnect.db.data.getProfileData.City?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringCountry(value: String?): Country? {
        val listType = object : TypeToken<Country?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListCountry(list: Country?): String? {
        return Gson().toJson(list)
    }


    @TypeConverter
    fun fromStringUser(value: String?): User? {
        val listType = object : TypeToken<User?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListUser(list: User?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringRm(value: String?): Rm? {
        val listType = object : TypeToken<Rm?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListRm(list: Rm?): String? {
        return Gson().toJson(list)
    }

}


class CommissionTypeConverters(){
    @TypeConverter
    fun fromStringCommissionUser(value: String?): com.csestateconnect.db.data.commissionSlab.User? {
        val listType = object : TypeToken<com.csestateconnect.db.data.commissionSlab.User?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListCommissionUser(list: com.csestateconnect.db.data.commissionSlab.User?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringCommissionRM(value: String?): RelationshipManager? {
        val listType = object : TypeToken<RelationshipManager?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListCommissionRM(list: RelationshipManager?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringCommissionProject(value: String?): Project? {
        val listType = object : TypeToken<Project?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListCommissionProject(list: Project?): String? {
        return Gson().toJson(list)
    }
}

class FavouriteProjectsTypeConverters {

    // List<FavouriteProject>
    @TypeConverter
    fun stringfavouriteList(data: String?): List<FavouriteProject> {
        if (data == null) {
            return Collections.emptyList()
        }

        val favouriteListType = object : TypeToken<List<FavouriteProject>>() {}.type
        return Gson().fromJson(data, favouriteListType)
    }

    @TypeConverter
    fun favouriteListToString(someObjects: List<FavouriteProject>): String {
        return Gson().toJson(someObjects)
    }

}

class ProjectsTypeConverters {

    // List<Result>
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<com.csestateconnect.db.data.projects.Result> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<com.csestateconnect.db.data.projects.Result>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<com.csestateconnect.db.data.projects.Result>): String {
        return Gson().toJson(someObjects)
    }

    @TypeConverter
    fun fromStringFacets(value: String?): Facets? {
        val listType = object : TypeToken<Facets?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromListFacets(list: Facets?): String? {
        return Gson().toJson(list)
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

// TRY /*------------- Still have to try. Not tried yet. -------------*/
class tryconverter(){
//    @TypeConverter
//    fun fromstring(value: String?): List<T> {
//        val listtype = object: TypeToken<T>() {}.type
//        return Gson().fromJson(value, listtype)
//    }
//
//    @TypeConverter
//    fun tostring(list: T): String?{
//        return Gson().toJson(list)
//    }

    val moshi = Moshi.Builder().build()
    @TypeConverter
    inline fun <reified T> fromJson(value: String): T? {
        val jsonAdapter = moshi.adapter(T::class.java)
        return jsonAdapter.fromJson(value)
    }

    @TypeConverter
    inline fun <reified T> toJson(value: T): String {
        val jsonAdapter = moshi.adapter(T::class.java)
        return jsonAdapter.toJson(value)
    }
}
