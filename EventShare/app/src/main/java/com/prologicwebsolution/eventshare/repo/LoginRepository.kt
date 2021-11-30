package com.prologicwebsolution.eventshare.repo

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.prologicwebsolution.eventshare.data.loginData.UserLoginDataEntity
import com.prologicwebsolution.eventshare.data.loginData.Userdata
import com.prologicwebsolution.eventshare.network.AuthApi
import com.prologicwebsolution.eventshare.network.LoginBody
import com.prologicwebsolution.microatm.database.MyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository
    (application: Application) {
    val database: MyDatabase

    init {
        database = MyDatabase.getInstance(application.applicationContext)
    }

   suspend fun userLogin(email: String, password: String): Response<UserLoginDataEntity> {

        val response = AuthApi().loginApi(LoginBody(email, password))


       when (response.isSuccessful) {

               true -> {
                   withContext(Dispatchers.IO) {
                       database.getUserDataDao.delete()
                       database.getUserDataDao.insert(response.body()!!)

                   }
               }
           }


       Log.d("loginResponce",response.toString())

        return response
    }

    fun getallUserDataDatabase(): List<UserLoginDataEntity> {
        return database.getUserDataDao.getAllData()
    }

}