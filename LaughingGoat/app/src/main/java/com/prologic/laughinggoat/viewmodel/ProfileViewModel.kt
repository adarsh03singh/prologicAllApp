package com.prologic.laughinggoat.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.google.gson.Gson
import com.prologic.laughinggoat.model.ApiError
import com.prologic.laughinggoat.model.auth.Billing
import com.prologic.laughinggoat.model.auth.RegisterRequest
import com.prologic.laughinggoat.model.auth.Shipping
import com.prologic.laughinggoat.model.auth.UserResult
import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.utils.*
import com.prologic.laughinggoat.view.activity.MainActivity
import com.prologic.laughinggoat.view.fragment.ShippingBilling
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var update_button = MutableLiveData<String>("Edit Profile")
    var password_show = MutableLiveData<String>("Show")
    val isLoaderVisible = MutableLiveData<Int>(0)
    var errorMessage = MutableLiveData<String>("")

    var registerResponse = MutableLiveData<UserResult>()
    val user_data = sharedPreference.getUser()
    var billing_details = MutableLiveData<String>("")
    var shipping_details = MutableLiveData<String>("")
    var input_first_name = MutableLiveData<String>("")
    var input_last_name = MutableLiveData<String>("")
    var input_email = MutableLiveData<String>("")
    var input_password = MutableLiveData<String>("")
    var billing: Billing = user_data!!.billing
    var shipping: Shipping = user_data!!.shipping

    fun setView(activity: Activity) {
        input_first_name.value = user_data?.first_name
        input_last_name.value = user_data?.last_name
        input_email.value = user_data?.email
        input_password.value = sharedPreference.getString(user_password)
        setBilling()
        setShipping()
        registerResponse.observeForever {
            sharedPreference.putString(user_email, input_email.value!!)
            sharedPreference.putString(user_password, input_password.value!!)
            Log.d(TAG, it.toString())
            sharedPreference.putString(com.prologic.laughinggoat.utils.user_data, Gson().toJson(it))
            SuccessAlert.showHtml(
                activity,
                "Your profile has been updated",
                object : OnDialogCloseListener {
                    override fun onClick() {
                        val intent = Intent(activity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        activity.startActivity(intent)
                        activity.finish()
                    }
                })
        }
    }

    private fun setBilling() {
        if (billing.first_name.isNotEmpty() && billing.last_name.isNotEmpty()) {
            var value = ""
            value += "<b>Name : " + billing.first_name + " " + billing.last_name + "</b><br> "
            value += "<b>Address : </b>" + billing.address_1 + " " + billing.address_2 + " " + billing.city + "<br> "
            if (billing.company.isNotEmpty())
                value += "<b>Company : " + billing.company + "</b><br> "
            value += billing.state + " " + billing.country + " Postcode <b>" + billing.postcode + "</b><br>"
            value += "<b>Contact : </b><br>Phone Number : " + billing.phone + "<br>Email Id : " + billing.email + "<br> "
            billing_details.value = value
        }
    }

    private fun setShipping() {
        if (shipping.first_name.isNotEmpty() && shipping.last_name.isNotEmpty()) {
            var value = ""
            value += "<b>Name : " + shipping.first_name + " " + shipping.last_name + "</b><br> "
            value += "<b>Address : </b>" + shipping.address_1 + " " + shipping.address_2 + " " + shipping.city + "<br> "
            if (shipping.company.isNotEmpty())
                value += "<b>Company : " + shipping.company + "</b><br> "
            value += shipping.state + " " + shipping.country + " Postcode <b>" + shipping.postcode + "</b><br>"
            shipping_details.value = value
        }
    }

    fun setBillingBundle(bundle: Bundle) {
        val billing2 = Billing(
            first_name = bundle.getString("firstName")!!,
            last_name = bundle.getString("lastName")!!,
            address_1 = bundle.getString("address_1")!!,
            address_2 = bundle.getString("address_2")!!,
            city = bundle.getString("cityName")!!,
            company = bundle.getString("companyName")!!,
            state = bundle.getString("stateName")!!,
            country = bundle.getString("countryRegionId")!!,
            postcode = bundle.getString("postcode")!!,
            phone = bundle.getString("phoneNumber")!!,
            email = bundle.getString("emailAddress")!!,
        )
        billing = billing2
        setBilling()
    }

    fun setShippingBundle(bundle: Bundle) {
        val shipping2 = Shipping(
            first_name = bundle.getString("firstName")!!,
            last_name = bundle.getString("lastName")!!,
            address_1 = bundle.getString("address_1")!!,
            address_2 = bundle.getString("address_2")!!,
            city = bundle.getString("cityName")!!,
            company = bundle.getString("companyName")!!,
            state = bundle.getString("stateName")!!,
            country = bundle.getString("countryRegionId")!!,
            postcode = bundle.getString("postcode")!!,
        )
        shipping = shipping2
        setShipping()

    }

    fun getBillingBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("firstName", billing.first_name)
        bundle.putString("lastName", billing.last_name)
        bundle.putString("address_1", billing.address_1)
        bundle.putString("address_2", billing.address_2)
        bundle.putString("companyName", billing.company)
        bundle.putString("countryRegionName", billing.country)
        bundle.putString("countryRegionId", billing.country)
        bundle.putString("stateName", billing.state)
        bundle.putString("cityName", billing.city)
        bundle.putString("postcode", billing.postcode)
        bundle.putString("phoneNumber", billing.phone)
        bundle.putString("emailAddress", billing.email)
        return bundle
    }

    fun getShippingBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("firstName", shipping.first_name)
        bundle.putString("lastName", shipping.last_name)
        bundle.putString("address_1", shipping.address_1)
        bundle.putString("address_2", shipping.address_2)
        bundle.putString("companyName", shipping.company)
        bundle.putString("countryRegionName", shipping.country)
        bundle.putString("countryRegionId", shipping.country)
        bundle.putString("stateName", shipping.state)
        bundle.putString("cityName", shipping.city)
        bundle.putString("postcode", shipping.postcode)
        return bundle
    }


    fun move_billing_page(view: View) {
        val fragment = ShippingBilling()
        val bundle = getBillingBundle()
        bundle.putString(view_type, com.prologic.laughinggoat.utils.billing)
        bundle.putString(title_name, "Billing Details")
        fragment.arguments = bundle
        addFragment(fragment)
    }

    fun move_shipping_page(view: View) {
        val fragment = ShippingBilling()
        val bundle = getShippingBundle()
        bundle.putString(view_type, com.prologic.laughinggoat.utils.shipping)
        bundle.putString(title_name, "Shipping Details")
        fragment.arguments = bundle
        addFragment(fragment)
    }


    fun updateProfile() {
        val registerRequest = RegisterRequest(
            username = "" + user_data!!.username,
            email = input_email.value!!,
            first_name = input_first_name.value!!,
            last_name = input_last_name.value!!,
            password = input_password.value!!,
            shipping = shipping,
            billing = billing
        )
        Log.d(TAG, registerRequest.toString())
        isLoaderVisible.postValue(1)
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val response =
                        apiRepository.updateCustomer(user_data.id.toString(), registerRequest)
                    if (response.isSuccessful) {
                        registerResponse.postValue(response.body())
                    } else {
                        val jsonString = response.errorBody()!!.string()
                        val errorData = gson.fromJson(jsonString, ApiError::class.java)
                        errorMessage.postValue(errorData.message)
                    }

                }
            }.onSuccess {
                isLoaderVisible.postValue(2)
            }.onFailure {
                isLoaderVisible.postValue(2)
                errorMessage.postValue(errorException(it))
            }
        }

    }


}


