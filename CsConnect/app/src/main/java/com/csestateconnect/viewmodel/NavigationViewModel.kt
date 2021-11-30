package com.csestateconnect.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.repo.AccountDetailRepository
import com.csestateconnect.repo.ProfileRepository
import com.csestateconnect.repo.VerificationRepository
import com.csestateconnect.ui.home.HomeActivity
import com.csestateconnect.utils.Coroutines
import retrofit2.Response
import java.io.File
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestBuilder
import com.csestateconnect.R
import com.csestateconnect.db.data.*
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.repo.AuthRepository
import com.csestateconnect.ui.ValidateOtpActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_verification.*
import org.json.JSONObject


class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private val accountDataRepository: AccountDetailRepository = AccountDetailRepository(application)
    private val personalDataRepository: ProfileRepository = ProfileRepository(application)
    private val verifyRepository: VerificationRepository = VerificationRepository(application)
    private val accountDetailRepository: AccountDetailRepository =
        AccountDetailRepository(application)
    private val authRepository: AuthRepository = AuthRepository(application)

    var tabItem = MutableLiveData<Int>().apply { postValue(0) }
    // PERSONAL DETAILS
    var firstname = MutableLiveData<String>()
    var lastname = MutableLiveData<String>()
    var emailid = MutableLiveData<String>()
    var userCountry = MutableLiveData<String>()
    var userCity = MutableLiveData<String>()
    var cityIndexValue = MutableLiveData<Int>().apply { postValue(0) }
    var countryIndexValue  = MutableLiveData<Int>().apply { postValue(0) }
    var state = MutableLiveData<String>()
    var zipcode = MutableLiveData<String>()
//    var address = MutableLiveData<String>()
    var colorChangeButton = MutableLiveData<Boolean>().apply { postValue(false) }
    var canenablebutton = MutableLiveData<Boolean>().apply { postValue(true) }

    var profileCountValue = MutableLiveData<Int>()

    var progressBarPersonal = MutableLiveData<Int>().apply { postValue(View.GONE) }

    //VERIFICATION
    var idImageGet = MutableLiveData<String>()
    val id_proof_image = MutableLiveData<String>()
    val idProofImageSuccessful = MutableLiveData<Boolean>().apply { postValue(false) }

    var progressBarVerify = MutableLiveData<Int>().apply { postValue(View.GONE) }

    //ACCOUNT DETAILS
    var account_holder_name = MutableLiveData<String>()
    var bank_name = MutableLiveData<String>()
    var branch = MutableLiveData<String>()
    var account_number = MutableLiveData<String>()
    var ifsc_code = MutableLiveData<String>()
    var addressBank = MutableLiveData<String>()
    var pan_card_number = MutableLiveData<String>()
    var correspondence_address = MutableLiveData<String>()


    var progressBarAccount = MutableLiveData<Int>().apply { postValue(View.GONE) }

    val chequeImageGet = MutableLiveData<String>()
    val panImageGet = MutableLiveData<String>()

    var canceled_cheque_image = MutableLiveData<String>()
    var pan_card_image = MutableLiveData<String>()

    val chequeImageSuccessful = MutableLiveData<Boolean>().apply { postValue(false) }
    val panImageSuccessful = MutableLiveData<Boolean>().apply { postValue(false) }
    var imgValue: String? = null
    var acount: Int? = 0
     var panImgValue: String? = null
 var chequeImgValue: String? = null


    init {


        var userAccountOldData: List<GetAccountDetailEntity>? = null
        var profileOldData: List<GetProfileData>? = null
        var responseVerify : List<GetVerificationData>? = null
        Coroutines.io {
            try {
                 verifyRepository.getImageVerification().forEach {
                     imgValue  = it.idCardImage
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                userAccountOldData = accountDataRepository.getUserAccountDetails()
                userAccountOldData!!.forEach {
                    panImgValue = it.panCardImage
                    chequeImgValue = it.canceledChequeImage
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }



            profileOldData = personalDataRepository.getUserPersonalDetails()
            Coroutines.main {
                if (!profileOldData!!.isEmpty()) {
                    profileOldData!!.forEach {
                        firstname.value = it.firstName
                        lastname.value = it.lastName
                        emailid.value = it.email
                        state.value = it.state
                        try {
                            userCity.value = it.city!!.name
                            userCountry.value = it.country!!.name
                        } catch (e: java.lang.Exception){
                            e.printStackTrace()
                        }
                        zipcode.value = it.zipCode.toString()
                        try {
                            cityIndexValue.value = it.city!!.id
                            countryIndexValue.value = it.country!!.id
                        }catch (e: java.lang.Exception){
                          e.printStackTrace()
                        }

                    }
                }

                if (!userAccountOldData!!.isEmpty()) {
                    userAccountOldData!!.forEach {
                        if(it.accountHolderName != null ){
                            acount =1
                        }
                        account_holder_name.value = it.accountHolderName
                        bank_name.value = it.bankName
                        branch.value = it.branch
                        account_number.value = it.accountNumber
                        ifsc_code.value = it.ifscCode
                        addressBank.value = it.address
                        pan_card_number.value = it.panCardNumber
                        correspondence_address.value = it.correspondenceAddress
                    }
                }

            }
        }


    }


    fun cheque_image_click(view: View) {
        chequeImageGet.value = "CHEQUE"
    }

    fun pancard_image_click(view: View) {
        panImageGet.value = "PAN"
    }

//    fun id_proofImage_click(view: View) {
//        idImageGet.value = "idProofImage"
//    }

    fun nextPersonalInfo(view: View) {

        if (firstname.value.isNullOrEmpty()
            || state.value.isNullOrEmpty()  || userCountry.value.isNullOrEmpty()
            || userCity.value.isNullOrEmpty() || zipcode.value.isNullOrEmpty()
        ) {

            Snackbar.make(view, "Please fill all the details", Snackbar.LENGTH_LONG).show()
        } else if (!isEmailValid(emailid.value)){
            Snackbar.make(view, "Please enter valid Email Address", Snackbar.LENGTH_LONG).show()
        }
        else {
            progressBarPersonal.value = View.VISIBLE
            canenablebutton.value = false

            var response: Response<CreateProfileData>? = null
            Coroutines.io {
                response = personalDataRepository.updateProfileData(
                    firstname.value!!, lastname.value!!,
                    emailid.value!!, countryIndexValue.value!!,cityIndexValue.value!!,state.value!!, zipcode.value!!)

                if (response!!.isSuccessful) {
                    Coroutines.main {
                        progressBarPersonal.value = View.GONE
                        canenablebutton.value = true
                        Toast.makeText(view.context, "SuccessFully Updated Profile", Toast.LENGTH_LONG).show()
                        callUserProfileApi()
                        if(profileCountValue.value == 1){
                            view.findNavController().navigate(R.id.action_personalInfoFragment_to_header_view)
                        }else
                        tabItem.value = 2
                    }
                } else {
                    Coroutines.main {
                        progressBarPersonal.value = View.GONE
                        canenablebutton.value = true
                        try {
                            if (!response!!.errorBody()!!.equals(null)) {
                                val jObjError = JSONObject(response!!.errorBody()!!.string())
                                try {
                                    val email = jObjError.getString("email")
                                    Snackbar.make(view, email, Snackbar.LENGTH_LONG).show()
                                } catch (e: NullPointerException) {
                                e.printStackTrace()
                            }
                                val error = jObjError.getString("error")
                                Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }




    fun nextVerification(view: View) {
// FOR UPDATE VERIFICATION

        if (idProofImageSuccessful.value == false) {

            Snackbar.make(view, "Please add IdProof Image", Snackbar.LENGTH_LONG).show()

        } else {
            if (imgValue != null) {
                var file: File? = null
                progressBarVerify.value = View.VISIBLE
                canenablebutton.value = false
                file = File(id_proof_image.value!!)

                var response: retrofit2.Response<UpdateVerificationDataEntity>? = null
                Coroutines.main {
                    try {
                        response = verifyRepository.updateVerificationData(file)
                        Log.i("verification_", response?.body().toString())

                        if (response!!.isSuccessful) {
                            Toast.makeText(
                                this.getApplication(), "Successfully Updated", Toast.LENGTH_SHORT
                            ).show()
                            callVerificationGetApi()

                            if (profileCountValue.value == 4) {
                                view.findNavController()
                                    .navigate(R.id.action_verificationFragment_to_header_view)
                            } else {
                                tabItem.value = 3
                            }
                            canenablebutton.value = true
                        } else {
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
                            canenablebutton.value = true
                        }
                        progressBarVerify.value = View.GONE

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            } else {

                canenablebutton.value = false
                progressBarVerify.value = View.VISIBLE
                val file = File(id_proof_image.value!!)

                var response: retrofit2.Response<CreateVerificationDataEntity>? = null
                Coroutines.main {
                    response = verifyRepository.getVerificationData(file)
                    Log.i("verification_", response?.body().toString())

                    if (response!!.isSuccessful) {
                        Toast.makeText(
                            this.getApplication(), "Successfully KYC Created", Toast.LENGTH_SHORT
                        ).show()
                        callVerificationGetApi()
                        tabItem.value = 3
                        canenablebutton.value = true
                    } else {
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
                        canenablebutton.value = true
                    }
                    progressBarVerify.value = View.GONE
                }
            }
        }
    }

    fun sendAccountDetails(view: View) {



        colorChangeButton.value = false
        if (account_holder_name.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Account Holder Name", Snackbar.LENGTH_LONG).show()
        } else if (bank_name.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Bank Name", Snackbar.LENGTH_LONG).show()
        } else if (branch.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Branch Name", Snackbar.LENGTH_LONG).show()
        } else if (account_number.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Account Number", Snackbar.LENGTH_LONG).show()
        } else if (ifsc_code.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter IFSC Code", Snackbar.LENGTH_LONG).show()
        } else if (addressBank.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Address", Snackbar.LENGTH_LONG).show()
        } else if (pan_card_number.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Pan Card Number", Snackbar.LENGTH_LONG).show()
        } else if (correspondence_address.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter Correspondence Address", Snackbar.LENGTH_LONG).show()
        } else if (chequeImageSuccessful.value == false) {
            Snackbar.make(view, "Please add Cheque Image", Snackbar.LENGTH_LONG).show()
        } else if (panImageSuccessful.value == false) {
            Snackbar.make(view, "Please add PanCard Image", Snackbar.LENGTH_LONG).show()
        } else {
            progressBarAccount.value = View.VISIBLE
            canenablebutton.value = false

            // for update Account Details
            if( acount == 1){

                var response: retrofit2.Response<UpdateAccountDetailEntity>?= null
                Coroutines.main() {

                    val chequeFile = File(canceled_cheque_image.value!!)

                    val panFile = File(pan_card_image.value!!)

                        response = accountDetailRepository.getUpdateAccontDetail(
                            account_holder_name.value!!,
                            bank_name.value!!,
                            branch.value!!,
                            account_number.value!!,
                            ifsc_code.value!!,
                            addressBank.value!!,
                            pan_card_number.value!!,
                            correspondence_address.value!!,
                            chequeFile,
                            panFile
                        )
                        if (response!!.isSuccessful) {
                            callAccountGetApi()
                            Toast.makeText(this.getApplication(), "Successfully Updated", Toast.LENGTH_SHORT
                            ).show()

                            if(profileCountValue.value == 2 || profileCountValue.value == 3){
                                view.findNavController().navigate(R.id.action_accountDetailsFragment_to_header_view)
                            }else{

                                goToHomeActivity(view)
                            }
                            progressBarAccount.value = View.GONE
                            canenablebutton.value = true
                        }
                        else {
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
                                canenablebutton.value = true
                                progressBarAccount.value = View.GONE
                            }
                        }
                }

            }else {

                colorChangeButton.value = true
                progressBarAccount.value = View.VISIBLE
                canenablebutton.value = false

                Coroutines.io {
                    val chequeFile = File(canceled_cheque_image.value!!)

                    val panFile = File(pan_card_image.value!!)

                    val response = accountDetailRepository.getCreateAccon(
                        account_holder_name.value!!,
                        bank_name.value!!,
                        branch.value!!,
                        account_number.value!!,
                        ifsc_code.value!!,
                        addressBank.value!!,
                        pan_card_number.value!!,
                        correspondence_address.value!!,
                        chequeFile,
                        panFile

                    )
                    if (response!!.isSuccessful) {
                        callAccountGetApi()
                        Toast.makeText(this.getApplication(), "Successfully Account Created", Toast.LENGTH_SHORT
                        ).show()
                        goToHomeActivity(view)
                        canenablebutton.value = true

                    } else {
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
                            canenablebutton.value = true
                        }
                    }
                }

                progressBarAccount.value = View.GONE
            }
        }
    }


    fun callUserProfileApi() {
        Coroutines.io {
            try {
                personalDataRepository.getUserProfileData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

fun callVerificationGetApi() {
    Coroutines.io {
        try {
            verifyRepository.getVerifyImage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

    fun callAccountGetApi() {
        Coroutines.io {
            try {
                accountDetailRepository.getUserAccountData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun goToHomeActivity(view: View) {
        val activity = view.context as Activity
        Intent(activity, HomeActivity::class.java).also {
            activity.startActivity(it)
            activity.finish()
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun getCountriesData(): LiveData<List<Countries>> {
        return authRepository.getallLiveDataCountriesDatabase()
    }

}


