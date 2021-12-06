package com.prologic.strains.viewmodel

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prologic.strains.R
import com.prologic.strains.model.ApiError
import com.prologic.strains.model.auth.RegisterResult
import com.prologic.strains.network.Repository
import com.prologic.strains.network.errorException
import com.prologic.strains.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreference = SharedPreference()
    val apiRepository = Repository()
    var input_first_name = MutableLiveData<String>("")
    var input_last_name = MutableLiveData<String>("")
    var input_email = MutableLiveData<String>("")
    var input_phone = MutableLiveData<String>("")
    var input_password = MutableLiveData<String>("")
    var driversLicenseName = MutableLiveData<String>("")
    var doctorRecName = MutableLiveData<String>("")
    var account_type = MutableLiveData<Int>(R.id.recreationalUserRb)
    val doctor_rec_lay = MutableLiveData<Int>(View.GONE)
    val isLoaderVisible = MutableLiveData<Boolean>(false)
    var clickType = 0
    var userResult = MutableLiveData<RegisterResult>()
    var errorMessage = MutableLiveData<String>("")
    var password_show = MutableLiveData<String>("Show")

    init {
        account_type.observeForever {
            if (it == R.id.recreationalUserRb) {
                doctor_rec_lay.value = View.GONE
                doctorRecName.value = ""
            } else {
                doctor_rec_lay.value = View.VISIBLE
            }
        }
        userResult.observeForever {
            showToast(it.message)
            val bundle = Bundle()
            bundle.putString("email", input_email.value!!)
            bundle.putString("password", input_password.value!!)
            getAppFragmentManager().setFragmentResult("register", bundle)
            getAppFragmentManager().popBackStack()
        }
    }


    fun setImageName(filePath: String) {
        if (clickType == 1)
            driversLicenseName.value = filePath
        else if (clickType == 2)
            doctorRecName.value = filePath
    }

    fun checkFileValidation() {
        if (driversLicenseName.value.isNullOrEmpty()) {
            errorMessage.value = "Please Upload Drivers License"
        } else if (doctorRecName.value.isNullOrEmpty() && account_type.value == R.id.medicalUserRb) {
            errorMessage.value = "Please Upload Doctor's Rec"
        } else
            register()
    }

    fun getFIleBody(key: String, filePath: String): MultipartBody.Part? {
        val file = File(filePath)
        val doctorRecordFileBody =
            MultipartBody.Part.createFormData(
                key,
                file.name,
                file.asRequestBody(("image/jpeg").toMediaTypeOrNull())
            )
        return doctorRecordFileBody
    }

    fun register() {

        val driverLicenseFileBody = getFIleBody("drivers_license", driversLicenseName.value!!)
        var doctorRecordFileBody: MultipartBody.Part? = null
        val username = input_email.value!!.split("@")[0]
        val params = HashMap<String, RequestBody>()
        if (account_type.value == R.id.recreationalUserRb) {
            params.put(
                "role",
                "um_recreational-user".toRequestBody("text/plain".toMediaTypeOrNull())
            )
            doctorRecordFileBody = null
        } else if (account_type.value == R.id.medicalUserRb) {
            params.put("role", "um_medical-user".toRequestBody("text/plain".toMediaTypeOrNull()))
            doctorRecordFileBody = getFIleBody("dr_record", doctorRecName.value!!)
        }
        params.put("username", username.toRequestBody("text/plain".toMediaTypeOrNull()))
        params.put(
            "firstName",
            input_first_name.value!!.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        params.put(
            "lastName",
            input_last_name.value!!.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        params.put("email", input_email.value!!.toRequestBody("text/plain".toMediaTypeOrNull()))
        params.put(
            "password",
            input_password.value!!.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        params.put("phone", input_phone.value!!.toRequestBody("text/plain".toMediaTypeOrNull()))
        isLoaderVisible.value = true

        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val response = apiRepository.createCustomer(
                        params,
                        driverLicenseFileBody!!,
                        doctorRecordFileBody
                    )
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.code == 200) {
                            userResult.postValue(result!!)
                        } else
                            errorMessage.postValue("Error")
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


