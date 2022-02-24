package com.prologic.strains.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.prologic.strains.model.ApiError
import com.prologic.strains.model.auth.Customer
import com.prologic.strains.model.auth.UserResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var input_email = MutableLiveData<String>("")
    var input_password = MutableLiveData<String>("")
    val isLoaderVisible = MutableLiveData<Boolean>(false)
    var userResult = MutableLiveData<UserResult>()
    var errorMessage = MutableLiveData<String>("")
    var password_show = MutableLiveData<String>("Show")


    fun login() {
        isLoaderVisible.value = true
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val response =
                        apiRepository.getCustomerLogin(input_email.value!!, input_password.value!!)
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Log.d(TAG, loginResponse.toString())
                        errorMessage.postValue("Welcome " + loginResponse!!.user_email)
                        sharedPreference.putString(user_login, gson.toJson(loginResponse))
                        val customer_data: Customer =
                            apiRepository.getCustomerByEmail(input_email.value!!)

                        val userResultResult =
                            apiRepository.getCustomerById(customer_data.customer.id.toString())
                        sharedPreference.putString(user_email, input_email.value!!)
                        sharedPreference.putString(user_password, input_password.value!!)
                        sharedPreference.putString(user_data, gson.toJson(userResultResult))
                        userResult.postValue(userResultResult)


                    } else {
                        val errorData =
                            gson.fromJson(response.errorBody()!!.string(), ApiError::class.java)
                        errorMessage.postValue(getHtmlSpanned(errorData.message).toString())
                    }
                }
            }.onSuccess {
                isLoaderVisible.value = false

            }.onFailure {
                isLoaderVisible.value = false
                errorMessage.postValue(errorException(it))
            }
        }
    }


}


