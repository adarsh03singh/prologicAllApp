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
import com.csestateconnect.network.AuthApi
import com.csestateconnect.network.CreateClientBody
import com.csestateconnect.network.CreateEventBody
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

class EventsRepository
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

   suspend fun getCreateEvent(
       category: String,auto_reminder: Boolean,active: Boolean?, date: String?, time: String?, subject: String?,
       description: String?,priority: String?
    ): Response<CreateEventData>? {


        val response = AuthApi().CreateEvent(token,
            CreateEventBody(category,auto_reminder,active, date, time, subject, description, priority
            )

        )
       Log.d("hjhgfghgg",response.toString())

        return response
    }

    suspend fun getEventCategoriesListApi(): Response<GetEventCategoriesList>? {

        val responseEventCategoriesListData = AuthApi().GetEventCategories(token)

        when (responseEventCategoriesListData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.eventCategoriesListDao.delete()
                    database.eventCategoriesListDao.insert(responseEventCategoriesListData.body()!!)

                    Log.i("tag whole response", responseEventCategoriesListData.body().toString())
                }
            }
        }

        return responseEventCategoriesListData
    }

    suspend fun removeEvents( eventId: Int): Response<ResponseBody>? {

        val response = AuthApi().RemoveEvents( token , eventId)
          Log.d("hjhjsdh",response.toString())
        return response
    }

    suspend fun updateEvents( eventId: Int,category: String,auto_reminder: Boolean,active: Boolean?, date: String?, time: String?, subject: String?,
                              description: String?,priority: String?): Response<CreateEventData> {

        val response = AuthApi().UpdateEvents(
            token, eventId,
            CreateEventBody(category,auto_reminder,active, date, time, subject, description, priority)
        )

        return response
    }

    suspend fun getEventDataListApi(): Response<GetEventsDataEntity>? {

        val responseEventListData = AuthApi().GetEventDataList(token)

        when (responseEventListData.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.eventDataListDao.delete()
                    database.eventDataListDao.insert(responseEventListData.body()!!)

                    Log.i("tag whole response", responseEventListData.body().toString())
                }
            }
        }

        return responseEventListData
    }


    fun getEventDataFromLiveData(): LiveData<GetEventsDataEntity> {
        return database.eventDataListDao.getAllData()
    }

    fun getEventDataFromList(): List<com.csestateconnect.db.data.events.eventsDataList.Result?> {
        return database.eventDataListDao.getClientListData().get(0).results!!
    }

    fun getEventCategoriesListData(): LiveData<GetEventCategoriesList> {
        return database.eventCategoriesListDao.getAllData()
    }
}