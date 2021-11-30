package com.prologicwebsolution.microatm.repo

import android.app.Application
import com.prologicwebsolution.microatm.data.loginData.LoginEntity
import com.prologicwebsolution.microatm.network2.CreateLoginBody
import com.prologicwebsolution.microatm.network2.RetrofitClient
import retrofit2.Response

class LoginRepository
    (application: Application) {

    suspend fun userLogin(request: CreateLoginBody): Response<LoginEntity> {
        val response = RetrofitClient.getClient()?.LoginApi(request)
        return response!!
    }

}