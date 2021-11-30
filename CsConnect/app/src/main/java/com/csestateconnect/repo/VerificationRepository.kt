package com.csestateconnect.repo

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.db.data.CreateProfileData
import com.csestateconnect.db.data.CreateVerificationDataEntity
import com.csestateconnect.db.data.GetVerificationData
import com.csestateconnect.db.data.UpdateVerificationDataEntity
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.network.*
import com.csestateconnect.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.MultipartBody
import java.io.File

class VerificationRepository
    (application: Application) {

    val database: MyDatabase
    lateinit var token:String

    init {
        database = MyDatabase.getInstance(
            application.applicationContext
        )

        Coroutines.io {
            database.authvalDao.getAllData().forEach {
                token = "Token " + it.token
            }
        }
    }
    suspend fun getVerificationData(verificationImage: File): Response<CreateVerificationDataEntity>?{

        val id_proofImage = MultipartBody.Part.createFormData("id_card_image", verificationImage.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), verificationImage))

        val response = AuthApi().CreateVerification(token,id_proofImage)

        when(response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.createVerificationDao.delteCreateVerification()
                    database.createVerificationDao.insert(response.body()!!)

                    Log.i("tag whole response", response.body().toString())

                    //For logging the response stored in database.
                    database.createVerificationDao.getVerificationCreateData().forEach {
                        Log.i("tagdbdataprofile", "hdhdshhs")
                    }
                }
            }
        }

        return response
    }

    suspend fun updateVerificationData(verificationImage: File): Response<UpdateVerificationDataEntity>?{

        val id_proofImage = MultipartBody.Part.createFormData("id_card_image", verificationImage.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), verificationImage))

        val response = AuthApi().UpdateVerification(token,id_proofImage)
        return response
    }

    suspend fun getVerifyImage(): Response<GetVerificationData>?{

        val response = AuthApi().GetVerification(token)

        when(response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.getVerificationDao.deleteGetVerificationData()
                    database.getVerificationDao.insert(response.body()!!)

                    //For logging the response stored in database.
                    database.getVerificationDao.getAllVerificationData().forEach {
                        Log.i("tagdbdataprofile", "hdhdshhs")
                    }
                }
            }
        }

        return response
    }

    fun getImageVerification():List<GetVerificationData>  {
        return database.getVerificationDao.getAllVerificationData()
    }

}
