package com.csestateconnect.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.csestateconnect.R
import com.csestateconnect.db.data.commissionSlab.ProjectCommissionSlab
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.repo.HomeRepository
import com.csestateconnect.repo.ProfileRepository
import com.csestateconnect.utils.Coroutines
import com.google.android.material.snackbar.Snackbar
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    var navcontroller: NavController? = null
    private var repository = HomeRepository(application)
    private var profileRepository = ProfileRepository(application)
    var previousProjectIds: Int = 0
    var project_progressBarLayout = MutableLiveData<Int>().apply { postValue(View.GONE) }

    var project_name = MutableLiveData<String>()
    var project_location = MutableLiveData<String>()
    var project_price_range = MutableLiveData<String>()
    var project_construction_status = MutableLiveData<String>()
    var project_property_type = MutableLiveData<String>()
    var project_unit_bhk = MutableLiveData<String>()
    var project_total_area = MutableLiveData<String>()
    var project_sqft_price = MutableLiveData<String>()
    var project_size_range = MutableLiveData<String>()
    var project_rera_no = MutableLiveData<String>()
    var project_reraRegisteredTxtView = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var project_reraLayout = MutableLiveData<Int>().apply { postValue(View.VISIBLE) }
    var project_HighCommissionView = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var project_info = MutableLiveData<String>()
    var project_developer_name = MutableLiveData<String>()
    var project_developProjectNo = MutableLiveData<String>()
    var project_developerInfo = MutableLiveData<String>()
    var project_developer_imageIcon: String? = null
    var project_image_no = MutableLiveData<String>()
    var project_image_icon: String? = null
    var rmPhoneNumber: String? = null
    var userCityFromDatabase: String? = null
    var userCity: String? = null


    // FOR FILTER

    var selectedProjectsProprtyList = MutableLiveData<MutableList<String>>()
    var selectedProjectsLocationId = MutableLiveData<MutableList<String>>()
    var selectedProjectsUnitPlanId = MutableLiveData<MutableList<String>>()
    var selectedProjectsStatusId = MutableLiveData<MutableList<String>>()
    var selectedProjectsAmenitiesId = MutableLiveData<MutableList<String>>()


    var clearStatusNull = MutableLiveData<Boolean>().apply { postValue(false) }
    var progressBarFilter = MutableLiveData<Int>().apply { postValue(View.GONE) }

    var clearButtonEnabled = MutableLiveData<Boolean>().apply { postValue(true) }
    var applyButtonEnabled = MutableLiveData<Boolean>().apply { postValue(true) }

    var clearButtonClicked = MutableLiveData<Boolean>().apply { postValue(false) }

    val filteredProjectsData = MutableLiveData<Projectsdata?>()

    var showFilteredProjects = MutableLiveData<Boolean>().apply { postValue(false) }

    var filterMinPriceTxt = MutableLiveData<String>()
    var filterMaxPriceTxt = MutableLiveData<String>()
    var filterMinPerSqFtPriceTxt = MutableLiveData<String>()
    var filterMaxPerSqFtPriceTxt = MutableLiveData<String>()

    var builtUp_min_edittext = MutableLiveData<String>()
    var builtUp_max_edittext = MutableLiveData<String>()

    //  var projectProperty: String? = null
    var projectLocation: String? = null
    var projectUnitPlan: String? = null
    var projectStatus: String? = null
    var projectAmenities: String? = null
    var projectPrice: String? = null
    var projectPerSqFtPrice: String? = null
    var projectBuiltUp: String? = null


    init {
        repository = HomeRepository(application)

        Coroutines.io {
            try {
                val profileData = profileRepository.getUserPersonalDetails()
                profileData.forEach {
                    rmPhoneNumber = it.rm!!.phoneNumber
                    userCityFromDatabase = it.city!!.name
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        Coroutines.main {
            userCity = userCityFromDatabase
        }

    }


    fun getAllProjectData(): LiveData<List<Projectsdata>> {
        return repository.getProjectsFromDatabase()
    }

    fun getCommissionDataFromDatabase(): LiveData<List<ProjectCommissionSlab>> {
        return repository.getCommissionsFromDatabase()
    }

    fun callCommissionAPIAndStore() {
        Coroutines.main {
            repository.getCommissionSlabDetails()
        }
    }

//    fun getFavouriteProjectsDatabase(): LiveData<List<GetFavouriteProjectsEntity>>
//    {
//        return repository.getFavoriteProjectFromDatabase()
//    }
//
//    fun callGetFavouriteProjectAPIAndStore()
//    {
//        Coroutines.main {
//            repository.getFavouriteProjectsdata()
//        }
//    }


    fun getProjectDetailByPreviousId(projectData: List<Projectsdata>, view: View) {


        try {
            navcontroller = Navigation.findNavController(view)
            val users: List<Projectsdata> = projectData
            for (i in 0..users.get(0).results!!.size - 1) {

                val projectValues = users.get(0).results!!.get(i)
                if (projectValues!!.id == previousProjectIds) {

//            try {
//                navcontroller!!.currentDestination?.label = projectValues.name
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
                    try {
                        navcontroller!!.addOnDestinationChangedListener { controller, destination, arguments ->
                            when (destination.id) {
                                R.id.projectDetailFragment -> navcontroller!!.currentDestination?.label =
                                    projectValues.name
                                else -> {
                                    navcontroller!!.currentDestination?.label
                                }
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    project_name.value = projectValues.name
                    project_location.value =
                        projectValues.location?.name + "," + projectValues.city?.name
                    project_price_range.value =
                        projectValues.lowCostView + "-" + projectValues.highCostView
                    project_construction_status.value = projectValues.projectCompletionStatus!!.name

                    project_total_area.value = projectValues.projectTotalAreaView.toString()
                    project_sqft_price.value = projectValues.projectBspCostView
                    project_size_range.value =
                        projectValues.minimumSizeView + "-" + projectValues.maximumSizeView
                    project_developer_name.value = "About " + projectValues.projectDeveloper!!.name
                    project_developProjectNo.value =
                        projectValues.projectDeveloper.noOfProjects.toString() + " Projects"
                    project_developerInfo.value = projectValues.projectDeveloper.developerInfo

                    if (!projectValues.projectDeveloper.developerInfo.equals(null)) {
                        project_developer_imageIcon = projectValues.projectDeveloper.iconImage
                    }

                    if (projectValues.highCommission == true) {
                        project_HighCommissionView.value = View.VISIBLE
                    }
                    try {
                        if(projectValues.projectImages?.size == 0){
                            project_image_no.value =  projectValues.projectImages.size.toString()
                        }else
                        project_image_no.value = " + " + projectValues.projectImages?.size.toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }



                    if (!projectValues.iconImage.equals(null)) {
                        project_image_icon = projectValues.iconImage
                    }


                    try {
                        if (!projectValues.projectTowers?.get(0)?.unitPlans?.get(0)?.bhkType?.name.isNullOrEmpty()) {
                            project_property_type.value =
                                projectValues.projectTowers?.get(0)?.unitPlans?.get(0)
                                    ?.propertyType?.name
                            project_unit_bhk.value =
                                projectValues.projectTowers?.get(0)?.unitPlans?.get(0)
                                    ?.bhkType?.name
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    try {
                        val reraValue = projectValues.projectReraNumber
                        if (reraValue != null) {
                            project_reraLayout.value = View.VISIBLE
                            project_reraRegisteredTxtView.value = View.VISIBLE
                            project_rera_no.value = projectValues.projectReraNumber
                            project_info.value = projectValues.projectInfo
                        } else {
                            project_reraRegisteredTxtView.value = View.GONE
                            project_reraLayout.value = View.GONE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun onClick(view: View) {
        if (project_image_no.value != "+0") {
            val bundle = bundleOf("projectId" to previousProjectIds)
            navcontroller!!.navigate(
                R.id.action_projectDetailFragment_to_projectImagesFragment,
                bundle
            )
        }
    }

    fun callRm(view: View) {
        Coroutines.main {
            if (rmPhoneNumber != null) {

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel: ${rmPhoneNumber}")
                view.context.startActivity(intent)
            }
        }

    }

    fun goToOffersFragment(view: View) {
        navcontroller!!.navigate(
            R.id.projectOffersFragment)

    }

    fun goToInventoryFragment(view: View) {

        navcontroller!!.navigate(
            R.id.projectInventoryFragment)
    }

    fun filterProjectApplyButton(view: View) {


        var response: Response<Projectsdata>?

        progressBarFilter.value = View.VISIBLE
        clearButtonEnabled.value = false
        applyButtonEnabled.value = false

        Coroutines.io {


//                    if (selectedProjectsProprtyList.value != null) {
//                        projectProperty = selectedProjectsProprtyList.value.toString()
//                            .replace("[", "")
//                            .replace("]", "")
//                    }

            if (selectedProjectsLocationId.value != null) {
                projectLocation = selectedProjectsLocationId.value.toString()
                    .replace("[", "")
                    .replace(",", "__")
                    .replace("]", "")
            }

            if (selectedProjectsUnitPlanId.value != null) {
                projectUnitPlan = selectedProjectsUnitPlanId.value.toString()
                    .replace("[", "")
                    .replace("BHK", "")
                    .replace(",", "__")
                    .replace("]", "")
            }


            if (selectedProjectsStatusId.value != null) {
                projectStatus = selectedProjectsStatusId.value.toString()
                    .replace("[", "")
                    .replace(",", "__")
                    .replace("]", "")
            }

            if (selectedProjectsAmenitiesId.value != null) {
                projectAmenities = selectedProjectsAmenitiesId.value.toString()
                    .replace("[", "")
                    .replace(",", "__")
                    .replace("]", "")
            }


            if (filterMinPriceTxt.value != null && filterMaxPriceTxt.value != "") {
                projectPrice = filterMinPriceTxt.value + "__" + filterMaxPriceTxt.value
            }
            if (filterMinPerSqFtPriceTxt.value != null && filterMaxPerSqFtPriceTxt.value != "") {
                projectPerSqFtPrice =
                    filterMinPerSqFtPriceTxt.value + "__" + filterMaxPerSqFtPriceTxt.value
            }

            if (builtUp_min_edittext.value != null && builtUp_max_edittext.value != "") {
                projectBuiltUp = builtUp_min_edittext.value + "__" + builtUp_max_edittext.value
            }

            val filter = HashMap<String, String>()

            if (!projectLocation.isNullOrEmpty() || !projectStatus.isNullOrEmpty() || !projectPerSqFtPrice.isNullOrEmpty() ||
                !projectUnitPlan.isNullOrEmpty() || !projectBuiltUp.isNullOrEmpty() || !projectAmenities.isNullOrEmpty() ||
                !projectPrice.isNullOrEmpty()
            ) {

                if (!projectLocation.isNullOrEmpty()) {
                    filter.put("location_name__in", projectLocation!!)
                }
                if (!projectStatus.isNullOrEmpty()) {
                    filter.put("status__in", projectStatus!!)
                }
                if (!projectPrice.isNullOrEmpty()) {
                    filter.put("low_cost__range", projectPrice!!)
                }
                if (!projectPerSqFtPrice.isNullOrEmpty()) {
                    filter.put("project_bsp_cost__range", projectPerSqFtPrice!!)
                }
                if (!projectUnitPlan.isNullOrEmpty()) {
                    filter.put("rooms__in", projectUnitPlan!!)
                }
                if (!projectBuiltUp.isNullOrEmpty()) {
                    filter.put("unit_built_up_area__range", projectBuiltUp!!)
                }
                if (!projectAmenities.isNullOrEmpty()) {
                    filter.put("amenities__in", projectAmenities!!)
                }


                response = repository.getFilterProjects(filter)
                Coroutines.main {
                    if (response!!.isSuccessful) {
                        if (response?.body()?.count != 0) {
                            progressBarFilter.value = View.GONE
                            filteredProjectsData.value = response!!.body()
                            showFilteredProjects.value = true
                            setValueInSharedPref(view, showFilteredProjects.value!!.toString())
                            view.findNavController()
                            view.findNavController().navigateUp()
//                                .navigate(R.id.action_projectFilterFragment_to_navigation_projects)

//                                view.findNavController().navigateUp()
                            clearButtonEnabled.value = true
                            applyButtonEnabled.value = true

                        } else {
                            progressBarFilter.value = View.GONE
                            clearButtonEnabled.value = true
                            applyButtonEnabled.value = true

                            Snackbar.make(
                                view,
                                "No Projects found based on your filters.",
                                Snackbar.LENGTH_SHORT
                            ).show()
//                            view.findNavController()
//                                .navigate(R.id.action_projectFilterFragment_to_navigation_projects)


                        }
                    } else {
                        progressBarFilter.value = View.GONE
                        clearButtonEnabled.value = true
                        applyButtonEnabled.value = true
                        Snackbar.make(
                            view,
                            "Something went wrong.",
                            Snackbar.LENGTH_SHORT
                        ).show()
//                            view.findNavController().navigate(R.id.action_projectFilterFragment_to_navigation_projects)
                    }
                }

            } else
                Coroutines.main {
                    progressBarFilter.value = View.GONE
                    clearButtonEnabled.value = true
                    applyButtonEnabled.value = true
                    Snackbar.make(
                        view,
                        "You are not selected any filters.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
        }


    }

    fun clearData(view: View) {

         filterMinPriceTxt.value = ""
         filterMaxPriceTxt.value = ""
         filterMinPerSqFtPriceTxt.value = ""
         filterMaxPerSqFtPriceTxt.value = ""
         builtUp_min_edittext.value = ""
         builtUp_max_edittext.value = ""


         selectedProjectsProprtyList.value?.clear()
         selectedProjectsLocationId.value?.clear()
         selectedProjectsUnitPlanId.value?.clear()
         selectedProjectsStatusId.value?.clear()
         selectedProjectsAmenitiesId.value?.clear()
        clearButtonClicked.value = true
//        view.findNavController().navigateUp()
    }


    fun setValueInSharedPref(view: View, value: String) {
        val sharedPref: SharedPreferences =
            view.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("applyButtonEnabled", value)
        editor.apply()

    }

}