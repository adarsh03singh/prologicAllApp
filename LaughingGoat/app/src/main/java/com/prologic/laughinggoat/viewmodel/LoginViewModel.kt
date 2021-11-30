package com.prologic.laughinggoat.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.prologic.laughinggoat.model.ApiError
import com.prologic.laughinggoat.model.auth.*
import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var input_first_name = MutableLiveData<String>("")
    var input_last_name = MutableLiveData<String>("")
    var input_email = MutableLiveData<String>("")
    var input_password = MutableLiveData<String>("")
    val header_title_text = MutableLiveData<String>("Login")
    val name_visibility = MutableLiveData<Int>(View.GONE)
    val sign_up_visibility = MutableLiveData<Int>(View.GONE)
    val sign_in_visibility = MutableLiveData<Int>(View.VISIBLE)
    val create_account_visibility = MutableLiveData<Int>(View.VISIBLE)
    val isLoaderVisible = MutableLiveData<Boolean>(false)
    var userResult = MutableLiveData<UserResult>()
    var errorMessage = MutableLiveData<String>("")
    var password_show = MutableLiveData<String>("Show")
    fun set(activity: Activity) {
        userResult.observeForever {
            sharedPreference.putString(user_email, input_email.value!!)
            sharedPreference.putString(user_password, input_password.value!!)
            Log.d(TAG, it.toString())
            errorMessage.postValue("Welcome " + it.first_name + " " + it.last_name)
            sharedPreference.putString(user_data, Gson().toJson(it))
            val intent = Intent()
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }

    }


    fun login() {
        isLoaderVisible.value=(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val response =
                        apiRepository.getCustomerLogin(input_email.value!!, input_password.value!!)
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Log.d(TAG, loginResponse.toString())
                        errorMessage.postValue("Welcome " + loginResponse!!.user_email)
                        sharedPreference.putString(user_login, Gson().toJson(loginResponse))
                        val customer_data: Customer =
                            apiRepository.getCustomerByEmail(input_email.value!!)
                        errorMessage.postValue("Welcome " + customer_data.customer.first_name + " " + customer_data.customer.last_name)
                        val userResultResult =
                            apiRepository.getCustomerById(customer_data.customer.id.toString())
                        userResult.postValue(userResultResult)
                    } else  {
                        val errorData =
                            gson.fromJson(response.errorBody()!!.string(), ApiError::class.java)
                        errorMessage.postValue(getHtmlSpanned(errorData.message).toString())
                    }
                }
            }.onSuccess {
                isLoaderVisible.value=(false)

            }.onFailure {
                isLoaderVisible.value=(false)
                errorMessage.postValue(errorException(it))
            }
        }
    }


    fun register() {
        val username = input_email.value!!.split("@")[0]
        val billing = Billing(
            first_name = input_first_name.value!!,
            last_name = input_last_name.value!!,
            address_1 = "",
            address_2 = "",
            city = "",
            company = "",
            state = "",
            country = "",
            postcode = "",
            phone = "",
            email = input_email.value!!,
        )
        val shipping = Shipping(
            first_name = input_first_name.value!!,
            last_name = input_last_name.value!!,
            address_1 = "",
            address_2 = "",
            city = "",
            company = "",
            state = "",
            country = "",
            postcode = "",
        )

        val registerRequest = RegisterRequest(
            username = username,
            first_name = input_first_name.value!!,
            last_name = input_last_name.value!!,
            email = input_email.value!!,
            password = input_password.value!!,
            shipping = shipping,
            billing = billing
        )
        Log.d(TAG, registerRequest.toString())

        isLoaderVisible.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val response = apiRepository.createCustomer(registerRequest)
                    if (response.isSuccessful)
                        userResult.postValue(response.body())
                    else {
                        val errorData =
                            gson.fromJson(response.errorBody()!!.string(), ApiError::class.java)
                        errorMessage.postValue(getHtmlSpanned(errorData.message).toString())
                    }
                }
            }.onSuccess {
                isLoaderVisible.postValue(false)
            }.onFailure {
                isLoaderVisible.postValue(false)
                errorMessage.postValue(errorException(it))
            }
        }

    }

    fun createAccount(view: View) {
        if (create_account_visibility.value == View.VISIBLE) {
            header_title_text.value = "Register"
            name_visibility.value = View.VISIBLE
            create_account_visibility.value = View.GONE
            sign_in_visibility.value = View.GONE
            sign_up_visibility.value = View.VISIBLE
        }
    }

    fun onBackPressed(): Boolean {
        if (create_account_visibility.value == View.GONE) {
            header_title_text.value = "Login"
            name_visibility.value = View.GONE
            create_account_visibility.value = View.VISIBLE
            sign_in_visibility.value = View.VISIBLE
            sign_up_visibility.value = View.GONE
            return false
        } else
            return true
    }
}


