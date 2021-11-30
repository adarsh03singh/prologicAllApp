package com.csestateconnect.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csestateconnect.db.data.CreateProfileData
import com.csestateconnect.db.data.UpdateImageEntity
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.repo.AccountDetailRepository
import com.csestateconnect.repo.AuthRepository
import com.csestateconnect.repo.ProfileRepository
import com.csestateconnect.repo.VerificationRepository
import com.csestateconnect.ui.home.HomeActivity
import com.csestateconnect.utils.Coroutines
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProfileRepository
    private val verifyRepository: VerificationRepository
    private val userAccountRepository: AccountDetailRepository
    private val authRepository: AuthRepository


    var idImageGet = MutableLiveData<String>()
    val profile_image = MutableLiveData<String>()
    val profileImageSuccessful = MutableLiveData<Boolean>().apply { postValue(false) }
    var imageResponse = MutableLiveData<Boolean>().apply { postValue(false) }
    var imageValue: String? = null
    var profileImgeView = MutableLiveData<String>()
    var imageValue2: String? = null


    //for create profile
    var account_holder_name = MutableLiveData<String>()
    var bankWithbranch_name = MutableLiveData<String>()
    var account_number = MutableLiveData<String>()
    var ifsc_code = MutableLiveData<String>()
    var addressBank = MutableLiveData<String>()
    var pan_card_number = MutableLiveData<String>()
    var correspondence_address = MutableLiveData<String>()
    var accountVerifiedOn = MutableLiveData<String>()
    var accountVerifiedStatus = MutableLiveData<String>()
    var accountVerifiedReason = MutableLiveData<String>()



    var progressBarAccount = MutableLiveData<Int>().apply { postValue(View.GONE) }

    val chequeImageGet = MutableLiveData<String>()
    val panImageGet = MutableLiveData<String>()

    var canceled_cheque_image = MutableLiveData<String>()
    var pan_card_image = MutableLiveData<String>()

    //for create profile
    var firstname = MutableLiveData<String>().apply { postValue("") }
    var lastname = MutableLiveData<String>().apply { postValue("") }
    var emailid = MutableLiveData<String>().apply { postValue("") }
    var state = MutableLiveData<String>().apply { postValue("") }
    var zipcode = MutableLiveData<String>().apply { postValue("") }
    var address = MutableLiveData<String>().apply { postValue("") }
    var userCountry = MutableLiveData<String>().apply { postValue("") }
    var cityIndexValue = MutableLiveData<Int>().apply { postValue(0) }
    var countryIndexValue  = MutableLiveData<Int>().apply { postValue(0) }
    var userCity = MutableLiveData<String>().apply { postValue("") }
    var canenablebutton = MutableLiveData<Boolean>().apply { postValue(true) }
    var progressBarCreateProf = MutableLiveData<Int>()

    //for get profile from server
    var userDetail = ArrayList<String?>()
    var userCreatedAt = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var userEmailId = MutableLiveData<String>()
    var userAddress = MutableLiveData<String>()
    var userMobile = MutableLiveData<String>()
    var infoCount = MutableLiveData<Int>().apply { postValue(0) }
    var accountCount = MutableLiveData<Int>().apply { postValue(0) }
    var updatePanCount = MutableLiveData<Int>().apply { postValue(0) }
    var verificationCount = MutableLiveData<Int>().apply { postValue(0) }

    //for get verify from server
    var verifyDataArray = ArrayList<String?>()
    var AccountDataArray = ArrayList<String?>()
    var verifiedData = MutableLiveData<String>()
    var verifiedOn = MutableLiveData<String>()
    var imgValueVerify: String? = null
    var idCardImgValue: String? = null

    var panImg: String? = null
    var chequeimg: String? = null

    val sharedPref: SharedPreferences = application.getSharedPreferences("tokenValue", 0)
    val token ="Token " + sharedPref.getString("tokenValue", "")

    init {
        authRepository = AuthRepository(application)
        repository = ProfileRepository(application)
        verifyRepository = VerificationRepository(application)
        userAccountRepository = AccountDetailRepository(application)
        progressBarCreateProf.value = View.GONE
        Coroutines.io {

            try {
                verifyRepository.getImageVerification().forEach {
                    idCardImgValue  = it.idCardImage
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
                        try {
                repository.getUserProfileData()
             } catch (e: Exception) {
            e.printStackTrace()
        }
        }

    }


    fun getCountriesData():  LiveData<List<Countries>>{
        return authRepository.getallLiveDataCountriesDatabase()
    }

    @SuppressLint("SimpleDateFormat")
    fun getVerifyAndAccountData(){
        Coroutines.io {

            try {
                verifyRepository.getImageVerification().forEach {
                    imgValueVerify = it.idCardImage
                    verifyDataArray.add(it.verified.toString())
                    verifyDataArray.add(it.createdAt)
                    Log.i("profileApi",it.idCardImage )
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                // getting account data from database
                userAccountRepository.getUserAccountDetails().forEach {
                    panImg = it.panCardImage
                    chequeimg = it.canceledChequeImage
                    AccountDataArray.add(it.accountHolderName)
                    AccountDataArray.add(it.bankName)
                    AccountDataArray.add(it.accountNumber)
                    AccountDataArray.add(it.branch)
                    AccountDataArray.add(it.ifscCode)
                    AccountDataArray.add(it.address)
                    AccountDataArray.add(it.accountStatus)
                    AccountDataArray.add(it.createdAt)
                    AccountDataArray.add(it.updatedAt)
                    AccountDataArray.add(it.statusReason)
                    AccountDataArray.add(it.correspondenceAddress)
                    AccountDataArray.add(it.panCardNumber)
                    AccountDataArray.add(it.verified.toString())
                    AccountDataArray.add(it.panCardImage)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            Coroutines.main {

                val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val outputFormatter = SimpleDateFormat("dd/MM/yyy")
                var date: Date? = null


                // getting verification data
                try {
                    verifiedData.value = verifyDataArray.get(0)

                    try {
                        date = inputFormatter.parse(verifyDataArray.get(1))
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    verifiedOn.value = outputFormatter.format(date)
                }catch (e: Exception){
                    e.printStackTrace()
                }



                // setting account detail data with text
                try {
                    account_holder_name.value = AccountDataArray.get(0)
                    bankWithbranch_name.value =
                        AccountDataArray.get(1) + "," + AccountDataArray.get(3)
                    account_number.value = AccountDataArray.get(2)
                    ifsc_code.value = AccountDataArray.get(4)
                    pan_card_number.value = AccountDataArray.get(11)
                    correspondence_address.value = AccountDataArray.get(10)

                    try {
                        date = inputFormatter.parse(AccountDataArray.get(7))
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    accountVerifiedOn.value = outputFormatter.format(date)
                    accountVerifiedStatus.value = AccountDataArray.get(12)
                    accountVerifiedReason.value = AccountDataArray.get(9)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }



    fun checkfields(view: View){
        if (firstname.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter First Name", Snackbar.LENGTH_LONG).show()
        }
        else if (!isEmailValid(emailid.value)){
            Snackbar.make(view, "Please enter valid Email Address", Snackbar.LENGTH_LONG).show()
        }
        else if (userCountry.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter Country", Snackbar.LENGTH_LONG).show()
        }
        else if (userCity.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter City", Snackbar.LENGTH_LONG).show()
        }

        else if (state.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter State", Snackbar.LENGTH_LONG).show()
        }
        else if (zipcode.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter ZipCode", Snackbar.LENGTH_LONG).show()
        }
       /* else if (address.value.isNullOrEmpty()){
            Toast.makeText(this.getApplication(), "Please enter Address", Toast.LENGTH_SHORT).show()
        }*/
        else{
            canenablebutton.value = false
            createProfile(view)
        }
    }

    fun createProfile(view: View){
        progressBarCreateProf.value = View.VISIBLE
        var response : Response<CreateProfileData>?
        Coroutines.io {
            response = repository.getCreateProfile(firstname.value!!, lastname.value,
                emailid.value!!,countryIndexValue.value, cityIndexValue.value, state.value!!, zipcode.value!!)

            if (response!!.isSuccessful){
                Coroutines.main {

                    callUserProfileGetApi()
                    authRepository.addFcmDevice(token)
                    Snackbar.make(view, "User Profile Updated", Snackbar.LENGTH_LONG).show()
                    onclickToHome(view)
                    progressBarCreateProf.value = View.GONE

                }
            } else {
                Coroutines.main {

                    try {
                        if (!response!!.errorBody()!!.equals(null)) {
                            canenablebutton.value = true
                            val jObjError = JSONObject(response!!.errorBody()!!.string())
                                val error = jObjError.getString("error")
                                val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                snackbar.show()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    progressBarCreateProf.value = View.GONE
                }
            }
        }

    }

    fun getProfile(){
//        progressBarCreateProf.value = View.VISIBLE
        Coroutines.io {

            try {
                repository.getUserPersonalDetails().forEach {
                    imageValue = it.profileImage
                    userDetail.add(it.firstName)
                    userDetail.add(it.lastName)
                    userDetail.add(it.state)
                    userDetail.add(it.email)
                    userDetail.add(it.phoneNumber)
                    userDetail.add(it.createdAt)
                    userDetail.add(it.city!!.name)
                    userDetail.add(it.country!!.name)

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            Coroutines.main {
                try {

                    profileImgeView.value = imageValue
                    username.value = "Hi ! " + userDetail.get(0) + " " + userDetail.get(1)
                    userAddress.value = userDetail.get(2) + "," + userDetail.get(7)
                    userEmailId.value = userDetail.get(3)
                    userMobile.value = userDetail.get(4)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val outputFormatter = SimpleDateFormat("dd/MM/yyy")
                var date: Date? = null
                try {
                    date = inputFormatter.parse(userDetail.get(5))
                } catch (e: ParseException) {
                    e.printStackTrace()
                }


                userCreatedAt.value =
                    "Joined on " + outputFormatter.format(date)//userDetail.get(5)
                Log.i("profileTag", "API Working!!")

//                    progressBarCreateProf.value = View.GONE
            }
        }


    }

    // FOR UPDATE PROFILE IMAGE
    fun updateImage(view: View) {
        if (profileImageSuccessful.value == false) {
            Toast.makeText(this.getApplication(), "Please add Image", Toast.LENGTH_SHORT).show()
        } else {
//                progressBarVerify.value = View.VISIBLE
            val file = File(profile_image.value!!)
            Coroutines.main {
              val  response = repository.getProfileImage(file)

                if (response!!.isSuccessful) {
                    val snackbar = Snackbar.make(view, "successfully Uploaded", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    callUserProfileGetApi()
                    profileImgeView.value = response.body()!!.profileImage
                }
                else{
                    Coroutines.main {
                        try {
                            if (!response!!.errorBody()!!.equals(null)) {
                                val jObjError = JSONObject(response!!.errorBody()!!.string())
                                val error = jObjError.getString("error")

                                val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }


    fun deleteImage(view: View) {
            Coroutines.main {
               val response = repository.deleteProfileImage()

                if (response!!.isSuccessful) {
                    val snackbar = Snackbar.make(view, "successfully Removed", Snackbar.LENGTH_LONG)
                    snackbar.show()
                    callUserProfileGetApi()
                    profileImgeView.value = response.body()!!.profileImage
                }
                else{
                    Coroutines.main {
                        try {
                            if (!response.errorBody()!!.equals(null)) {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                val error = jObjError.getString("error")
                                val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
        }
    }


    fun image_click(view: View) {
        idImageGet.value = "profileImage"
    }


    fun getimageFromDatabase() {
        Coroutines.io{
            repository.getProfileImageFromDatabase().forEach {
                imageValue =  it.profileImage
            }

            Coroutines.main {
                if(imageValue != null){
                    profileImgeView.postValue(imageValue)
                }
            }
        }
    }

    fun callUserProfileGetApi() {
        Coroutines.io {
            try {
                repository.getUserProfileData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun goToProfileInfo(view: View){
        infoCount.value = 1
    }
    fun goToAccountDetails(view: View){
        accountCount.value = 2
    }
    fun goToAccountDetailByPan(view: View){
        updatePanCount.value = 3
    }
    fun goToVerification(view: View){
        verificationCount.value = 4
    }


    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun onclickToHome(view: View) {
        val activity = view.context as Activity
        Intent(activity, HomeActivity::class.java).also {
            activity.startActivity(it)
            activity.finish()
        }
    }

}