package com.prologicwebsolution.microatm.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Constants {
 const   val BASE_URL = "http://199.34.22.225:9087/YouCloudMiddlewarePre/MobileReq/public/"
    var USER_NAME = "8982364218"
    var AEPS_MID = "PROL00000000002"
    var AEPS_TID = "PROL0002"
    var TAG = "AEPS_TAG"
}

fun hideSoftKeyBoard(view: View) {
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun hideSoftKeyBoard() {
    try {
        val view = fragmentActivity!!.getWindow().getDecorView().getRootView()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}