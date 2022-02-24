package com.prologic.strains.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.prologic.strains.model.auth.UserResult


class SharedPreference {
    private val appSharedPrefs: SharedPreferences =
        MyApplication.getMyApplication()
            .getSharedPreferences("appSharedPrefs", Context.MODE_PRIVATE)
    private val prefsEditor: SharedPreferences.Editor = appSharedPrefs.edit()

    fun clearSharedPrefernce() {
        prefsEditor.clear()
        prefsEditor.commit()
    }

    fun getIntger(key: String?): Int {
        return appSharedPrefs.getInt(key, 0)
    }

    fun putIntger(key: String, value: Int) {
        prefsEditor.putInt(key, value)
        prefsEditor.commit()
    }

    fun getString(key: String): String? {
        return appSharedPrefs.getString(key, "")
    }

    fun putString(key: String, value: String) {
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun putDouble(key: String, value: Double) {
        prefsEditor.putString(key, value.toString())
        prefsEditor.commit()
    }

    fun getDouble(key: String): Double {
        val value = appSharedPrefs.getString(key, "0.0")
        return value!!.toDouble()
    }

    fun putFloat(key: String, value: Float) {
        prefsEditor.putFloat(key, value!!)
        prefsEditor.commit()
    }

    fun getFloat(key: String): Float {
        return appSharedPrefs.getFloat(key, 0.0.toFloat())
    }

    fun getBoolean(key: String): Boolean {
        return appSharedPrefs.getBoolean(key, false)
    }

    fun putBoolean(key: String, value: Boolean) {
        prefsEditor.putBoolean(key, value)
        prefsEditor.commit()
    }

    fun getUser(): UserResult? {
        var registerResponse: UserResult? = null
        val json = appSharedPrefs.getString(user_data, "")
        if (json!!.isNotEmpty()) {
            registerResponse = gson.fromJson(json, UserResult::class.java)
        }
        return registerResponse
    }


}