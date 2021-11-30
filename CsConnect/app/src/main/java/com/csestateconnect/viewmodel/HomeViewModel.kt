package com.csestateconnect.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csestateconnect.R
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.getProfileData.GetProfileData
import com.csestateconnect.db.data.homedata.*
import com.csestateconnect.db.data.homepagedata.banner.GetHomeBanner
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.db.data.notifications.NotificationData
import com.csestateconnect.repo.*
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.utils.Coroutines.io
import com.csestateconnect.utils.DatenTimeChatConverter
import com.csestateconnect.utils.DatenTimeConverter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.cs_assign_leads_dialog_item.view.close_dialog_button
import kotlinx.android.synthetic.main.raise_concern_dialog.view.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Response


class HomeViewModel(application: Application) : AndroidViewModel(application)
     {
//BottomNavigationView.OnNavigationItemSelectedListener
//    var menuItem = MutableLiveData<Int>()
    /* override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when (item.title) {
           *//*  "Home" -> {
                menuItem.value = 1
                return true
            }
            "Projects" -> {
                menuItem.value = 2
                return true
            }
            "Call RM" -> {
                menuItem.value = 3
                return true
            }

            "Leads" -> {
                menuItem.value = 4
                return true
            }*//*

//            "Deal" -> {
//                menuItem.value = 5
//                return true
//            }
           *//* "Get In Touch" -> {
                menuItem.value = 5
                return true
            }*//*

        }
        return false
//                Navigation.createNavigateOnClickListener(R.id.action_homeFrag1_to_projectsFrag22)
    }*/

    private var repository = HomeRepository(application)
    private var authRepository = AuthRepository(application)
    private var profileRepository = ProfileRepository(application)
    private var verificationRepository = VerificationRepository(application)
    private var userAccountRepository = AccountDetailRepository(application)
    var userDetail = ArrayList<String?>()
    var username = MutableLiveData<String>()
    var headerImge: String? = null
    var headerImgeView = MutableLiveData<String>()
    var userCity = MutableLiveData<String>()
    var userCountry = MutableLiveData<String>()

    //    var menuItem = MutableLiveData<Int>()
    var menuIconVisibility = MutableLiveData<Int>()
    var navigationItemVisibility = MutableLiveData<Int>()
    var tabViewItemVisibility = MutableLiveData<Int>()

    var checkResponseRemove = MutableLiveData<Boolean>().apply{postValue(false)}

    var checkResponseAdd = MutableLiveData<Boolean>().apply{postValue(false)}


    var progressBarHomeLoad = MutableLiveData<Int>().apply { postValue(View.GONE) }

    var tabItem = MutableLiveData<Int>().apply { postValue(0) }

    // HOME PAGE
    var homePageBannerImage = MutableLiveData<GetHomeBanner>()
    var headerViewData = MutableLiveData<GetProfileData>()

    //Commission
    var commission_1 = MutableLiveData<String>()
    var commission_2 = MutableLiveData<String>()
    var commission_3 = MutableLiveData<String>()
    var commission1: Int? = -1
    var commission2: Int? = -1
    var commission3: Int? = -1

    var projectName1 = MutableLiveData<String>().apply { postValue("Project Name") }
    var projectName2 = MutableLiveData<String>().apply { postValue("Project Name") }
    var projectName3 = MutableLiveData<String>().apply { postValue("Project Name") }
    var projectName4 = MutableLiveData<String>().apply { postValue("Project Name") }
    var projectName5 = MutableLiveData<String>().apply { postValue("Project Name") }

    var projectLocationTxt1 = MutableLiveData<String>().apply { postValue("--") }
    var projectLocationTxt2 = MutableLiveData<String>().apply { postValue("--") }
    var projectLocationTxt3 = MutableLiveData<String>().apply { postValue("--") }
    var projectLocationTxt4 = MutableLiveData<String>().apply { postValue("--") }
    var projectLocationTxt5 = MutableLiveData<String>().apply { postValue("--") }


    var brokersName = ArrayList<String?>()
    var brokerName1 = MutableLiveData<String>().apply { postValue("Name") }
    var brokerName2 = MutableLiveData<String>().apply { postValue("Name") }
    var brokerName3 = MutableLiveData<String>().apply { postValue("Name") }
    var brokerName4 = MutableLiveData<String>().apply { postValue("Name") }
    var brokerName5 = MutableLiveData<String>().apply { postValue("Name") }
    var response: Response<ResponseBody>? = null

    var setProjectsDataHomePage = MutableLiveData<Int>().apply { postValue(-1) }
    var top5ProjectData: List<ProjectDetail>? = null
    var selectedCityNameForProjects = ""

    // Concerns
    var progressBarGetInTouch = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var rmPhoneNumber: String? = null

    var concernDetailTitleText = MutableLiveData<String>()
    var concernDetailDateText = MutableLiveData<String>()
    var concernDetailDescriptionText = MutableLiveData<String>()
    var concernDetailStatusText = MutableLiveData<String>()
    var concernDetailDateChatText = MutableLiveData<String>()
    var concernSendCommentLayout = MutableLiveData<Int>()

    var concernIdForComment: Int = 0
    var commentChatText = MutableLiveData<String>().apply { postValue("") }
    var progressBarConcernChat = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var nullDataName =
        listOf("Name", "Name", "Name", "Name", "Name")


    // citiesFragment for Broker
    var cities_recyclerView = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var city_drop_down = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var city_drop_up = MutableLiveData<Int>().apply { postValue(View.VISIBLE) }
    var brokersLayout = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var projectsLayout = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var cityTxt = MutableLiveData<String>().apply { postValue("Select City") }
    var allBrokersDetails: Brokers? = null
    var countValue: String? = null

    // citiesFragment for Projects
    var allProjectDetails: Projects? = null
    var projectsIcons1 = MutableLiveData<String>()
    var projectsIcons2 = MutableLiveData<String>()
    var projectsIcons3 = MutableLiveData<String>()
    var projectsIcons4 = MutableLiveData<String>()
    var projectsIcons5 = MutableLiveData<String>()




    init {
        getValuesFromDataBase()
    }

    fun getHomeBanner(view: View?) {
        if (userCity.value == null || userCity.value == "") {
            userCity.value = "Noida"
        }

        if (userCountry.value == null || userCountry.value == "") {
            userCountry.value = "India"
        }

        Coroutines.io {
            try {
                val allCountryData = authRepository.getallDataCountriesFromDataBAse()
                val countryList = allCountryData.map { it.name }
                val countryindex = countryList.indexOf(userCountry.value!!)
                val countryId = allCountryData.get(countryindex).id
                val cityList = allCountryData.get(countryindex).cities.map { it.name }
                val cityIndex = cityList.indexOf(userCity.value!!)
                val cityId = allCountryData.get(countryindex).cities.get(cityIndex).id

                val bannerData = repository.getHomeBannerData()



            if (!bannerData.isNullOrEmpty()) {
                Coroutines.main {
                    homePageBannerImage.value = bannerData.get(0)
                }
            }
            else {
                if (isNetworkConnected(view!!.context)) {
                    val response = repository.getHomeBanner(cityId, countryId)
                    if (response.isSuccessful) {
                        Coroutines.main {
                            homePageBannerImage.value = response.body()
                        }
                    }
                }
            }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun setHomeCommission(view: View?) {
        Coroutines.io {
            var response : Response<Homedata>? = null
            val data = repository.getHomeDataFromDatabase()
            if (data.isNullOrEmpty()){
                if (isNetworkConnected(view!!.context)) {
                    response = repository.getHomeData()
                    if (response != null) {
                        if (response.isSuccessful) {
                            val commission = repository.getCommissionFromDatabase()
                            commission1 = commission?.commission_amount_total__sum
                            commission2 = commission?.commission_amount_paid__sum
                            commission3 = commission?.commission_amount_unpaid__sum

                            Coroutines.main {
                                commission_1.value = commission1.toString()
                                commission_2.value = commission2.toString()
                                commission_3.value = commission3.toString()
                            }

                            setTop5BrokersData("Delhi NCR")
                        }
                    }
                }
            }
            else {
                val commission = repository.getCommissionFromDatabase()
                commission1 = commission?.commission_amount_total__sum
                commission2 = commission?.commission_amount_paid__sum
                commission3 = commission?.commission_amount_unpaid__sum

                Coroutines.main {
                    commission_1.value = commission1.toString()
                    commission_2.value = commission2.toString()
                    commission_3.value = commission3.toString()
                }

                setTop5BrokersData("Delhi NCR")
            }

        }
    }

    fun setTop5BrokersData(cityName: String) {

        var brokerData: List<BrokerDetail>
        Coroutines.io {
            try {
                brokerData = repository.getTop5BrokersFromDatabase()
                if (!brokerData.isNullOrEmpty()) {
                    when (cityName) {
                        "Mumbai" -> {
                            val data = brokerData!!.filter { it.city == "Mumbai" }
                            val name = data.map { it.name }
                            Coroutines.main {
                                if (!data.isNullOrEmpty()) {
                                    if (data.size >= 5) {
                                        setBrokersName(name)
                                    } else {
                                        setBrokersName(nullDataName)
                                    }
                                } else {
                                    setBrokersName(nullDataName)
                                }
                            }
                        }

                        "Bangalore" -> {
                            val data = brokerData!!.filter { it.city == "Bangalore" }
                            val name = data.map { it.name }
                            Coroutines.main {
                                if (!data.isNullOrEmpty()) {
                                    if (data.size >= 5) {
                                        setBrokersName(name)
                                    } else {
                                        setBrokersName(nullDataName)
                                    }
                                } else {
                                    setBrokersName(nullDataName)
                                }
                            }
                        }

                        else -> {
                            val data = brokerData!!
                            val name = data.map { it.name }
                            Coroutines.main {
                                if (!data.isNullOrEmpty()) {
                                    if (data.size >= 5) {
                                        setBrokersName(name)
                                    } else {
                                        setBrokersName(nullDataName)
                                    }
                                } else {
                                    setBrokersName(nullDataName)
                                }
                            }
                        }
                    }

                    setTop5ProjectsData("Delhi NCR")
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


    fun setTop5ProjectsData(cityName: String){
        val nullDataName =
            listOf("Project Name", "Project Name", "Project Name", "Project Name", "Project Name")
        Coroutines.io {
            try {
                top5ProjectData = repository.getTop5ProjectsFromDatabase()
                if (!top5ProjectData.isNullOrEmpty()) {
                    Coroutines.main {
                        selectedCityNameForProjects = cityName
                        setProjectsDataHomePage.value = 1
                    }
                }
                else {
                    Coroutines.main {
                        setProjectsDataHomePage.value = 0
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setBrokersName(brokerList: List<String>) {
        brokerName1.value = brokerList[0]
        brokerName2.value = brokerList[1]
        brokerName3.value = brokerList[2]
        brokerName4.value = brokerList[3]
        brokerName5.value = brokerList[4]
    }

    fun getRmNumber(view: View) {
        if (rmPhoneNumber != null) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: ${rmPhoneNumber}")
            view.context.startActivity(intent)
        }
    }

    fun getChannelPartnersData(context: Context) {
//        progressBarCreateProf.value = View.VISIBLE
        var userName: String? = null
        io {

            val userData =  profileRepository.getUserPersonalDetails()

            if (!userData.isNullOrEmpty()) {
                Coroutines.main {
                    try {
                        headerViewData.value  = userData.get(0)
                       val headerdata = headerViewData.value!!
                            userName = headerdata.firstName
                            userDetail.add(headerdata.firstName)
                            userDetail.add(headerdata.lastName)
                            headerImge = headerdata.profileImage
                            rmPhoneNumber = headerdata.rm?.phoneNumber
                            userCity.value = headerdata.city.toString()
                            userCountry.value = headerdata.country.toString()

                            Log.i("profileTag", headerdata.profileImage)



                        if (userName != null) {

                                username.value = userDetail.get(0) + " " + userDetail.get(1)
                                headerImgeView.value = headerImge
                                Log.i("profileTag", "API Working!!")
                            }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            else {
                if (isNetworkConnected(context)) {
                    try {
                        val response = profileRepository.getUserProfileData()
                    if (response!!.isSuccessful) {
                        Coroutines.main {
                            headerViewData.value = response.body()
                        }
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                }
                }
            }

        }

    }


    fun logOut(application: Application): Boolean? {

        var response: Response<Void>? = null
        runBlocking {
            response = repository.deleteFcmDevice()

            if (response!!.isSuccessful) {
                authRepository.deleteAllDataFromDatabase()
            } else {
                Coroutines.main {
                    Toast.makeText(
                        application,
                        "Please connect to internet",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        return response?.isSuccessful

    }

    fun getValuesFromDataBase() {
        io {
            try {
                profileRepository.getUserProfileData()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                userAccountRepository.getUserAccountData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                verificationRepository.getVerifyImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun callSearchProjectAPIAndStore(cityNameQuery: String): Response<ResponseBody>? {
        val response = repository.callSearchProjectByCityApi(cityNameQuery)

        return response
    }

    fun getProjectFromAPIAndStore() {
        Coroutines.main {
            repository.getProjectsdata()
        }
    }

    suspend fun callSearchProjectByProjectName(projectNameQuery: String): Response<ResponseBody>? {
        val response = repository.callSearchProjectByProjNameApi(projectNameQuery)

        return response
    }

    fun getFavouriteProjectsDatabase(): LiveData<List<GetFavouriteProjectsEntity>> {
        return repository.getFavoriteProjectFromDatabase()
    }

    fun callGetFavouriteProjectAPIAndStore() {
        Coroutines.main {
            repository.getFavouriteProjectsdata()
        }
    }

    fun callAddFavouriteProjectsApi(projectId: Int) {
        Coroutines.main {
            val response = repository.addFavouriteProjects(projectId)

            if(response!!.isSuccessful){

                checkResponseAdd.value =true

            }
        }
    }

    fun callRemoveFavouriteProjectsApi(projectId: Int) {
        Coroutines.main {
            val response = repository.removeFavouriteProjectsdata(projectId)
            if(response!!.isSuccessful){
                checkResponseRemove.value =true

            }
        }
    }


    fun earningIssueConcern(view: View) {
        openConcernDialogView("Earnings related issue", view)
    }

    fun supportIssueConcern(view: View) {
        openConcernDialogView("Support related issue", view)
    }

    fun openConcernDialogView(headingText: String, view: View) {
        val mDialogView =
            LayoutInflater.from(view.context).inflate(R.layout.raise_concern_dialog, null)

        mDialogView.dialog_heading_text.text = headingText
        val mBuilder = AlertDialog.Builder(view.context!!).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.concern_submit_button.setOnClickListener {
            progressBarGetInTouch.value = View.VISIBLE
            val description = mDialogView.concern_description.text.toString()
            runBlocking {
                val response = repository.raiseConcern(
                    mDialogView.dialog_heading_text.text.toString(),
                    description
                )
                if (response.isSuccessful) {
                    Toast.makeText(view.context, "Concern Raised", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(view.context, "Unknown Error", Toast.LENGTH_LONG).show()
                }
                Coroutines.main {
                    progressBarGetInTouch.value = View.GONE
                }
                mAlertDialog.dismiss()
            }
        }
        mDialogView.close_dialog_button.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun getListOfConcerns() {
        runBlocking {
            repository.getListOfConcerns()
        }
    }

    fun getallConcernsData(): LiveData<GetListOfConcerns> {
        return repository.getListOfConcernsData()
    }

    suspend fun getListOfNotificationsApi(): Response<NotificationData> {
        return repository.getListOfNotificationsApi()

    }

    fun getNotificationsData(): LiveData<NotificationData> {
        return repository.getNotificationDataFromDB()
    }

    fun getConcernDetailById(
        data: GetListOfConcerns,
        concernIndex: Int
    ) {
        try {
            val index = concernIndex
            concernIdForComment = data.results?.get(index)?.id!!
            concernDetailTitleText.value =
                "${data.results?.get(index)?.heading}. ID# ${data.results?.get(index)?.id}"
            concernDetailDateText.value =
                DatenTimeConverter().dateConverter(data.results?.get(index)?.created_at!!)
            concernDetailDateChatText.value =
                DatenTimeChatConverter().dateConverter(data.results?.get(index)?.created_at!!)
            concernDetailDescriptionText.value = data.results?.get(index)?.description
            if (data.results?.get(index)?.closed) {
                concernDetailStatusText.value = "Status- Close"
                concernSendCommentLayout.value = View.GONE
            } else {
                concernDetailStatusText.value = "Status- Open"
                concernSendCommentLayout.value = View.VISIBLE
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    fun commentChatSend(view: View) {

        if (commentChatText.value != null && commentChatText.value != "") {
            progressBarConcernChat.value = View.VISIBLE
            runBlocking {
                val response =
                    repository.createConcernComment(concernIdForComment, commentChatText.value!!)
                if (response.isSuccessful) {
                    getListOfConcerns()
//                    trymutable.value = true
                }
            }
            progressBarConcernChat.value = View.GONE
        }
        commentChatText.value = ""
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun getintouchCallAction(view: View){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: 01204321477}")
        ContextCompat.startActivity(view.context, intent, null)
    }

    fun  getAllBrokersWithCity() {
        Coroutines.io {
            allBrokersDetails = repository.getAllBrokersHomeFromDatabase()
            allProjectDetails = repository.getAllProjectHomeFromDatabase()
            Log.i("ghshgg","jsjsj")
        }
    }


    fun setBrokerOrProjectUI(count: String){
        Coroutines.main {
            if (count == "0") {
                brokersLayout.value = View.VISIBLE
            } else if (count == "1") {
                projectsLayout.value = View.VISIBLE
            }
        }
    }


        fun  getBrokersAndProjectName(selectedCityKey: String){
                Coroutines.main {
                    if(!selectedCityKey.equals(null)){

                        var cityKey: String? = null

                        if(countValue!! == "0"){
                            val brokerList = allBrokersDetails!!.brokerDetails
                            val brokerDetail = brokerList.groupBy({ it.city }, {it})

                        for (i in 0..brokerDetail.keys.size - 1) {
                            cityKey = brokerDetail.map { it.key }.get(i)

                            if (cityKey == selectedCityKey ) {

                                cityTxt.value = selectedCityKey
                                cities_recyclerView.value = View.GONE
                                val brokersList = brokerDetail.getValue(cityKey)
                                val name = brokersList.map { it.name }

                                    if (!brokersList.isNullOrEmpty()) {
                                        if (brokersList.size >= 5) {
                                            setBrokersName(name)
                                        } else {
                                            setBrokersName(nullDataName)
                                        }
                                    } else {
                                        setBrokersName(nullDataName)
                                    }

                            }
                        }

                    }else if(countValue!! == "1"){

                            val homeProjectList = allProjectDetails!!.projectDetails
                            val projectDetail = homeProjectList.groupBy({ it.city }, {it})

                            var nullProjectName =
                                listOf("Project Name", "Project Name", "Project Name", "Project Name", "Project Name")

                            var nullProjectLocation =
                                listOf("--", "--", "--", "--", "--")

                            for (i in 0..projectDetail.keys.size - 1) {
                                cityKey = projectDetail.map { it.key }.get(i)

                                if (cityKey == selectedCityKey ) {

                                    cityTxt.value = selectedCityKey
                                    cities_recyclerView.value = View.GONE

                                    val projectList = projectDetail.getValue(cityKey)
                                    val name = projectList.map { it.name }
                                    val location = projectList.map { it.city }
                                    val icons = projectList.map { it.icon_image }

                                    if (!projectList.isNullOrEmpty()) {

                                        if (projectList.size >= 5) {
                                            setProjectsName(name, location)
                                        } else {
                                            projectName1.value = name[0]
                                            projectLocationTxt1.value = location[0]
                                            projectsIcons1.value = icons[0]
//                                            setProjectsName(nullProjectName,nullProjectLocation )
                                        }
                                    } else {
                                        setProjectsName(nullProjectName, nullProjectLocation)
                                    }
                                }
                            }
                    }
                    }
                }

    }

    fun setProjectsName(projectName: List<String>, projectLocation: List<String>) {
        try {
            projectName1.value = projectName[0]
            projectName2.value = projectName[1]
            projectName3.value = projectName[2]
            projectName4.value = projectName[3]
            projectName5.value = projectName[4]
        }catch (e: Exception){
            e.printStackTrace()
        }

        try {
            projectLocationTxt1.value = projectLocation[0]
            projectLocationTxt2.value = projectLocation[1]
            projectLocationTxt3.value = projectLocation[2]
            projectLocationTxt4.value = projectLocation[3]
            projectLocationTxt5.value = projectLocation[4]
        }catch (e: Exception){
            e.printStackTrace()
        }

//        try {
//            projectsIcons1.value = projectIcons[0]
//            projectsIcons2.value = projectIcons[1]
//            projectsIcons3.value = projectIcons[2]
//            projectsIcons4.value = projectIcons[3]
//            projectsIcons5.value = projectIcons[4]
//        }catch (e: Exception){
//            e.printStackTrace()
//        }

    }

    fun citiesRecyclerViewVisibility(view: View){
        if (cities_recyclerView.value == View.GONE) {
            city_drop_down.value = View.GONE
            city_drop_up.value = View.VISIBLE
            cities_recyclerView.value = View.VISIBLE
        } else {
            cities_recyclerView.value = View.GONE
            city_drop_down.value = View.VISIBLE
            city_drop_up.value = View.GONE
        }
    }
}