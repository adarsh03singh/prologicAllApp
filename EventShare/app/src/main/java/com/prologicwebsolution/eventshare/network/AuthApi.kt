package com.prologicwebsolution.eventshare.network

import com.google.gson.GsonBuilder
import com.prologicwebsolution.eventshare.data.loginData.UserLoginDataEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


private const val BASE_URL = "https://gstsuvidhakendra.org.in"

private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val okHttp = OkHttpClient.Builder()
        .addInterceptor(logger)

interface AuthApi {


    //Call Login Api here
    @POST("/validate_user.php")
    suspend fun loginApi(@Body loginBody: LoginBody): Response<UserLoginDataEntity>
    


//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    //CREATING RETROFIT OBJECT
    companion object {
        //        val gson = Gson()
        var gson = GsonBuilder().setLenient().create()
        operator fun invoke(): AuthApi {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttp.build())
                    .build()
                    .create(AuthApi::class.java)
        }
    }
}

class LoginBody(
        userEmail: String, password: String
) {
    val email: String = userEmail
    val password: String = password
}

