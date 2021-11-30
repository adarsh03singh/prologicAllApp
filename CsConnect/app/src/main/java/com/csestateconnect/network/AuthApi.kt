package com.csestateconnect.network

import com.csestateconnect.CreateDealData
import com.csestateconnect.CreateLeadData
import com.csestateconnect.GetFavProjectId
import com.csestateconnect.GetNumber
import com.csestateconnect.db.data.*
import com.csestateconnect.db.data.clientDoc.CreateClientDocData
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.clientDoc.UpdateClientdocData
import com.csestateconnect.db.data.commissionSlab.ProjectCommissionSlab
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.deals.ListOfDeals
import com.csestateconnect.db.data.events.CreateEventData
import com.csestateconnect.db.data.events.GetEventCategoriesList
import com.csestateconnect.db.data.events.eventsDataList.GetEventsDataEntity
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.fcm.AddFcmDeviceEntity
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.db.data.homedata.Homedata
import com.csestateconnect.db.data.homepagedata.banner.GetHomeBanner
import com.csestateconnect.db.data.leads.CreateLeadActivity
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfClients.CreateClientData
import com.csestateconnect.db.data.listOfConcerns.Comments
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.db.data.notifications.NotificationData
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.db.data.reimbursements.CreateReimbursementData
import com.csestateconnect.db.data.reimbursements.GetReimbursementTypeEntity
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.CreateReimbursementDocs
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.GetReimburseDocListEntity
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers


private const val BASE_URL = "https://api.csestateconnect.com/"

private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val okHttp = OkHttpClient.Builder()
    .addInterceptor(logger)

interface AuthApi {

    //FOR GENERATE OTP
    @Headers("Content-Type: application/json")
    @POST("auth/generate_otp")
//    @FormUrlEncoded       //Cannot be used with @Body tag
    suspend fun AuthGen(@Body phone_number: GetNumber): Response<AuthGenEntity>

    //FOR VALIDATE OTP
    @Headers("Content-Type: application/json")
    @POST("/auth/validate_otp")
    suspend fun AuthVal(@Body authValBody: AuthValBody): Response<AuthValEntity>

    //FOR CREATE/UPDATE USER PROFILE
    @Headers("Content-Type: application/json")
    @PUT("appapi/profile/update")
    suspend fun CreateProfile(@Header("Authorization") token: String, @Body createProfileBody: CreateProfileBody): Response<CreateProfileData>

    //FOR GET USER PROFILE
    @Headers("Content-Type: application/json")
    @GET("/appapi/profile")
    suspend fun GetProfileApi(@Header("Authorization") token: String): Response<GetProfileData>

    @Multipart
    @POST("/appapi/profile/update_image")
    suspend fun UpdateProfileImage(
        @Header("Authorization") token: String,
        @Part id_card_image: MultipartBody.Part
    ): Response<UpdateImageEntity>


    @POST("/appapi/profile/reset_image")
    suspend fun DeleteProfileImage(
        @Header("Authorization") token: String
    ): Response<UpdateImageEntity>

    //FOR GETTING COUNTRIES
    @GET("/appapi/read_only/countries")
    suspend fun GetCountriesApi(): Response<List<Countries>>

    //FOR HOME PAGE DATA
    @GET("/appapi/app_home_data")
    suspend fun GetHomeData(@Header("Authorization") token: String): Response<Homedata>

    // FOR HOME PAGE BANNERS
    @Headers("Content-Type: application/json")
    @GET("/appapi/banner_projects/")
    suspend fun getHomeBanner(@Header("Authorization") token: String,
                              @Query("city") city: Int?,
                              @Query("country") country: Int?) : Response<GetHomeBanner>

    //FOR GET ALL LEADS IN LEADS HOME FRAGMENT
    @GET("/appapi/leads/")
    suspend fun GetAllLeads(
        @Header("Authorization") token: String
    ): Response<ListOfLeads>

    // FOR FILTER LEADS
    @GET("/appapi/leads/")
    suspend fun FilterLeads(
        @Header("Authorization") token: String,
        @Query("created_at") created_at: String?,
        @Query("status") status: String?,
        @Query("updated_at") updated_at: String?,
        @Query("assigned") assigned: Boolean?,
        @Query("submitted") submitted: Boolean?
    ): Response<ListOfLeads>

    // FOR FILTER DEALS
    @GET("/appapi/deals/")
    suspend fun FilterDeals(
        @Header("Authorization") token: String,
        @Query("created_at") created_at: String?,
        @Query("status") status: String?,
        @Query("updated_at") updated_at: String?
    ): Response<ListOfDeals>

    // FOR LEAD STATUS ITEMS
    @GET("/appapi/read_only/lead_statuses")
    suspend fun GetLeadStatus(): Response<List<GetLeadStatus>>

    // FOR DEAL STATUS ITEMS
    @GET("/appapi/read_only/deal_statuses")
    suspend fun GetDealStatus(): Response<List<GetDealStatus>>

    // FOR COMPLETION STATUS
    @GET("/appapi/read_only/completion_statuses")
    suspend fun GetCompletionStatus(): Response<List<GetCompletionStatus>>

    // FOR PREFERRED PROPERTY TYPE
    @GET("/appapi/read_only/property_types")
    suspend fun GetPreferredPropertyType(): Response<List<GetPreferredProperty>>

    // FOR UPDATE LEAD LEAD_STATUS
    @Headers("Content-Type: application/json")
    @PATCH("/appapi/leads/{id}/")
    suspend fun UpdateLead(@Header("Authorization") token: String, @Path("id") id: Int, @Body leadStatus: LeadStatus): Response<Result>

    // FOR UPDATE LEAD FEEDBACK
    @Headers("Content-Type: application/json")
    @PATCH("/appapi/leads/{id}")
    suspend fun UpdateLeadFeedback(@Header("Authorization") token: String, @Path("id") id: Int, @Body updateLead : UpdateLeadFeedbackStar): Response<Result>

    // FOR LEAD CREATE ACTIVITY
    @Headers("Content-Type: application/json")
    @POST("/appapi/lead_activities/create/")
    suspend fun UpdateLeadActivity(@Header("Authorization") token: String, @Body updateLead : UpdateLeadCreateActivity): Response<CreateLeadActivity>

    // FOR UPDATE DEAL
    @Headers("Content-Type: application/json")
    @PATCH("/appapi/deals/{id}")
    suspend fun UpdateDeal(
        @Header("Authorization") token: String,
        @Path("id") id: Int, @Body deal_status: Int): Response<ListOfDeals>

//    // FOR CREATE DEAL ASSIGNED PROJECTS
//    @Headers("Content-Type: application/json", "Authorization: Token eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6OTcsImV4cCI6MTU4NTEyOTQ5NH0.V5V5118h0WteZaJ_hXrf3ffqnDD3W_ehyxLXtVg09Ek")
//    // my token is below
////    @Headers("Content-Type: application/json", "Authorization: Token eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTQzLCJleHAiOjE1ODY5MzQxNTF9.FhyEcSjWIvZkOfq2yEkL96qR3mYpfatEx0EVcZTsQgU")
//    @GET("/appapi/projects/")
//    suspend fun GetAssignedProjects(
////        @Header("Authorization") token: String
//    ): Response<GetAssignedProjects>

    // FOR CREATE DEAL
    @Headers("Content-Type: application/json")
    @POST("/appapi/deals/")
    suspend fun CreateDeal(@Header("Authorization") token: String, @Body createDealData: CreateDealData): Response<ResponseBody>

    // FOR GET DEAL
    @GET("/appapi/deals/{id}/")
    suspend fun getDeal(@Header("Authorization") token: String, @Path("id") id: Int): Response<com.csestateconnect.db.data.deals.Result>

//    // FOR PROJECT NAME ON CREATE DEAL PAGE
//    @GET("/projects/suggest/")
//    suspend fun ProjectNameSuggestionForCreateDeal(@Query("name_suggest__completion") nameSuggest: String): Response<ResponseBody>

    // FOR LEAD REQUEST TO RM
    @POST("/appapi/lead_requests")
    suspend fun postLeadRequest(@Header("Authorization") token: String) : Response<ResponseBody>

    // FOR CREATE LEAD
    @Headers("Content-Type: application/json")
    @POST("/appapi/leads/")
    suspend fun CreateLead(@Header("Authorization") token: String, @Body createLeadData: CreateLeadData): Response<Void>

    //FOR GET PROJECTS
    @GET("/search/")
    suspend fun GetProject(): Response<Projectsdata>

    //FOR CREATE USER VERIFICATION
    @Multipart
    @POST("/appapi/kyc/create")
    suspend fun CreateVerification(
        @Header("Authorization") token: String,
        @Part id_card_image: MultipartBody.Part
    ): Response<CreateVerificationDataEntity>


    //FOR UPDATE USER VERIFICATION
    @Multipart
    @PUT("/appapi/kyc/update")
    suspend fun UpdateVerification(
        @Header("Authorization") token: String,
        @Part id_card_image: MultipartBody.Part
    ): Response<UpdateVerificationDataEntity>


    //FOR GET USER VERIFICATION
    @GET("/appapi/kyc")
    suspend fun GetVerification(
        @Header("Authorization") token: String): Response<GetVerificationData>


    // FOR CREATE BANK ACCOUNT
    @Multipart
    @POST("/appapi/bank_account/create")//appapi/bank_account/update and put
    suspend fun CreateAccountDetails(
        @Header("Authorization") token: String,
        @Part("account_holder_name") account_holder_name: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("ifsc_code") ifsc_code: RequestBody,
        @Part("address") address: RequestBody,
        @Part("pan_card_number") pan_card_number: RequestBody,
        @Part("correspondence_address") correspondence_address: RequestBody,
        @Part canceled_cheque_image: MultipartBody.Part,
        @Part pan_card_image: MultipartBody.Part
    ): Response<CreateAccountDetailEntity>

    // FOR UPDATE BANK ACCOUNT
    @Multipart
    @PUT("/appapi/bank_account/update")//appapi/bank_account/update and put
    suspend fun UpdateAccountDetails(
        @Header("Authorization") token: String,
        @Part("account_holder_name") account_holder_name: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("ifsc_code") ifsc_code: RequestBody,
        @Part("address") address: RequestBody,
        @Part("pan_card_number") pan_card_number: RequestBody,
        @Part("correspondence_address") correspondence_address: RequestBody,
        @Part canceled_cheque_image: MultipartBody.Part,
        @Part pan_card_image: MultipartBody.Part
    ): Response<UpdateAccountDetailEntity>

    // FOR GET USER ACCOUNT DETAILS
    @GET("/appapi/bank_account")
    suspend fun GetAccountDetails(
        @Header("Authorization") token: String): Response<GetAccountDetailEntity>

    // FOR SEARCH PROJECTS
    @GET("/projects/suggest/")
    suspend fun SearchProjectByCity(
        @Query("city_name_suggest__completion") cityNameSuggest: String): Response<ResponseBody>

    // FOR SEARCH PROJECTS
    @GET("/projects/suggest/")
    suspend fun SearchProjectsByProjectName(
        @Query("name_suggest__completion") projectNameSuggest: String) : Response<ResponseBody>

    // FOR PROJECTS FILTER
    @GET("/search/")
    suspend fun FilterProjects(
        @QueryMap filter: HashMap<String, String>
//        @Query("location_name__in") location_filter: String?,
//        @Query("amenities__in") amenities_filter: String?,
//        @Query("status__in") status_filter: String?,
//        @Query("low_cost__range") low_cost_price_filter: String?,
//        @Query("project_bsp_cost__range") bsp_price_filter: String?,
//        @Query("unit_built_up_area__range") builtup_area_filter: String?,
////        @Query("property__type__in") property_type_filter: String?,
//        @Query("rooms__in") bhk_type_filter: String?

    ): Response<Projectsdata>

    // FOR RAISE CONCERN
    @Headers("Content-Type: application/json")
    @POST("/appapi/concerns/")
    suspend fun raiseConcern(@Header("Authorization") token: String, @Body concernBody: ConcernBody) : Response<Void>

    // FOR GET LIST OF CONCERNS
    @GET("/appapi/concerns/")
    suspend fun getListOfConcerns(@Header("Authorization") token: String): Response<GetListOfConcerns>

    @Headers("Content-Type: application/json")
    // FOR ADD COMMENT ON CONCERN
    @POST("/appapi/concern_comments/")
    suspend fun createConcernComment(@Header("Authorization") token: String, @Body concernCommentBody: ConcernCommentBody) : Response<Comments>

    // FOR GET FAVORITE PROJECTS
    @GET("/appapi/favourite_projects/")
    suspend fun GetFavoriteProjects(
        @Header("Authorization") token: String
    ): Response<GetFavouriteProjectsEntity>

    // FOR REMOVE FAVORITE PROJECTS
    @HTTP(method = "DELETE", path="/appapi/favourite_projects/", hasBody = true)
    suspend fun RemoveFavouriteProjects(
        @Header("Authorization") token: String,@Body projectId: GetFavProjectId
    ): Response<ResponseBody>

    // FOR REMOVE FAVORITE PROJECTS
    @Headers("Content-Type: application/json")
    @POST("/appapi/favourite_projects/")
    suspend fun AddFavouriteProjects(
        @Header("Authorization") token: String,@Body projectId: GetFavProjectId
    ): Response<ResponseBody>


    //FOR GET ALL DEALS LIST
    @GET("/appapi/deals/")
    suspend fun GetAllDeals(
        @Header("Authorization") token: String
    ): Response<ListOfDeals>

    //FOR DEAL DETAIL CHEQUE IMAGE
    @Multipart
    @PATCH("/appapi/deal_cheque_image")
    suspend fun DealDetailChequeImage(
        @Header("Authorization") token: String,
        @Part("id") id : Int,
        @Part cheque_image: MultipartBody.Part?
    ): Response<DealDetailChequeImageEntity>


    @GET("/appapi/user_project_commission_slab_details/")
    suspend fun GetCommissionSlab(@Header("Authorization") token: String): Response<List<ProjectCommissionSlab>>

    // FOR GET LIST OF NOTIFICATIONS
    @GET("/appapi/notifications")
    suspend fun GetListOfNotifications(@Header("Authorization") token: String): Response<NotificationData>

//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//    FOR FCM :-----


//    FOR ADD FCM DEVICE
    @Headers("Content-Type: application/json")
    @POST("/appapi/fcm_devices/")
    suspend fun AddFcmDeviceApi(@Header("Authorization") token: String, @Body addFcmDevice: AddFcmDevice): Response<AddFcmDeviceEntity>

//    FOR DELETE FCM DEVICE
    @HTTP(method = "DELETE", path = "/appapi/fcm_devices/0/", hasBody = true)
//    @DELETE("/appapi/fcm_devices/0/")
    suspend fun deleteFcmDevice(@Header("Authorization") token: String, @Body reg: RegistrationId) : Response<Void>



    @GET("/appapi/broker_clients")
    suspend fun GetClientListApi(@Header("Authorization") token: String): Response<ClientList>

    @HTTP(method = "DELETE", path="/appapi/broker_clients/{id}/")
    suspend fun RemoveClientItems(
        @Header("Authorization") token: String, @Path("id") clientId: Int
    ): Response<ResponseBody>

    //FOR CREATE/UPDATE USER PROFILE
    @Headers("Content-Type: application/json")
    @POST("appapi/broker_clients")
    suspend fun CreateClientProfile(@Header("Authorization") token: String, @Body createClientData: CreateClientBody): Response<CreateClientData>

    //FOR UPDATE CLIENT PROFILE
    @PATCH("appapi/broker_clients/{id}/")
    suspend fun UpdateClientProfile(@Header("Authorization") token: String, @Path("id") id: Int,
                                    @Body createClientData: CreateClientBody): Response<CreateClientData>

    //FOR CREATE CLIENT DOCS

    @Multipart
    @POST("appapi/broker_client_docs")
    suspend fun CreateClientDocs(
        @Header("Authorization") token: String,
        @Part("name") document_name: RequestBody,
       @Part("broker_client") document_id: RequestBody,
        @Part document: MultipartBody.Part
    ): Response<CreateClientDocData>

    //FOR GET CLIENT DOCS
    @GET("/appapi/broker_client_docs")
    suspend fun GetClientDocs(
        @Header("Authorization") token: String): Response<GetClientDocListEntity>

    //FOR REMOVE CLIENT DOCS
    @HTTP(method = "DELETE", path="/appapi/broker_client_docs/{id}/")
    suspend fun RemoveClientDocs(
        @Header("Authorization") token: String, @Path("id") docId: Int
    ): Response<ResponseBody>

    //FOR UPDATE CLIENT Doc
    @Multipart
    @PATCH("appapi/broker_client_docs/{id}/")
    suspend fun UpdateClientDocs(@Header("Authorization") token: String,
                                 @Path("id") id: Int,
                                 @Part("name") document_name: RequestBody,
                                 @Part document: MultipartBody.Part): Response<UpdateClientdocData>

    //FOR GET Events Categories
    @GET("api/event_categories")
    suspend fun GetEventCategories(
        @Header("Authorization") token: String): Response<GetEventCategoriesList>

    //FOR GET Events Data List
    @GET("appapi/events")
    suspend fun GetEventDataList(
        @Header("Authorization") token: String): Response<GetEventsDataEntity>

    //FOR CREATE Events
    @Headers("Content-Type: application/json")
    @POST("appapi/events")
    suspend fun CreateEvent(@Header("Authorization") token: String, @Body createEventBody: CreateEventBody): Response<CreateEventData>

    //FOR UPDATE Events
    @PATCH("appapi/events/{id}/")
    suspend fun UpdateEvents(@Header("Authorization") token: String, @Path("id") id: Int,
                                    @Body createEventBody: CreateEventBody): Response<CreateEventData>


    //FOR REMOVE Events
    @HTTP(method = "DELETE", path="/appapi/events/{id}/")
    suspend fun RemoveEvents(
        @Header("Authorization") token: String, @Path("id") eventId: Int
    ): Response<ResponseBody>


    //FOR GET Reimbursement Types
    @GET("appapi/reimbursement_types")
    suspend fun GetReimbursementTypes(
        @Header("Authorization") token: String): Response<GetReimbursementTypeEntity>

    //FOR GET Reimbursement Data List
    @GET("appapi/reimbursements")
    suspend fun GetReimbursementList(
        @Header("Authorization") token: String): Response<GetReimbursementListEntity>

    //FOR CREATE Reimbursement
    @Headers("Content-Type: application/json")
    @POST("appapi/reimbursements")
    suspend fun CreateReimbursement(@Header("Authorization") token: String,
                                    @Body createReimburseBody: CreateReimburseBody): Response<CreateReimbursementData>

    //FOR UPDATE Events
    @PATCH("appapi/reimbursements/{id}/")
    suspend fun UpdateReimbursement(@Header("Authorization") token: String, @Path("id") id: Int,
                             @Body createReimburseBody: CreateReimburseBody): Response<CreateReimbursementData>


    //FOR REMOVE Events
    @HTTP(method = "DELETE", path="/appapi/reimbursements/{id}/")
    suspend fun RemoveReimbursement(
        @Header("Authorization") token: String, @Path("id") reimbursementId: Int
    ): Response<ResponseBody>

    //FOR CREATE REIMBURSE DOCS
    @Multipart
    @POST("appapi/reimbursement_docs")
    suspend fun CreateReimburseDocs(
        @Header("Authorization") token: String,
        @Part("name") document_name: RequestBody,
        @Part("reimbursement") document_id: RequestBody,
        @Part document: MultipartBody.Part
    ): Response<CreateReimbursementDocs>

    //FOR GET Reimburse DOCS
    @GET("/appapi/reimbursement_docs")
    suspend fun GetReimburseDocList(
        @Header("Authorization") token: String): Response<GetReimburseDocListEntity>

    //FOR REMOVE Reimburse DOCS
    @HTTP(method = "DELETE", path="/appapi/reimbursement_docs/{id}/")
    suspend fun RemoveReimburseDocs(
        @Header("Authorization") token: String, @Path("id") docId: Int
    ): Response<ResponseBody>

    //FOR UPDATE Reimburse Doc
    @Multipart
    @PATCH("appapi/reimbursement_docs/{id}/")
    suspend fun UpdateReimburseDocs(@Header("Authorization") token: String,
                                 @Path("id") id: Int,
                                 @Part("name") document_name: RequestBody,
                                 @Part document: MultipartBody.Part): Response<CreateReimbursementDocs>


    class RegistrationId(regId: String){
        val registration_id = regId
    }


//    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


    companion object {
        val gson = Gson()
        operator fun invoke(): AuthApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttp.build())
                .build()
                .create(AuthApi::class.java)
        }
    }
}

class AddFcmDevice(type: String?, regId: String, deviceId: String?){
    val type = type
    val registration_id = regId
    val device_id = deviceId
}

class ConcernBody(heading: String, description: String, closed: Boolean?){
    val heading = heading
    val description = description
    val closed = closed
}

class ConcernCommentBody(concernId: Int, body: String){
    val concern_id = concernId
    val body = body
}

class LeadStatus(leadstatus: Int){
    val lead_status = leadstatus
}

class UpdateLeadFeedbackStar(feedback_comment: String?, quality: Float, submitted: Boolean){
    val feedback_comment = feedback_comment
    val quality = quality
    val feedback_submitted = submitted
}

class UpdateLeadCreateActivity(lead: Int, description: String, date: String){
    val lead = lead
    val short_description = description
    val date = date
}

class AuthValBody(pkValue: Int, otpValue: String) {
    val pk: Int = pkValue
    val otp: String = otpValue
}

class CreateProfileBody(
    firstname: String, lastname: String?, email: String, country: Any?, city: Any?, state: String, zipcode: String
    ) {
    val first_name: String = firstname
    val last_name: String? = lastname
    val email: String = email
    val country: Any? = country
    val city: Any? = city
    val state: String = state
//    val latitude: String = latitude
//    val longitude: String = longitude
    val zip_code: String = zipcode
}

class CreateClientBody(
      name: String, phone_number: String?, email: String, country: Any?, city: Any?, location: Any?, address: String
) {
    val name: String = name
    val phone_number: String? = phone_number
    val email: String = email
    val country: Any? = country
    val city: Any? = city
    val location: Any? = location
    val address: String = address
}

class CreateEventBody(
    category: String,active: Boolean,auto_reminder: Boolean?, date: String?, time: String?, subject: String?,
    description: String?,priority: String?
) {
    val category: String = category
    val active: Boolean = active
    val auto_reminder: Boolean? = auto_reminder
    val date: String? = date
    val time: String? = time
    val subject: String? = subject
    val description: String? = description
    val priority: String? = priority


}

class CreateReimburseBody(
     amount: String,type: String
) {
    val amount: String = amount
    val type: String = type

}
