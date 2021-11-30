package com.csestateconnect.repo

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.csestateconnect.CreateDealData
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.db.data.DealDetailChequeImageEntity
import com.csestateconnect.db.data.GetDealStatus
import com.csestateconnect.db.data.deals.ListOfDeals
import com.csestateconnect.db.data.assignedProjects.GetAssignedProjects
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.network.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File


class DealsRepository(application: Application) {
    val database: MyDatabase
    val sharedPref: SharedPreferences = application.getSharedPreferences("tokenValue", 0)
    val token ="Token " + sharedPref.getString("tokenValue", "")
    val authApi = AuthApi()

    init {
        database = MyDatabase.getInstance(
            application.applicationContext
        )
    }

    suspend fun getlistofdeals(): Response<ListOfDeals>? {
        val responseDeals = authApi.GetAllDeals(token)

        when (responseDeals.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.listOfDealsDao.delete()
                    database.listOfDealsDao.insert(responseDeals.body()!!)
                }
            }
        }
        return responseDeals
    }

    suspend fun getDealStatus(): Response<List<GetDealStatus>> {
        val response = authApi.GetDealStatus()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.dealStatusDao.delete()
                    database.dealStatusDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun updateDeal(id: Int, dealstatusid: Int): Response<ListOfDeals> {

        val response = authApi.UpdateDeal(
            token,
            id, dealstatusid)

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.listOfDealsDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun getFilterDeals(createdDate: String?, selectedleadsId: String?, modifiedDate: String?): Response<ListOfDeals>? {

        val responseDeals = authApi.FilterDeals(token, createdDate, selectedleadsId, modifiedDate)

        return responseDeals
    }

    suspend fun setDealChequeImage(id: Int, image: File?): Response<DealDetailChequeImageEntity> {
        var chequeImage: MultipartBody.Part? = null

        if (image != null) {
            chequeImage = MultipartBody.Part.createFormData(
                "cheque_image", image.name.toString(),
                RequestBody.create(MediaType.parse("image/jpeg"), image)
            )
        }
        else {
            chequeImage = null
        }

        val response = authApi.DealDetailChequeImage(
            token,
            id, chequeImage)
        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.dealDetailChequeImageDao.delete()
                    database.dealDetailChequeImageDao.insert(response.body()!!)
                    database.dealDetailChequeImageDao.getAllData()
                }
            }
        }

        return response
    }

    suspend fun getCreateDeal(leadId: Int, dealStatus: Int, project: Int, totalCommission: String,
        payableCommission: String, paidCommission: String, unpaidCommission: String,
        soldArea: Float, commissionRate: Float, commissionPercent: Float): Response<ResponseBody> {

        val response = authApi.CreateDeal(token, CreateDealData(leadId, dealStatus, project, totalCommission,
            payableCommission, paidCommission, unpaidCommission, soldArea, commissionRate, commissionPercent)
        )

        when (response.isSuccessful) {
            true -> {
                Log.i("TAG Log", "Deal Created")
            }
        }
        return response
    }

    suspend fun getDeal(id: Int): Response<com.csestateconnect.db.data.deals.Result>{
        val response = authApi.getDeal(token, id)
        return response
    }

//    suspend fun projectNameSuggestion(nameSuggest: String){
//        val call = authApi.ProjectNameSuggestionForCreateDeal(nameSuggest)
//        call.enqueue(object : Callback<Void> {
//
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//
//            }
//        })
//    }

    fun getAssignedProjectsFromDatabase(): LiveData<GetAssignedProjects> {
        return database.assignedProjectsDao.getAllData()
    }

    fun getDealsFromDatabase(): LiveData<List<ListOfDeals>> {
        return database.listOfDealsDao.getAllData()
    }

    fun getDealStatusFromDatabase(): LiveData<List<GetDealStatus>> {
        return database.dealStatusDao.getAllData()
    }

    fun getDealChequeImage(): LiveData<DealDetailChequeImageEntity> {
        return database.dealDetailChequeImageDao.getAllData()
    }

    fun getProjectsFromDatabase(): LiveData<List<Projectsdata>> {
        return database.projectsDataDao.getAllData()
    }
}