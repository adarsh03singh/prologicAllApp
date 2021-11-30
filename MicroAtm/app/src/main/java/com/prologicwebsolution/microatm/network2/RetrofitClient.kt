package com.prologicwebsolution.microatm.network2

import android.util.Log
import kotlinx.coroutines.TimeoutCancellationException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private const val MICROATM_BASE_URL = "https://microatm.org/"
private const val AEPS_BASE_URL = "https://app.youcloudpayment.in/"

class RetrofitClient {
    companion object {

        private fun getOkHttpClient(): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(null)
                .build()
            return okHttpClient
        }

        fun getClient(): ApiService? {
            val retrofit = Retrofit.Builder()
                .baseUrl(MICROATM_BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
            return retrofit

        }

        fun getClientAeps(): ApiService? {
            val retrofit = Retrofit.Builder()
                .baseUrl(AEPS_BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
            return retrofit
        }

        fun errorException(throwable: Throwable): String {
            var error_msg = ""
            if (throwable is SocketException) {
                error_msg = ("Socket Exception")
            } else if (throwable is HttpException) {
                error_msg = ("Http Exception")
                val body = throwable.response()?.errorBody().toString()
                Log.d("Error Body", body)
            } else if (throwable is UnknownHostException) {
                error_msg = ("Internet Connection Error")
            } else if (throwable is TimeoutException || throwable is TimeoutCancellationException) {
                error_msg = ("Timeout Exception")
            }
//            else if (throwable is Exception) {
//                error_msg = ("Unknown Exception")
//            }
            else {
                error_msg = (throwable.localizedMessage!!)
            }
            return error_msg
        }
    }

}