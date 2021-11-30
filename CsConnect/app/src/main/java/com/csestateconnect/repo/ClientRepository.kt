package com.csestateconnect.repo

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.csestateconnect.GetFavProjectId
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.db.data.CreateAccountDetailEntity
import com.csestateconnect.db.data.CreateProfileData
import com.csestateconnect.db.data.UpdateImageEntity
import com.csestateconnect.db.data.clientDoc.CreateClientDocData
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.clientDoc.UpdateClientdocData
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfClients.CreateClientData
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.network.AuthApi
import com.csestateconnect.network.CreateClientBody
import com.csestateconnect.network.CreateProfileBody
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.GetClientId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class ClientRepository
    (application: Application) {

   lateinit var response : Response<CreateClientDocData>
    lateinit var response2 : Response<UpdateClientdocData>
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

   suspend fun getCreateClient(
        name: String, phone_number: String?, email: String, country: Any?,
        city: Any?, location: String, address: String
    ): Response<CreateClientData>? {


        val response = AuthApi().CreateClientProfile(token,
            CreateClientBody(name, phone_number, email, country, city, location, address
            )

        )
       Log.d("hjhgfghgg",response.toString())

        return response
    }

    suspend fun getClientListApi(): Response<ClientList>? {

        val responseClientListData = AuthApi().GetClientListApi(token)

        when (responseClientListData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.clientListDao.delete()
                    database.clientListDao.insert(responseClientListData.body()!!)

                    Log.i("tag whole response", responseClientListData.body().toString())
                }
            }
        }

        return responseClientListData
    }

    suspend fun removeClientsData( clientId: Int): Response<ResponseBody>? {

        val response = AuthApi().RemoveClientItems( token , clientId)
          Log.d("hjhjsdh",response.toString())
        return response
    }

    suspend fun updateClientProfile( clientId: Int, name: String, phone_number: String?, email: String, country: Any?,
                                      city: Any?, location: String, address: String): Response<CreateClientData> {

        val response = AuthApi().UpdateClientProfile(
            token,clientId,
            CreateClientBody(name, phone_number, email, country, city, location, address)
        )

        return response
    }

    suspend fun getCreateClientDoc(doc_name: String, client_id: String, doc_img: File
    ): Response<CreateClientDocData>?{

        val doct_name = RequestBody.create(MediaType.parse("text/plain"), doc_name)
        val client_Id = RequestBody.create(MediaType.parse("text/plain"), client_id)

        val doct_image = MultipartBody.Part.createFormData("document", doc_img.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), doc_img))


        // can try changing multipart to image/* for image
        try {
            response = AuthApi().CreateClientDocs(
                token,
                doct_name,
                client_Id,
                doct_image
            )

            Log.i("tagresponseabovesuccess", response.body().toString())
        } catch (e: Exception){
            e.printStackTrace()
        }


        return response
    }

    suspend fun getUpdateClientDoc(doc_id: Int,doc_name: String,  doc_img: File
    ): Response<UpdateClientdocData>?{

        val doct_name = RequestBody.create(MediaType.parse("text/plain"), doc_name)

        val doct_image = MultipartBody.Part.createFormData("document", doc_img.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), doc_img))


        // can try changing multipart to image/* for image
        try {
            response2 = AuthApi().UpdateClientDocs(
                token,
                doc_id,
                doct_name,
                doct_image
            )

            Log.i("tagresponseabovesuccess", response2.body().toString())
        } catch (e: Exception){
            e.printStackTrace()
        }


        return response2
    }

    suspend fun getClientDocListApi(): Response<GetClientDocListEntity>? {

        val responseClientDocListData = AuthApi().GetClientDocs(token)

        when (responseClientDocListData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.clientDocListDao.delete()
                    database.clientDocListDao.insert(responseClientDocListData.body()!!)

                    Log.i("tag whole response", responseClientDocListData.body().toString())
                }
            }
        }

        return responseClientDocListData
    }

    suspend fun removeClientsDocument( docId: Int): Response<ResponseBody>? {

        val response = AuthApi().RemoveClientDocs( token , docId)
        Log.d("hjhjsdh",response.toString())
        return response
    }


    fun getClientListData(): LiveData<ClientList> {
        return database.clientListDao.getAllData()
    }

    fun getClientResultData(): List<Result?> {
        return database.clientListDao.getClientListData().get(0).results!!
    }

    fun getClientDocListData(): LiveData<GetClientDocListEntity> {
        return database.clientDocListDao.getAllData()
    }
}