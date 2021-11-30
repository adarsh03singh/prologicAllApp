package com.prologic.laughinggoat.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter

import com.prologic.laughinggoat.R
import android.text.Html

import android.text.Spanned
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager

import android.widget.TextView


import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.prologic.laughinggoat.db.AppRoomDatabase
import com.prologic.laughinggoat.view.activity.MyImageView

import com.squareup.picasso.Picasso

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat


val TAG = "E-Prologic"
var application = MyApplication.getMyApplication()
val user_data = "user_data"
val user_login = "user_login"
val view_type = "view_type"
val currency = "$"
val intent_data = "intent_data"
val product_id = "product_id"
val title_name = "title_name"
val user_password = "user_password"
val user_email = "user_email"
var is_cart_update = true
val on_back_key = "on_back_key"
val billing_shipping = "billing_shipping"
val billing = "billing"
val shipping = "shipping"
var gson = Gson()

fun getProductUniqeKey(product_id: String, variation_id: String): String {
    return "${product_id}_${variation_id}"
}

fun getMyApplication(): Application {
    return MyApplication.getMyApplication()
}

fun getRoomDatabase(): AppRoomDatabase {
    return AppRoomDatabase.getRoomDatabase()
}

fun getString(id: Int): String {
    return application.resources.getString(id)
}

class UrlValue {
    companion object {
        const val squareup_version = "Square-Version: 2021-10-20"
        const val squareup_token =
            "Authorization: Bearer EAAAEKS2-xJiIZA0HPxshPKK4FhCnVY0bjsCfRr91licXXiQiN48AlnXWdjVeYTj"
        val squareup_payment = "https://connect.squareupsandbox.com/v2/"
        var consumer_key = "ck_f793162cea3427d716cd4b679ee19e7c40cd051d"
        var consumer_secret = "cs_a5e02fba404d8c117ab99a0b83434d6da86a3dde"
        var BASE_URL = "https://laughinggoatfarm.com/"
    }
}

fun hideSoftKeyBoard(activity: Activity) {
    val view = activity.currentFocus
    try {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun showToast(msg: String) {
    Toast.makeText(application, msg, Toast.LENGTH_LONG).show()
}

fun showToastShort(msg: String) {
    Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
}

fun deleteCardData() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        val error = "Error : " + throwable.localizedMessage
       // showToast(error)
    }
    val roomProductDao = getRoomDatabase().productRoomDao()
    CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
        roomProductDao.deleteTableData()
        val sharedPreferenceProduct = SharedPreferenceProduct()
        sharedPreferenceProduct.clear()
    }
}

@BindingAdapter("showImage", "imageTitle")
fun showImage(imageView: ImageView, imageUrl: String?, imageTitle: String?) {
    Picasso.get()
        .load(imageUrl)
        .placeholder(R.drawable.loading)
        .error(R.drawable.error)
        .resize(300, 300)
        .into(imageView)

    imageView.setOnClickListener {
        val intent = Intent(imageView.context, MyImageView::class.java)
        intent.putExtra("imageUrl", imageUrl)
        if (imageTitle != null)
            intent.putExtra("imageTitle", imageTitle)
        imageView.context.startActivity(intent)
    }

}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .placeholder(R.drawable.loading)
        .error(R.drawable.error)
        .resize(300, 300)
        .into(imageView)
}

@BindingAdapter("android:htmlText")
fun setHtmlTextValue(textView: TextView, htmlText: String?) {
    if (htmlText == null)
        return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        textView.text = Html.fromHtml(htmlText)
    }
}

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun getHtmlSpanned(htmlText: String): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(htmlText)
    }
}


@BindingAdapter("android:priceText")
fun getPriceTextView(textView: TextView, text: String?) {
    if (text == null)
        return
    textView.text = currency + text
}

fun roundOfDecimal(number: Double): Double {
    val df = DecimalFormat("#.##")
    return df.format(number).toDouble()
}

fun onSnackbar(view: View, msg: String) {
    val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
    snackbar.show()
}
