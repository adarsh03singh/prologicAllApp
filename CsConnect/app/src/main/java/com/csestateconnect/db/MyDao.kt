package com.csestateconnect.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.csestateconnect.db.data.*
import com.csestateconnect.db.data.commissionSlab.ProjectCommissionSlab
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.deals.ListOfDeals
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.homedata.Homedata
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.db.data.assignedProjects.GetAssignedProjects
import com.csestateconnect.db.data.AuthGenEntity
import com.csestateconnect.db.data.AuthValEntity
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.events.GetEventCategoriesList
import com.csestateconnect.db.data.events.eventsDataList.GetEventsDataEntity
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.fcm.AddFcmDeviceEntity
import com.csestateconnect.db.data.homepagedata.banner.GetHomeBanner
import com.csestateconnect.db.data.leads.CreateLeadActivity
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfConcerns.Comments
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.db.data.notifications.NotificationData
import com.csestateconnect.db.data.reimbursements.GetReimbursementTypeEntity
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.GetReimburseDocListEntity

// DAO CAN BE ABSTRACT CLASS OR INTERFACE
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)
}

@Dao
abstract class AuthGenDao: BaseDao<AuthGenEntity> {
    @Query("DELETE FROM Otp_Table")
    abstract fun delete()

    @Query("SELECT * from Otp_Table")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<AuthGenEntity>
}

@Dao
interface AuthValDao: BaseDao<AuthValEntity> {
    @Query("DELETE FROM Otp_Validate")
    fun delete()

    @Query("SELECT * from Otp_Validate")
    @JvmSuppressWildcards
    fun getAllData(): List<AuthValEntity>
}

@Dao
interface CreateProfileDao: BaseDao<CreateProfileData> {

    @Query("DELETE FROM CreateProfileTable")
    fun deleteProfile()

    @Query("SELECT * from CreateProfileTable")
    @JvmSuppressWildcards
    fun getAllDataProfile(): List<CreateProfileData>
}

@Dao
interface GetProfileDao: BaseDao<GetProfileData> {
    @Query("DELETE FROM GetProfileTable")
    fun delete()

    @Query("SELECT * from GetProfileTable")
    @JvmSuppressWildcards
    fun getAllProfileData(): List<GetProfileData>
}

@Dao
interface GetProfileImageDao: BaseDao<UpdateImageEntity> {
    @Query("DELETE FROM ProfileImage")
    fun delete()

    @Query("SELECT * from ProfileImage")
    @JvmSuppressWildcards
    fun getProfileImageData(): List<UpdateImageEntity>
}

@Dao
interface CreateVerificationDao: BaseDao<CreateVerificationDataEntity> {

    @Query("DELETE FROM Verification_table")
    fun delteCreateVerification()

    @Query("SELECT * from Verification_table")
    @JvmSuppressWildcards
    fun getVerificationCreateData(): List<CreateVerificationDataEntity>
}


@Dao
interface GetVerificationDao: BaseDao<GetVerificationData> {

    @Query("DELETE FROM VerificationData_table")
    fun deleteGetVerificationData()

    @Query("SELECT * from VerificationData_table")
    @JvmSuppressWildcards
    fun getAllVerificationData(): List<GetVerificationData>
}

@Dao
interface CreateAccountDetaileDao: BaseDao<CreateAccountDetailEntity> {

    @Query("DELETE FROM Account_Detail")
    fun deleteAccountDetail()

    @Query("SELECT * from Account_Detail")
    @JvmSuppressWildcards
    fun getAllAccountData(): List<CreateAccountDetailEntity>
}

@Dao
interface GetAccountDetaileDao: BaseDao<GetAccountDetailEntity> {

    @Query("DELETE FROM useracountdetail")
    fun deleteUserAccountDetail()

    @Query("SELECT * from useracountdetail")
    @JvmSuppressWildcards
    fun getAllUserAccountData(): List<GetAccountDetailEntity>
}

@Dao
abstract class CountriesDao: BaseDao<List<Countries>>{
    @Query("DELETE FROM countries_list")
    abstract fun delete()

    @Query("SELECT * from countries_list")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<Countries>

    @Query("SELECT * from countries_list")
    @JvmSuppressWildcards
    abstract fun getAllLiveData(): LiveData<List<Countries>>
}

@Dao
abstract class ListOfLeadsDao: BaseDao<ListOfLeads>{
    @Query("DELETE FROM list_of_leads")
    abstract fun delete()

    @Query("SELECT * from list_of_leads")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<ListOfLeads>>

    @Query("SELECT * from lead_activity")
    @JvmSuppressWildcards
    abstract fun getAllActivityData(): LiveData<List<CreateLeadActivity>?>

//    @Update
//    abstract fun update(leadstatus: Int)
}

@Dao
abstract class LeadsResultDao: BaseDao<Result>{
    @Query("DELETE FROM lead_result")
    abstract fun delete()

    @Query("SELECT * from lead_result")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<Result>>
}

@Dao
abstract class GetAssignedProjectsDao: BaseDao<GetAssignedProjects?>{
    @Query("DELETE FROM assigned_projects")
    abstract fun delete()

    @Query("SELECT * from assigned_projects")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetAssignedProjects>
}

@Dao
abstract class ListOfDealsDao: BaseDao<ListOfDeals>{
    @Query("DELETE FROM list_of_deals")
    abstract fun delete()

    @Query("SELECT * from list_of_deals")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<ListOfDeals>>
}

@Dao
abstract class LeadsStatusDao: BaseDao<List<GetLeadStatus>>{
    @Query("DELETE FROM lead_status")
    abstract fun delete()

    @Query("SELECT * from lead_status")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<GetLeadStatus>>
}

@Dao
abstract class LeadActivityDao: BaseDao<CreateLeadActivity>{
    @Query("DELETE FROM lead_activity")
    abstract fun delete()

    @Query("SELECT * from lead_activity")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<CreateLeadActivity>
}

@Dao
abstract class DealsStatusDao: BaseDao<List<GetDealStatus>>{
    @Query("DELETE FROM deal_status")
    abstract fun delete()

    @Query("SELECT * from deal_status")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<GetDealStatus>>
}

@Dao
abstract class DealDetailChequeImageDao: BaseDao<DealDetailChequeImageEntity>{
    @Query("DELETE FROM deal_cheque")
    abstract fun delete()

    @Query("SELECT * from deal_cheque")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<DealDetailChequeImageEntity>
}

@Dao
abstract class CompletionStatusDao: BaseDao<List<GetCompletionStatus>>{
    @Query("DELETE FROM completion_status")
    abstract fun delete()

    @Query("SELECT * from completion_status")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<GetCompletionStatus>>
}

@Dao
abstract class PreferredPropertyDao: BaseDao<List<GetPreferredProperty>>{
    @Query("DELETE FROM preferred_property")
    abstract fun delete()

    @Query("SELECT * from preferred_property")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<GetPreferredProperty>>
}

@Dao
abstract class HomeBannerDao: BaseDao<GetHomeBanner>{
    @Query("DELETE FROM home_banner")
    abstract fun delete()

    @Query("SELECT * from home_banner")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<GetHomeBanner>
}

@Dao
abstract class HomeDataDao: BaseDao<Homedata>{
    @Query("DELETE FROM home_data")
    abstract fun delete()

    @Query("SELECT * from home_data")
    @JvmSuppressWildcards
    abstract fun getAllData(): Homedata

    @Query("SELECT * from home_data")
    @JvmSuppressWildcards
    abstract fun getAllDataAsList(): List<Homedata>

    @Query("SELECT count(id) from home_data")
    abstract fun checkIfNull(): Int
}

@Dao
abstract class ProjectsDataDao: BaseDao<Projectsdata>{
    @Query("DELETE FROM get_projects")
    abstract fun delete()

    @Query("SELECT * from get_projects")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<Projectsdata>>

}

@Dao
abstract class FavouriteProjectsDataDao: BaseDao<GetFavouriteProjectsEntity>{
    @Query("DELETE FROM get_favourite_projects")
    abstract fun delete()

    @Query("SELECT * from get_favourite_projects")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<GetFavouriteProjectsEntity>>

    @Query("SELECT * from get_favourite_projects")
    @JvmSuppressWildcards
    abstract fun getFavProjData(): List<GetFavouriteProjectsEntity>

}

@Dao
abstract class CommissionSlabDataDao: BaseDao<List<ProjectCommissionSlab>>{
    @Query("DELETE FROM get_commission_slab")
    abstract fun delete()

    @Query("SELECT * from get_commission_slab")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<List<ProjectCommissionSlab>>

}

@Dao
abstract class ListOfConcernsDao: BaseDao<GetListOfConcerns> {
    @Query("DELETE FROM concern_list")
    abstract fun delete()

    @Query("SELECT * from concern_list")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetListOfConcerns>

}

@Dao
abstract class ConcernCommentsdao: BaseDao<Comments> {
    @Query("DELETE FROM concern_comments")
    abstract fun delete()

    @Query("SELECT * from concern_comments")
    @JvmSuppressWildcards
    abstract fun getAllData(): Comments

}

@Dao
abstract class AddFcmDeviceDao: BaseDao<AddFcmDeviceEntity> {
    @Query("DELETE FROM fcm_device")
    abstract fun delete()

    @Query("SELECT * from fcm_device")
    @JvmSuppressWildcards
    abstract fun getAllData(): AddFcmDeviceEntity
}

@Dao
abstract class NotificationDao: BaseDao<NotificationData> {
    @Query("DELETE FROM notification_data")
    abstract fun delete()

    @Query("SELECT * from notification_data")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<NotificationData>
}

@Dao
abstract class ClientListDao: BaseDao<ClientList> {
    @Query("DELETE FROM clients_list_data")
    abstract fun delete()

    @Query("SELECT * from clients_list_data")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<ClientList>

    @Query("SELECT * from clients_list_data")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<ClientList>
}

@Dao
abstract class ClientDocListDao: BaseDao<GetClientDocListEntity> {
    @Query("DELETE FROM clients_DocList_data")
    abstract fun delete()

    @Query("SELECT * from clients_DocList_data")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetClientDocListEntity>

    @Query("SELECT * from clients_DocList_data")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<GetClientDocListEntity>
}

@Dao
abstract class EventCategoriesListDao: BaseDao<GetEventCategoriesList> {
    @Query("DELETE FROM events_categoriesList_data")
    abstract fun delete()

    @Query("SELECT * from events_categoriesList_data")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetEventCategoriesList>

    @Query("SELECT * from events_categoriesList_data")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<GetEventCategoriesList>
}

@Dao
abstract class EventDataListDao: BaseDao<GetEventsDataEntity> {
    @Query("DELETE FROM eventsDataList_table")
    abstract fun delete()

    @Query("SELECT * from eventsDataList_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetEventsDataEntity>

    @Query("SELECT * from eventsDataList_table")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<GetEventsDataEntity>
}

@Dao
abstract class ReimburseTypeListDao: BaseDao<GetReimbursementTypeEntity> {
    @Query("DELETE FROM reimburse_type_data_table")
    abstract fun delete()

    @Query("SELECT * from reimburse_type_data_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetReimbursementTypeEntity>

    @Query("SELECT * from reimburse_type_data_table")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<GetReimbursementTypeEntity>
}

@Dao
abstract class ReimburseDataListDao: BaseDao<GetReimbursementListEntity> {
    @Query("DELETE FROM ReimbursDataList_table")
    abstract fun delete()

    @Query("SELECT * from ReimbursDataList_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetReimbursementListEntity>

    @Query("SELECT * from ReimbursDataList_table")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<GetReimbursementListEntity>
}

@Dao
abstract class ReimburseDocsListDao: BaseDao<GetReimburseDocListEntity> {
    @Query("DELETE FROM reimburse_docsList_table")
    abstract fun delete()

    @Query("SELECT * from reimburse_docsList_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): LiveData<GetReimburseDocListEntity>

    @Query("SELECT * from reimburse_docsList_table")
    @JvmSuppressWildcards
    abstract fun getClientListData(): List<GetReimburseDocListEntity>
}