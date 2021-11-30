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
import com.csestateconnect.db.data.events.CreateEventData
import com.csestateconnect.db.data.events.GetEventCategoriesList
import com.csestateconnect.db.data.events.eventsDataList.GetEventsDataEntity
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfClients.CreateClientData
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.db.data.reimbursements.CreateReimbursementData
import com.csestateconnect.db.data.reimbursements.GetReimbursementTypeEntity
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.CreateReimbursementDocs
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.GetReimburseDocListEntity
import com.csestateconnect.network.*
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

class ReimbursementRepository
    (application: Application) {

   lateinit var response : Response<CreateReimbursementDocs>
    lateinit var response2 : Response<CreateReimbursementDocs>
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

   suspend fun getCreateReimburse(amount: String,
       type: String
    ): Response<CreateReimbursementData>? {


        val response = AuthApi().CreateReimbursement(token,
            CreateReimburseBody(amount, type)

        )
       Log.d("hjhgfghgg",response.toString())

        return response
    }

    suspend fun updateReimbursements( reimburseId: Int, amount: String,type: String): Response<CreateReimbursementData> {

        val response = AuthApi().UpdateReimbursement(
            token, reimburseId,
            CreateReimburseBody(amount,type)
        )

        return response
    }

    suspend fun getReimburseTypeListApi(): Response<GetReimbursementTypeEntity>? {

        val responseReimburseTypeList = AuthApi().GetReimbursementTypes(token)

        when (responseReimburseTypeList.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.reimburseTypeListDao.delete()
                    database.reimburseTypeListDao.insert(responseReimburseTypeList.body()!!)

                    Log.i("tag whole response", responseReimburseTypeList.body().toString())
                }
            }
        }

        return responseReimburseTypeList
    }

    suspend fun removeReimbursementsApi( eventId: Int): Response<ResponseBody>? {

        val response = AuthApi().RemoveReimbursement( token , eventId)
          Log.d("hjhjsdh",response.toString())
        return response
    }



    suspend fun getReimburseDataListApi(): Response<GetReimbursementListEntity>? {

        val responseReimbursListData = AuthApi().GetReimbursementList(token)

        when (responseReimbursListData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.reimburseDataListDao.delete()
                    database.reimburseDataListDao.insert(responseReimbursListData.body()!!)

                    Log.i("tag whole response", responseReimbursListData.body().toString())
                }
            }
        }

        return responseReimbursListData
    }


    // BELOW ALL CODES RELATED TO REIMURSEMENT DOCUMENT

    suspend fun getCreateReimburseDocsApi(doc_name: String, reimburse_id: String, doc_img: File
    ): Response<CreateReimbursementDocs>?{

        val doct_name = RequestBody.create(MediaType.parse("text/plain"), doc_name)
        val reimbursement_Id = RequestBody.create(MediaType.parse("text/plain"), reimburse_id)

        val doct_image = MultipartBody.Part.createFormData("document", doc_img.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), doc_img))


        // can try changing multipart to image/* for image
        try {
            response = AuthApi().CreateReimburseDocs(
                token,
                doct_name,
                reimbursement_Id,
                doct_image
            )

            Log.i("tagresponseabovesuccess", response.body().toString())
        } catch (e: Exception){
            e.printStackTrace()
        }
        return response
    }

    suspend fun getUpdateReimburseDoc(doc_id: Int,doc_name: String,  doc_img: File
    ): Response<CreateReimbursementDocs>?{

        val doct_name = RequestBody.create(MediaType.parse("text/plain"), doc_name)

        val doct_image = MultipartBody.Part.createFormData("document", doc_img.name.toString(),
            RequestBody.create(MediaType.parse("image/jpeg"), doc_img))


        // can try changing multipart to image/* for image
        try {
            response2 = AuthApi().UpdateReimburseDocs(
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

    suspend fun getReimburseDocListApi(): Response<GetReimburseDocListEntity>? {

        val responseReimburseDocListData = AuthApi().GetReimburseDocList(token)

        when (responseReimburseDocListData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.reimburseDocsListDao.delete()
                    database.reimburseDocsListDao.insert(responseReimburseDocListData.body()!!)

                    Log.i("tag whole response", responseReimburseDocListData.body().toString())
                }
            }
        }

        return responseReimburseDocListData
    }

    suspend fun removeReimburseDocumentApi( docId: Int): Response<ResponseBody>? {

        val response = AuthApi().RemoveReimburseDocs( token , docId)
        Log.d("hjhjsdh",response.toString())
        return response
    }


    fun getReimburseDataFromLiveData(): LiveData<GetReimbursementListEntity> {
        return database.reimburseDataListDao.getAllData()
    }

    fun getReimburseDataFromList(): List<com.csestateconnect.db.data.reimbursements.reimburseList.Result?> {
        return database.reimburseDataListDao.getClientListData().get(0).results!!
    }

    fun getReimburseTypeListData(): LiveData<GetReimbursementTypeEntity> {
        return database.reimburseTypeListDao.getAllData()
    }

    fun getReimburseDocListData(): LiveData<GetReimburseDocListEntity> {
        return database.reimburseDocsListDao.getAllData()
    }
}