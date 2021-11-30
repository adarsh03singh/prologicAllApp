package com.csestateconnect.repo

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.csestateconnect.CreateLeadData
import com.csestateconnect.GetFavProjectId
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.db.data.GetCompletionStatus
import com.csestateconnect.db.data.GetLeadStatus
import com.csestateconnect.db.data.GetPreferredProperty
import com.csestateconnect.db.data.commissionSlab.ProjectCommissionSlab
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.favouriteProject.removeFavouriteProjects.RemoveFavProjectEntity
import com.csestateconnect.db.data.homedata.*
import com.csestateconnect.db.data.homedata.BrokerDetail
import com.csestateconnect.db.data.homedata.Commission
import com.csestateconnect.db.data.homedata.Homedata
import com.csestateconnect.db.data.homedata.ProjectDetail
import com.csestateconnect.db.data.homepagedata.banner.GetHomeBanner
import com.csestateconnect.db.data.leads.CreateLeadActivity
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.db.data.listOfConcerns.Comments
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.db.data.notifications.NotificationData
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.network.*
import com.csestateconnect.network.AuthApi
import com.csestateconnect.network.LeadStatus
import com.csestateconnect.network.UpdateLeadCreateActivity
import com.csestateconnect.network.UpdateLeadFeedbackStar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class HomeRepository(application: Application) {
    val database: MyDatabase
//    lateinit var token: String
    val sharedPref: SharedPreferences = application.getSharedPreferences("tokenValue", 0)
    val token ="Token " + sharedPref.getString("tokenValue", "")
    val authApi = AuthApi()

    val sharedPrefer: SharedPreferences = application.getSharedPreferences("regToken", 0)
    val regToken = sharedPrefer.getString("regToken", "")

    init {
        database = MyDatabase.getInstance(
            application.applicationContext
        )
//        Coroutines.io {
//            database.authvalDao.getAllData().forEach {
//                token = "Token " + it.token
//            }
//        }
    }

    suspend fun getlistofleads(): Response<ListOfLeads>? {

        val responseLeads = authApi.GetAllLeads(token)

        when (responseLeads.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.listOfLeadsDao.delete()
                    database.listOfLeadsDao.insert(responseLeads.body()!!)
                    database.listOfLeadsDao.getAllData()
                    //For logging the response stored in database.
//                    database.listOfLeadsDao.getAllData().forEach {
//                        Log.i("tagdbdata Countries", "Id = ${it.count}, number code =  ${it}")
//                        Log.i("tag countries all data", it.toString())
//                    }
//                    Log.i("tagleaddatabase", leadsData.toString()
                }
            }
        }
        return responseLeads
    }

    suspend fun getFilterLeads(createdDate: String?, selectedleadsId: String?, modifiedDate: String?, assigned: Boolean?, submitted: Boolean?): Response<ListOfLeads>? {

        val responseLeads = authApi.FilterLeads(token, createdDate, selectedleadsId, modifiedDate, assigned, submitted)

        return responseLeads
    }

    suspend fun getLeadStatus(): Response<List<GetLeadStatus>> {
        val response = authApi.GetLeadStatus()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.leadStatusDao.delete()
                    database.leadStatusDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun getCompletionStatus(): Response<List<GetCompletionStatus>> {
        val response = authApi.GetCompletionStatus()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.completionStatusdao.delete()
                    database.completionStatusdao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun getPreferredProperty(): Response<List<GetPreferredProperty>> {
        val response = authApi.GetPreferredPropertyType()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.preferredPropertyDao.delete()
                    database.preferredPropertyDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun updateLead(leadId: Int, leadstatusid: Int): Response<Result> {
        val id: Int = leadId

        val response = authApi.UpdateLead(token, id, LeadStatus(leadstatusid))

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.leadsResultDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun updateLeadFeedback(leadId: Int, feedbackComment: String?, quality: Float): Response<Result> {
        val submitted = true

        val response = authApi.UpdateLeadFeedback(token, leadId, UpdateLeadFeedbackStar(feedbackComment, quality, submitted))

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.leadsResultDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun updateLeadActivity(leadId: Int, description: String, date: String): Response<CreateLeadActivity> {

        val response = authApi.UpdateLeadActivity(token, UpdateLeadCreateActivity(leadId, description, date))

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.leadActivityDao.insert(response.body()!!)
                }
            }
        }
        return response

    }

    suspend fun postLeadRequest(): Response<ResponseBody> {
        val response = authApi.postLeadRequest(token)
        return response
    }

    suspend fun getCreateLead(name: String, number: String, email: String?, country: Int?,
    city: Int?, minimum: String?, maximum: String?,
    budgetCurrency: String?, preferredLocation: List<Int>?, propertyType: List<Int>?,
    preferredStatus: List<Int>?, leadStatus: Int): Response<Void> {

        val response = authApi.CreateLead(token, CreateLeadData(name, number, email, country, city,
            minimum, maximum, budgetCurrency, preferredLocation, propertyType, preferredStatus, leadStatus))

        when (response.isSuccessful) {
            true -> {
                Log.i("TAG Log", "Create lead call request successfully executed.")
            }
        }
        return response
    }

    suspend fun getHomeData(): Response<Homedata>? {

            val response =
                authApi.GetHomeData(token)

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.homeDataDao.delete()
                    database.homeDataDao.insert(response.body()!!)
                }
            }
        }
        return response
    }

    suspend fun getHomeBanner(city: Int?, country: Int?): Response<GetHomeBanner> {
        val response = authApi.getHomeBanner(token, city, country)
        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.homeBannerDao.insert(response.body()!!)
                }
            }
        }

        return response
    }
//property__type__in: String?,
//    suspend fun getFilterProjects(location_name__in: String?, amenities__in: String?, status__in: String?,
//                                  low_cost__range: String?, project_bsp_cost__range: String?,unit_built_up_area__range: String?,
//                                    bhk__in: String?): Response<Projectsdata>? {
//
//        val responseProject = authApi.FilterProjects(location_name__in,amenities__in,status__in,
//            low_cost__range,project_bsp_cost__range,unit_built_up_area__range,bhk__in )
//
//        return responseProject
//    }

    suspend fun getFilterProjects(filter: HashMap<String, String>): Response<Projectsdata>? {

        val responseProject = authApi.FilterProjects( filter)

        return responseProject
    }

    suspend fun getProjectsdata(): Response<Projectsdata>? {

        val response = authApi.GetProject()

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.projectsDataDao.delete()
                    database.projectsDataDao.insert(response.body()!!)

//                    database.projectsDataDao.getAllData()
                }
            }
        }
        return response
    }

    suspend fun getFavouriteProjectsdata(): Response<GetFavouriteProjectsEntity>? {

        val response = authApi.GetFavoriteProjects(token)

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.favouriteProjectsDataDao.delete()
                    database.favouriteProjectsDataDao.insert(response.body()!!)

                }
            }
        }
        return response
    }

    suspend fun removeFavouriteProjectsdata( projectId: Int): Response<ResponseBody>? {

        val response = authApi.RemoveFavouriteProjects( token , GetFavProjectId(projectId))

        return response
    }

    suspend fun addFavouriteProjects( projectId: Int): Response<ResponseBody>? {

        val response = authApi.AddFavouriteProjects( token , GetFavProjectId(projectId))

        return response
    }



    suspend fun getCommissionSlabDetails(): Response<List<ProjectCommissionSlab>> {
        val response = authApi.GetCommissionSlab(token)

        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.commissionSlabDataDao.delete()
                    database.commissionSlabDataDao.insert(response.body()!!)

                }
            }
        }
        return response
    }

    suspend fun callSearchProjectByCityApi(cityNameQuery: String): Response<ResponseBody>? {

        val response = authApi.SearchProjectByCity(cityNameQuery)

        return response
    }

    suspend fun callSearchProjectByProjNameApi(projectNameQuery: String): Response<ResponseBody>? {

        val response = authApi.SearchProjectsByProjectName(projectNameQuery)

        return response
    }

    fun checkHomeNull(): Int{
        if (database.homeDataDao.checkIfNull() == 0) {
            return 0
//                getHomeData() // Api hit and database insert value
        } else {
            return 1
        }
    }

    suspend fun deleteFcmDevice(): Response<Void> {
        return authApi.deleteFcmDevice(token, AuthApi.RegistrationId(regToken))
    }

    suspend fun raiseConcern(heading: String, description: String) : Response<Void> {
        return authApi.raiseConcern(token, ConcernBody(heading, description, false))
    }

    suspend fun getListOfConcerns() : Response<GetListOfConcerns> {
        val response = authApi.getListOfConcerns(token)
        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.listOfConcernsDao.delete()
                    database.listOfConcernsDao.insert(response.body()!!)
                }
            }
        }

        return response
    }

    suspend fun createConcernComment(id: Int, comment: String): Response<Comments> {
        val response = authApi.createConcernComment(token, ConcernCommentBody(id, comment))
        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.concernCommentsdao.insert(response.body()!!)
                }
            }
        }

        return response
    }

    suspend fun getListOfNotificationsApi(): Response<NotificationData> {
         val response = authApi.GetListOfNotifications(token)
        when (response.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.notificationDao.delete()
                    database.notificationDao.insert(response.body()!!)
                }
            }
        }

        return response
    }

    fun getListOfConcernsData(): LiveData<GetListOfConcerns> {
        return database.listOfConcernsDao.getAllData()
    }

    fun getCommissionFromDatabase(): Commission {
        return database.homeDataDao.getAllData().commission
    }

    fun getTop5BrokersFromDatabase(): List<BrokerDetail> {
        return database.homeDataDao.getAllData().brokers.brokerDetails
    }

    fun getAllBrokersHomeFromDatabase(): Brokers {
        return database.homeDataDao.getAllData().brokers
    }
    fun getAllProjectHomeFromDatabase(): Projects {
        return database.homeDataDao.getAllData().projects
    }

    fun getTop5ProjectsFromDatabase(): List<ProjectDetail> {
        return database.homeDataDao.getAllData().projects.projectDetails
    }

    fun getHomeDataFromDatabase(): List<Homedata> {
        return database.homeDataDao.getAllDataAsList()
    }

    fun getProjectsFromDatabase(): LiveData<List<Projectsdata>> {
        return database.projectsDataDao.getAllData()
    }

    fun getLeadsFromDatabase(): LiveData<List<ListOfLeads>> {
        return database.listOfLeadsDao.getAllData()
    }

    fun getLeadStatusFromDatabase(): LiveData<List<GetLeadStatus>>{
        return database.leadStatusDao.getAllData()
    }

    fun getCompletionStatusFromDatabase(): LiveData<List<GetCompletionStatus>>{
        return database.completionStatusdao.getAllData()
    }

    fun getPreferredPropertyFromDatabase(): LiveData<List<GetPreferredProperty>>{
        return database.preferredPropertyDao.getAllData()
    }

    fun getCountriesFromDatabase(): LiveData<List<Countries>>{
        return database.countriesDao.getAllLiveData()
    }

    fun getCommissionsFromDatabase(): LiveData<List<ProjectCommissionSlab>> {
        return database.commissionSlabDataDao.getAllData()
    }

    fun getFavoriteProjectFromDatabase(): LiveData<List<GetFavouriteProjectsEntity>> {
        return database.favouriteProjectsDataDao.getAllData()
    }

    fun getNotificationDataFromDB(): LiveData<NotificationData> {
        return database.notificationDao.getAllData()
    }

    fun getHomeBannerData(): List<GetHomeBanner> {
        return database.homeBannerDao.getAllData()
    }

    fun getLeadActivityData(): LiveData<List<CreateLeadActivity>?> {
        return database.listOfLeadsDao.getAllActivityData()
    }

}
