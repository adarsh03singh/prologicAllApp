package com.csestateconnect.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.csestateconnect.R
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.repo.AuthRepository
import com.csestateconnect.ui.CreateProfileActivity
import com.csestateconnect.ui.GenerateOtpActivity
import com.csestateconnect.ui.ValidateOtpActivity
import com.csestateconnect.ui.home.HomeActivity
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.utils.Coroutines.io
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_walk_through.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Error
import java.security.AccessController.getContext
import java.util.ArrayList

class AuthViewModel(application: Application) : AndroidViewModel(application){


    private var snackbar: Snackbar? = null
    var numCode: String = ""
    private val repository: AuthRepository
    var numberText = MutableLiveData<String>()

    var otpView = MutableLiveData<String>().apply { postValue("") }
    var checkBox = MutableLiveData<Boolean>().apply { postValue(false) }
    var generateOtpButtonCount = MutableLiveData<Int>().apply { postValue(0) }
    var validateOtpButtonCount = MutableLiveData<Int>().apply { postValue(0) }

    var timer = MutableLiveData<String>()
    var canRestartTime = MutableLiveData<Boolean>().apply { postValue(false) }

    var progressBarGen = MutableLiveData<Int>()
    var progressBarVal = MutableLiveData<Int>()
    var profile_UserName = MutableLiveData<Boolean>()
    var userNumber = MutableLiveData<String>()
   lateinit var number:String
     var user_name:Boolean = false

    internal var country = arrayOf("INDIA +91", "USA +12", "UAE +971")

    init {
        repository = AuthRepository(application)
        io {
            try {
                repository.database.authvalDao.getAllData().forEach {
                    user_name= it.profile_updated
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                repository.database.authgenDao.getAllData().forEach {
                    number= it.phone_number
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                repository.getallDataCountriesFromDataBAse().forEach {
                    numCode = it.number_code
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Coroutines.main(){
            try {
                profile_UserName.value = user_name
            }
            catch (e:Exception){
                e.printStackTrace()
            }
            try {
                userNumber.value = number
            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }
        progressBarGen.value = View.GONE
        progressBarVal.value = View.GONE


    }


    fun AuthOtpGenerate(view: View) {

        progressBarGen.value = View.VISIBLE
        generateOtpButtonCount.value = 1
        io {
            var totalNumber = "+91" + numberText.value

            if (totalNumber.length > 12) {


                val responsegen = repository.generateOtp(totalNumber)

                    if (responsegen!!.isSuccessful) {
                        Coroutines.main {
                            progressBarGen.value = View.GONE
                            onclickToVal(view)
                            startTimer()
                            generateOtpButtonCount.value = 0
                            Toast.makeText(this.getApplication(), "OTP SENT", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                            Coroutines.main {
                        try {
                            if (!responsegen.errorBody()!!.equals(null)) {
                                val jObjError = JSONObject(responsegen.errorBody()!!.string())
                                try {
                                    generateOtpButtonCount.value = 0
                                    val phone_value = jObjError.getJSONArray("phone_number")
                                   val phone_numberError =  phone_value.getString(0)
                                    val snackbar = Snackbar.make(view, phone_numberError, Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                try {
                                    generateOtpButtonCount.value = 0
                                    val error = jObjError.getString("error")
                                    val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                    snackbar.show()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

//                                        Toast.makeText(this.getApplication(), error, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                                progressBarGen.value = View.GONE
                                generateOtpButtonCount.value = 0
                    }
                }


            } else {
                Coroutines.main {
                    progressBarGen.value = View.GONE
                    val snackbar = Snackbar.make(view, "Wrong Number", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    generateOtpButtonCount.value = 0

                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun AuthOtpValidate(view: View) {

        validateOtpButtonCount.value = 1
        repository.otpValue = otpView.value!!
        var pkValue: Int = 1
        var profile_updated: Boolean = false

        if (otpView.value?.length == 6) {
            progressBarVal.value = View.GONE

            io {

                repository.database.authgenDao.getAllData().forEach {
                    Log.i("tag pk value val", it.pk.toString())
                    pkValue = it.pk

                }
                val responseVal = repository.validateOtp(pkValue)

                        if (responseVal!!.isSuccessful) {

                            lateinit var token : String
                            repository.database.authvalDao.getAllData()
                                .forEach { profile_updated = it.profile_updated }

                            repository.database.authvalDao.getAllData()
                                .forEach { token = it.token }

                            if (profile_updated) {

                                val fcmResponse = repository.addFcmDevice(token)
                                Log.i("tagfcmresponse", fcmResponse.toString())

                                Coroutines.main {
                                    onclickToHome(view)
                                    Toast.makeText(view.context, "Successful Login", Toast.LENGTH_LONG).show()
                                }


                            } else {
                                onclickToProfile(view)

                            }
//                            validateOtpButtonCount.value = 0
                        }
                        else {
                            Coroutines.main {
                                try {
                                    if (!responseVal.errorBody()!!.equals(null)) {
                                        validateOtpButtonCount.value = 0
                                        val jObjError = JSONObject(responseVal.errorBody()!!.string())
                                        val error = jObjError.getString("error")
//                                        Toast.makeText(this.getApplication(), error, Toast.LENGTH_LONG).show()

                                        val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                        snackbar.show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                progressBarVal.value = View.GONE
                            }

                    }
                }

            progressBarVal.value = View.VISIBLE

        } else {
            val snackbar = Snackbar.make(view, "Wrong OTP", Snackbar.LENGTH_LONG)
            snackbar.show()
            validateOtpButtonCount.value = 0
        }
    }

    fun onclickToVal(view: View) {
        val activity = view.context as Activity
        Intent(activity, ValidateOtpActivity::class.java).also {
            activity.startActivity(it)
            activity.finish()
        }
    }

    fun onclickToProfile(view: View) {

        val activity = view.context as Activity
        Intent(activity, CreateProfileActivity::class.java).also {
            activity.startActivity(it)
            activity.finish()
        }
    }

    fun onclickToHome(view: View) {
        val activity = view.context as Activity
        Intent(activity, HomeActivity::class.java).also {
            activity.startActivity(it)
            activity.finish()
        }
    }


    fun startTimer() {
        object : CountDownTimer(20000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val seconds: String = millisUntilFinished.toString()
                if (seconds.length < 5) {
                    timer.value = "00:0${millisUntilFinished / 1000}"
                } else {
                    timer.value = "00:${millisUntilFinished / 1000}"
                }
            }

            override fun onFinish() {
                canRestartTime.value = true

            }
        }.start()
    }

    fun restartTimer(view: View) {
        var abc: String = "1"

        io {
            repository.database.authgenDao.getAllData().forEach {
                Log.i("tag phone number", it.phone_number)
                abc = it.phone_number
            }
            repository.generateOtp(abc)
        }
        startTimer()
        canRestartTime.value = false

    }

    fun getContriesWithCities() {

        io {
           val response = repository.getCountries()
            if(response!!.isSuccessful ) {
                repository.getallDataCountriesFromDataBAse().forEach {
                    numCode = it.number_code
                }
            }


        }
    }

}