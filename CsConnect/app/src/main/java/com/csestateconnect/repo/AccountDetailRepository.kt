package com.csestateconnect.repo

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.db.data.CreateAccountDetailEntity
import com.csestateconnect.db.data.GetAccountDetailEntity
import com.csestateconnect.db.data.GetVerificationData
import com.csestateconnect.db.data.UpdateAccountDetailEntity
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.network.AuthApi
import com.csestateconnect.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class AccountDetailRepository
    (application: Application) {
    lateinit var response : Response<CreateAccountDetailEntity>
    val database: MyDatabase
    lateinit var token:String

    init {
        database = MyDatabase.getInstance(application.applicationContext)

        Coroutines.io {
            database.authvalDao.getAllData().forEach {
                token = "Token " + it.token
            }
        }
    }

    suspend fun getCreateAccon(account_holder_name: String, bank_name: String, branch: String, account_number: String, ifsc_code: String, address: String,
                               pan_card_number: String, correspondence_address: String,
                               canceled_cheque_image: File, pan_card_image: File
                               ): Response<CreateAccountDetailEntity>?{


        val account_holder_name = RequestBody.create(MediaType.parse("text/plain"), account_holder_name)
        val bank_name = RequestBody.create(MediaType.parse("text/plain"), bank_name)
        val branch = RequestBody.create(MediaType.parse("text/plain"), branch)
        val account_number = RequestBody.create(MediaType.parse("text/plain"), account_number)
        val ifsc_code = RequestBody.create(MediaType.parse("text/plain"), ifsc_code)
        val address = RequestBody.create(MediaType.parse("text/plain"), address)
        val pan_card_number = RequestBody.create(MediaType.parse("text/plain"), pan_card_number)
        val correspondence_address = RequestBody.create(MediaType.parse("text/plain"), correspondence_address)

        Log.i("tagchequeimagefile1", canceled_cheque_image.toString())
        Log.i("tagchequeimagefile2", canceled_cheque_image.name.toString())

        val canceled_cheque_image = MultipartBody.Part.createFormData("canceled_cheque_image", canceled_cheque_image.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), canceled_cheque_image))

        val pan_card_image = MultipartBody.Part.createFormData("pan_card_image", pan_card_image.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), pan_card_image))

        // can try changing multipart to image/* for image


        try {
            response = AuthApi().CreateAccountDetails(
                token,
                account_holder_name,
                bank_name,
                branch,
                account_number,
                ifsc_code,
                address,
                pan_card_number,
                correspondence_address,
                canceled_cheque_image,
                pan_card_image
            )

            Log.i("tagresponseabovesuccess", response.body().toString())
        } catch (e: Exception){
            e.printStackTrace()
        }

        when(response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.createAccountDetaileDao.deleteAccountDetail()
                    database.createAccountDetaileDao.insert(response.body()!!)

                    Log.i("tag whole response", response.body().toString())

                    //For logging the response stored in database.
                    database.createAccountDetaileDao.getAllAccountData().forEach {
                        Log.i("tagdbdataprofile", "Firstname =  ${it.account_holder_name}, Lastname =  ${it.account_number}")
                    }
                }
            }
        }


        return response
    }

    suspend fun getUpdateAccontDetail(account_holder_name: String, bank_name: String, branch: String, account_number: String,
                                      ifsc_code: String, address: String, pan_card_number: String, correspondence_address: String,
                               canceled_cheque_image: File, pan_card_image: File
    ): Response<UpdateAccountDetailEntity>?{


        val account_holder_name = RequestBody.create(MediaType.parse("text/plain"), account_holder_name)
        val bank_name = RequestBody.create(MediaType.parse("text/plain"), bank_name)
        val branch = RequestBody.create(MediaType.parse("text/plain"), branch)
        val account_number = RequestBody.create(MediaType.parse("text/plain"), account_number)
        val ifsc_code = RequestBody.create(MediaType.parse("text/plain"), ifsc_code)
        val address = RequestBody.create(MediaType.parse("text/plain"), address)
        val pan_card_number = RequestBody.create(MediaType.parse("text/plain"), pan_card_number)
        val correspondence_address = RequestBody.create(MediaType.parse("text/plain"), correspondence_address)

        val canceled_cheque_image = MultipartBody.Part.createFormData("canceled_cheque_image", canceled_cheque_image.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), canceled_cheque_image))

        val pan_card_image = MultipartBody.Part.createFormData("pan_card_image", pan_card_image.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), pan_card_image))

        // can try changing multipart to image/* for image

        lateinit var response : Response<UpdateAccountDetailEntity>

        try {
            response = AuthApi().UpdateAccountDetails(
                token,
                account_holder_name,
                bank_name,
                branch,
                account_number,
                ifsc_code,
                address,
                pan_card_number,
                correspondence_address,
                canceled_cheque_image,
                pan_card_image
            )

            Log.i("tagresponseabovesuccess", response.body().toString())
        } catch (e: Exception){
            e.printStackTrace()
        }

        return response
    }

    suspend fun getUserAccountData(): Response<GetAccountDetailEntity>?{

        val responseUserAccount = AuthApi().GetAccountDetails(token)

        when(responseUserAccount.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.getAccountDetaileDao.deleteUserAccountDetail()
                    database.getAccountDetaileDao.insert(responseUserAccount.body()!!)

                    Log.i("tag whole response", responseUserAccount.body().toString())

                    //For logging the response stored in database.
                    database.getAccountDetaileDao.getAllUserAccountData().forEach {
                        Log.i("tag whole response", it.accountHolderName)

                    }
                }
            }
        }

        return responseUserAccount
    }


    fun getUserAccountDetails():List<GetAccountDetailEntity>  {
        return database.getAccountDetaileDao.getAllUserAccountData()
    }
}

