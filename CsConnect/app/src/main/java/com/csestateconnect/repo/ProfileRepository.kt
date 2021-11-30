package com.csestateconnect.repo

import android.app.Application
import android.util.Log
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.db.data.CreateProfileData
import com.csestateconnect.db.data.CreateVerificationDataEntity
import com.csestateconnect.db.data.UpdateImageEntity
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.db.data.homedata.BrokerDetail
import com.csestateconnect.db.data.homedata.Commission
import com.csestateconnect.network.AuthApi
import com.csestateconnect.network.CreateProfileBody
import com.csestateconnect.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class ProfileRepository
    (application: Application) {

    val database: MyDatabase
    lateinit var token: String

    init {
        database = MyDatabase.getInstance(application.applicationContext)
        Coroutines.io {
            database.authvalDao.getAllData().forEach {
                token = "Token " + it.token
            }
        }
    }

    suspend fun getCreateProfile(
        firstname: String, lastname: String?, email: String, country: Any?,
        city: Any?, state: String, zipcode: String
    ): Response<CreateProfileData>? {


        val response = AuthApi().CreateProfile(
            token,
            CreateProfileBody(
                firstname, lastname, email, country, city, state, zipcode
            )
        )

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.createprofileDao.deleteProfile()
                    database.createprofileDao.insert(response.body()!!)

                    Log.i("tag whole response", response.body().toString())

                    //For logging the response stored in database.
                    database.createprofileDao.getAllDataProfile().forEach {
                        Log.i(
                            "tagdbdataprofile",
                            "Firstname =  ${it.first_name}, Lastname =  ${it.last_name}"
                        )
                    }
                }
            }
        }

        return response
    }

    suspend fun getUserProfileData(): Response<GetProfileData>? {

        val responseProfileData = AuthApi().GetProfileApi(token)

        when (responseProfileData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.getprofileDao.delete()
                    database.getprofileDao.insert(responseProfileData.body()!!)

                    Log.i("tag whole response", responseProfileData.body().toString())

                    //For logging the response stored in database.
                    database.getprofileDao.getAllProfileData().forEach {
                        Log.i(
                            "tagdbdataprofile",
                            "Firstname =  ${it.firstName}, Lastname =  ${it.lastName}"
                        )
                    }
                }
            }
        }

        return responseProfileData
    }

    suspend fun updateProfileData(
        firstname: String, lastname: String?, email: String, country: Any?,
        city: Any?, state: String, zipcode: String
    ): Response<CreateProfileData> {

        val response = AuthApi().CreateProfile(
            token,
            CreateProfileBody(
                firstname, lastname, email, country, city, state, zipcode
            )
        )

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.createprofileDao.deleteProfile()
                    database.createprofileDao.insert(response.body()!!)

                    Log.i("tag whole response", response.body().toString())

                    //For logging the response stored in database.
                    database.createprofileDao.getAllDataProfile().forEach {
                        Log.i("tagdbdataprofile", "hdhdshhs")
                    }
                }
            }
        }

        return response
    }

    suspend fun getProfileImage(profile_Image: File): Response<UpdateImageEntity>? {

        val profileImage = MultipartBody.Part.createFormData(
            "profile_image", profile_Image.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), profile_Image)
        )

        val response = AuthApi().UpdateProfileImage(token, profileImage)

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.getprofileImageDao.delete()
                    database.getprofileImageDao.insert(response.body()!!)

                    database.getprofileImageDao.getProfileImageData().forEach {
                        Log.i("profileImage", it.profileImage)
                    }
                }
            }
        }

        return response
    }

    suspend fun deleteProfileImage(): Response<UpdateImageEntity>? {

        val response = AuthApi().DeleteProfileImage(token)

        return response
    }



    fun getUserPersonalDetails(): List<GetProfileData> {
        return database.getprofileDao.getAllProfileData()
    }

    fun getProfileImageFromDatabase(): List<UpdateImageEntity> {
        return database.getprofileImageDao.getProfileImageData()
    }

}