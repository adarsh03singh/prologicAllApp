package com.csestateconnect.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateConverter() {

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(convertToDate: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = SimpleDateFormat("dd/MM/yyyy")
        var date: Date? = null
        var finalDate: String = ""

        try {
            date = inputFormatter.parse(convertToDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        finalDate = outputFormatter.format(date)

        return finalDate
    }
}

class DatenTimeConverter() {

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(convertToDate: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z")
        var date: Date? = null
        var finalDate: String = ""

        try {
            date = inputFormatter.parse(convertToDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        finalDate = outputFormatter.format(date)

        return finalDate
    }
}

class DatenTimeChatConverter() {

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(convertToDate: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = SimpleDateFormat("dd MMM yyyy HH:mm")
        var date: Date? = null
        var finalDate: String = ""

        try {
            date = inputFormatter.parse(convertToDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        finalDate = outputFormatter.format(date)

        return finalDate
    }
}