package com.csestateconnect.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.csestateconnect.db.data.*
import com.csestateconnect.db.data.assignedProjects.GetAssignedProjects
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.commissionSlab.ProjectCommissionSlab
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.deals.ListOfDeals
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
import com.csestateconnect.db.data.listOfConcerns.Comments
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.db.data.notifications.NotificationData
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.db.data.reimbursements.GetReimbursementTypeEntity
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.GetReimburseDocListEntity

@Database(entities = [AuthGenEntity::class, AuthValEntity::class, CreateProfileData::class, GetProfileData::class,
    Countries::class, ListOfLeads::class, Homedata::class, Projectsdata::class, CreateAccountDetailEntity::class,
    GetAccountDetailEntity::class, CreateVerificationDataEntity::class,GetVerificationData::class,
    UpdateImageEntity::class, GetLeadStatus::class, CreateLeadActivity::class, GetCompletionStatus::class, GetPreferredProperty::class,
    ListOfDeals::class, Result::class, GetDealStatus::class, DealDetailChequeImageEntity::class,
    ProjectCommissionSlab::class, GetAssignedProjects::class, AddFcmDeviceEntity::class, GetListOfConcerns::class,
    Comments::class, GetHomeBanner::class, GetFavouriteProjectsEntity::class, NotificationData::class, ClientList::class,
    GetClientDocListEntity::class, GetEventCategoriesList::class,GetEventsDataEntity::class, GetReimbursementTypeEntity::class,
GetReimbursementListEntity::class,GetReimburseDocListEntity::class],
    version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val listOfLeadsDao: ListOfLeadsDao
    abstract val countriesDao: CountriesDao
    abstract val createVerificationDao: CreateVerificationDao
    abstract val createAccountDetaileDao: CreateAccountDetaileDao
    abstract val getAccountDetaileDao: GetAccountDetaileDao
    abstract val getVerificationDao: GetVerificationDao
    abstract val authgenDao: AuthGenDao
    abstract val authvalDao: AuthValDao
    abstract val createprofileDao: CreateProfileDao
    abstract val getprofileDao: GetProfileDao
    abstract val getprofileImageDao: GetProfileImageDao
    abstract val homeDataDao: HomeDataDao
    abstract val homeBannerDao : HomeBannerDao
    abstract val projectsDataDao: ProjectsDataDao
    abstract val favouriteProjectsDataDao: FavouriteProjectsDataDao
    abstract val leadStatusDao : LeadsStatusDao
    abstract val leadActivityDao : LeadActivityDao
    abstract val completionStatusdao : CompletionStatusDao
    abstract val preferredPropertyDao : PreferredPropertyDao
    abstract val commissionSlabDataDao : CommissionSlabDataDao
    abstract val leadsResultDao : LeadsResultDao

    abstract val listOfDealsDao : ListOfDealsDao
    abstract val dealStatusDao : DealsStatusDao
    abstract val dealDetailChequeImageDao : DealDetailChequeImageDao
    abstract val assignedProjectsDao : GetAssignedProjectsDao
    abstract val listOfConcernsDao : ListOfConcernsDao
    abstract val concernCommentsdao : ConcernCommentsdao

    abstract val addFcmDeviceDao : AddFcmDeviceDao
    abstract val notificationDao : NotificationDao
    abstract val clientListDao : ClientListDao
    abstract val clientDocListDao : ClientDocListDao

    abstract val eventCategoriesListDao : EventCategoriesListDao
    abstract val eventDataListDao : EventDataListDao

    abstract val reimburseTypeListDao : ReimburseTypeListDao
    abstract val reimburseDataListDao : ReimburseDataListDao
    abstract val reimburseDocsListDao : ReimburseDocsListDao


    companion object{
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "database_name"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}