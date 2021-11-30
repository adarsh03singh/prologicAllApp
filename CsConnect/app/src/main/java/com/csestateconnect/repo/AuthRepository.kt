package com.csestateconnect.repo

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.csestateconnect.GetNumber
import com.csestateconnect.db.*
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.network.AuthApi
import com.csestateconnect.db.data.AuthGenEntity
import com.csestateconnect.network.AuthValBody
import com.csestateconnect.db.data.AuthValEntity
import com.csestateconnect.network.AddFcmDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository
    (application: Application) {

    val database: MyDatabase
    var staffValue: Boolean = false
    lateinit var otpValue: String

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "tokenValue"
    val sharedPref: SharedPreferences = application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

//    val token = "Token " + sharedPref.getString("tokenValue", "")

    val sharedPrefer: SharedPreferences = application.getSharedPreferences("regToken", 0)
    val regToken = sharedPrefer.getString("regToken", "")

    init {
        database = MyDatabase.getInstance(
            application.applicationContext
        )
    }

    suspend fun getCountries(): Response<List<Countries>>? {

        val response = AuthApi().GetCountriesApi()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.countriesDao.delete()
                    database.countriesDao.insert(response.body()!!)

                    //For logging the response stored in database.
                    database.countriesDao.getAllData().forEach {
                        Log.i(
                            "tagdbdata_concheck",
                            "Id = ${it.id}, number code =  ${it.number_code},number name =  ${it.name}"
                        )
                        Log.i("tag countries all data", it.toString())
                    }
                }
            }
        }
        return response
    }

    suspend fun generateOtp(phoneNumber: String): Response<AuthGenEntity>? {

//        return otpResponse.value
        val response = AuthApi().AuthGen(GetNumber(phoneNumber))

//        val editor = sharedPref.edit()
//        editor.putInt(PREF_NAME, response.body()!!.pk)
//        editor.apply()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.authgenDao.delete()
                    database.authgenDao.insert(response.body()!!)

                }
            }
        }
        return response
    }

    suspend fun validateOtp(pkValue: Int): Response<AuthValEntity>? {

//        val pk = sharedPref.getInt(PREF_NAME, 0)
        val responseVal = AuthApi().AuthVal(AuthValBody(pkValue, otpValue))

        when (responseVal.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.authvalDao.delete()
                    database.authvalDao.insert(responseVal.body()!!)

                    //For logging the response stored in database.
                    database.authvalDao.getAllData().forEach {
                        Log.i("tagdbdataVal", "Id = ${it.id}, Token =  ${it.token}")

                        val editor = sharedPref.edit()
                        editor.putString(PREF_NAME, it.token)
                        editor.apply()
                        Log.i("tageditor", editor.toString())
                    }

                }
            }
        }

        return responseVal
    }

    suspend fun addFcmDevice(token: String) {
        val usertoken = "Token " + token
        val response = AuthApi().AddFcmDeviceApi(usertoken, AddFcmDevice("android", regToken, ""))
        Log.i("fcm response", response.toString())

        if (response.isSuccessful){
            database.addFcmDeviceDao.insert(response.body()!!)
        }
    }

    suspend fun deleteAllDataFromDatabase() {

        withContext(Dispatchers.IO) {
            database.authvalDao.delete()
            database.listOfLeadsDao.delete()
            database.createVerificationDao.delteCreateVerification()
            database.createAccountDetaileDao.deleteAccountDetail()
            database.getAccountDetaileDao.deleteUserAccountDetail()
            database.getVerificationDao.deleteGetVerificationData()
            database.authgenDao.delete()
            database.authvalDao.delete()
            database.createprofileDao.deleteProfile()
            database.getprofileDao.delete()
            database.getprofileImageDao.delete()
            database.homeDataDao.delete()
            database.projectsDataDao.delete()
            database.leadStatusDao.delete()
            database.completionStatusdao.delete()
            database.preferredPropertyDao.delete()
        }
    }


    fun getallDataCountriesFromDataBAse(): List<Countries> {
        return database.countriesDao.getAllData()
    }

    fun getallLiveDataCountriesDatabase(): LiveData<List<Countries>> {
        return database.countriesDao.getAllLiveData()
    }
}