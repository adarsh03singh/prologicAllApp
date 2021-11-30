package com.csestateconnect.viewmodel

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.csestateconnect.R
import com.csestateconnect.db.data.GetCompletionStatus
import com.csestateconnect.db.data.GetLeadStatus
import com.csestateconnect.db.data.GetPreferredProperty
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.leads.CreateLeadActivity
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.repo.AuthRepository
import com.csestateconnect.repo.HomeRepository
import com.csestateconnect.utils.Coroutines
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.cs_assign_leads_dialog_item.view.*
import kotlinx.android.synthetic.main.lead_create_activity_dialog_item.view.*
import kotlinx.android.synthetic.main.lead_feedback_quality_item.view.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class LeadsViewModel(application: Application) : AndroidViewModel(application) {

//    private val context = getApplication<Application>().applicationContext

    private var repository = HomeRepository(application)
    val authRepository = AuthRepository(application)

    // Feedback Dialog Box
    var lead_feedback_text = MutableLiveData<String>()
    var lead_feedback_star: Float = 0.0F
    var lead_feedback_starBind = MutableLiveData<Float>()
    var lead_feedback_nullcheck = MutableLiveData<Boolean>()

    lateinit var leadActivityDate: String
    lateinit var leadActivityTime: String

    var lead_status_visibility = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var layoutabove_leadstatus_visibility = MutableLiveData<Int>().apply { postValue(View.VISIBLE) }

    init {
        repository = HomeRepository(application)
    }

    var detailLeadsIds: Int = 0

    // Lead Details
    var leads_name = MutableLiveData<String>()
    var leads_status = MutableLiveData<String>()
    var leadsStatusColor = MutableLiveData<String>()
    var leads_budget = MutableLiveData<String>()
    var leads_id = MutableLiveData<Int>()
    var leads_location = MutableLiveData<String>()
    var preferred_property_type = MutableLiveData<String>()
    var leads_construction_status = MutableLiveData<String>()
    var leads_address = MutableLiveData<String>()
    var leads_email = MutableLiveData<String>()
    var leads_mobile = MutableLiveData<String>()
    var leads_created_date = MutableLiveData<String>()
    var leads_modified_date = MutableLiveData<String>()
    var feedback_clickable = MutableLiveData<Boolean>().apply { postValue(true) }
    var leads_feedback_star = MutableLiveData<Int>()
    var createDealButtonVisibility = MutableLiveData<Int>().apply { postValue(View.VISIBLE) }

    var leadActivityNotNull = MutableLiveData<List<CreateLeadActivity>?>()

    // Create Lead
    var createLeadName = MutableLiveData<String>()
    var createLeadNumber = MutableLiveData<String>()
    var createLeadEmail = MutableLiveData<String>()
    var createLeadCountry = MutableLiveData<String>()
    var countryId: Int? = null
    var createLeadCity = MutableLiveData<String>()
    var cityId: Int? = null
    var createLeadNumberCode = MutableLiveData<String>()
    var createLeadMinimum = MutableLiveData<String>()
    var budgetMinimumCurrency: String? = null
    val minBudget = StringBuilder()
    var createLeadMaximum = MutableLiveData<String>()
    var budgetMaximumCurrency: String? = null
    val maxBudget = StringBuilder()
    var createLeadBudgetCurrency = MutableLiveData<String>()
    var createLeadPreferredLocation = MutableLiveData<List<Int>>()
    var createLeadPropertyType = MutableLiveData<List<Int>>()
    var createLeadPreferredStatus = MutableLiveData<List<Int>>()
    var createLeadStatus = MutableLiveData<Int>()

    var createLeadProgressBar = MutableLiveData<Boolean>().apply { postValue(false) }


    // Filter Lead
    var filterLeadCreatedStart = MutableLiveData<String>()
    var filterLeadCreatedEnd = MutableLiveData<String>()
    var filterLeadModifiedStart = MutableLiveData<String>()
    var filterLeadModifiedEnd = MutableLiveData<String>()

    var selectedleadsId = MutableLiveData<List<Int>>()
    var clearButtonClicked = MutableLiveData<Boolean>().apply { postValue(false) }
    var progressBarFilter = MutableLiveData<Int>().apply { postValue(View.GONE) }

    var clearButtonEnabled = MutableLiveData<Boolean>().apply { postValue(true) }
    var applyButtonEnabled = MutableLiveData<Boolean>().apply { postValue(true) }

    val filteredLeadData = MutableLiveData<ListOfLeads?>()

    var leadType = MutableLiveData<Int>()
    var showFilteredLeads = MutableLiveData<Boolean>().apply { postValue(false) }
    var showMyFilteredLeads = MutableLiveData<Boolean>().apply { postValue(false) }

    var filterLeadAssigned = MutableLiveData<Boolean>().apply { postValue(false) }
    var filterLeadSubmitted = MutableLiveData<Boolean>().apply { postValue(false) }

    val listLead = mutableListOf<Int>()

    fun createLeadSubmitButton(view: View) {
        if (createLeadName.value.isNullOrEmpty() || createLeadNumber.value.isNullOrEmpty()
            || createLeadStatus.value.toString() == "null"
//            || createLeadStatus.value?.isEmpty()
        ) {
            Toast.makeText(
                this.getApplication(),
                "Please fill all mandatory details",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val phoneNumber = createLeadNumberCode.value + createLeadNumber.value
            if (createLeadNumber.value!!.length != 10) {
                Toast.makeText(
                    this.getApplication(),
                    "Please Enter Correct Number",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (createLeadEmail.value.isNullOrEmpty()) {
                createLeadSubmitButtonApi(phoneNumber, view)
            } else if (!isEmailValid(createLeadEmail.value)) {
                Toast.makeText(
                    this.getApplication(),
                    "Please Enter Correct Email",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                createLeadSubmitButtonApi(phoneNumber, view)
            }
        }
    }

    fun createLeadSubmitButtonApi(phoneNumber: String, view: View) {


        minBudget.append(createLeadMinimum.value).append(" ").append(budgetMinimumCurrency)
        maxBudget.append(createLeadMaximum.value).append(" ").append(budgetMaximumCurrency)

        createLeadProgressBar.value = true

        var response: Response<Void>? = null
        try {
            Coroutines.io {
                response = repository.getCreateLead(
                    createLeadName.value!!,
                    phoneNumber,
                    createLeadEmail.value,
                    countryId,
                    cityId,
                    minBudget.toString(),
                    maxBudget.toString(),
                    createLeadBudgetCurrency.value,
                    createLeadPreferredLocation.value,
                    createLeadPropertyType.value,
                    createLeadPreferredStatus.value,
                    createLeadStatus.value!!
                )

                Coroutines.main {
                    if (response!!.isSuccessful) {
                        Snackbar.make(view, "Lead Created", Snackbar.LENGTH_SHORT).show()
                        view.findNavController().navigateUp()
                    } else {
                        val jObjError = JSONObject(response!!.errorBody()!!.string())
                        val error = jObjError.getString("error")
                        Toast.makeText(this.getApplication(), error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            // TODO Not working and crashing here if api it hit without internet
            Snackbar.make(view, "Unknown error occurred", Snackbar.LENGTH_SHORT).show()
        }

        createLeadProgressBar.value = false
    }

    fun filterLeadsApplyButton(view: View) {
        if (filterLeadCreatedStart.value == null && filterLeadCreatedEnd.value == null ||
            filterLeadCreatedStart.value != null && filterLeadCreatedEnd.value != null
        ) {

            if (filterLeadModifiedStart.value == null && filterLeadModifiedEnd.value == null ||
                filterLeadModifiedStart.value != null && filterLeadModifiedEnd.value != null
            ) {

                var createdDate: String? = null
                if (filterLeadCreatedStart.value != null && filterLeadCreatedStart.value != "") {
                    createdDate =
                        filterLeadCreatedStart.value + " 00:00:00" + "," + filterLeadCreatedEnd.value + " 00:00:00"
                }
                var modifiedDate: String? = null
                if (filterLeadModifiedStart.value != null && filterLeadModifiedStart.value != "") {
                    modifiedDate =
                        filterLeadModifiedStart.value + " 00:00:00" + "," + filterLeadModifiedEnd.value + " 00:00:00"
                }

                var response: Response<ListOfLeads>?

                // TODO Progressbar not working
                progressBarFilter.value = View.VISIBLE
                clearButtonEnabled.value = false
                applyButtonEnabled.value = false

                Coroutines.io {
                    var leadStatus: String? = null
                    if (selectedleadsId.value != null) {
                        leadStatus = selectedleadsId.value.toString()
                            .replace("[", "")
                            .replace("]", "")
                    }

                    if (leadStatus != "") {
                        response = repository.getFilterLeads(
                            createdDate, leadStatus, modifiedDate,
                            filterLeadAssigned.value, filterLeadSubmitted.value
                        )
                        Coroutines.main {
                            if (response!!.isSuccessful) {

                                if (response?.body()?.count != 0) {
                                    progressBarFilter.value = View.GONE
                                    filteredLeadData.value = response!!.body()

                                    if (leadType.value == 1) {
                                        showFilteredLeads.value = true
                                        showMyFilteredLeads.value = false
                                    } else {
                                        showMyFilteredLeads.value = true
                                        showFilteredLeads.value = false
                                    }
                                    filterLeadAssigned.value = false
                                    filterLeadSubmitted.value = false

                                    view.findNavController().navigateUp()
                                    clearButtonEnabled.value = true
                                    applyButtonEnabled.value = true

                                } else {
                                    progressBarFilter.value = View.GONE
                                    clearButtonEnabled.value = true
                                    applyButtonEnabled.value = true

                                    Snackbar.make(
                                        view,
                                        "No leads found based on your filters.",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Coroutines.main {
                            showFilteredLeads.value = false
                            showMyFilteredLeads.value = false
                            listLead.clear()
                            view.findNavController().navigateUp()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    this.getApplication(),
                    "Please fill both start date and end date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this.getApplication(),
                "Please fill both start date and end date",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun filterLeadAssignedClick(view: View) {
        if (filterLeadAssigned.value == true) {
            filterLeadAssigned.value = false
        } else {
            filterLeadAssigned.value = true
        }
    }

    fun filterLeadSubmittedClick(view: View) {
        if (filterLeadSubmitted.value == true) {
            filterLeadSubmitted.value = false
        } else {
            filterLeadSubmitted.value = true
        }
    }

//    fun filterLeadsClearButton(view: View) {
//        filterLeadCreatedStart.value = ""
//        filterLeadCreatedEnd.value = ""
//        filterLeadModifiedStart.value = ""
//        filterLeadModifiedEnd.value = ""
//        filterLeadAssigned.value = false
//        filterLeadSubmitted.value = false
//
//        selectedleadsId.value = null
//        clearStatusNull.value = true
//    }

    fun getLeadsDetailByPreviousId(leadsData: List<ListOfLeads>) {

        val users: List<ListOfLeads> = leadsData
        try {
            for (i in 0..users.get(0).results.size - 1) {

                val leadValues = users.get(0).results.get(i)
                if (leadValues.id == detailLeadsIds) {

                    if (leadValues.get_deal != null) {
                        createDealButtonVisibility.value = View.GONE
                    }

                    leadsStatusColor.value = leadValues.lead_status.color

                    leads_name.value = leadValues.name
                    leads_status.value = leadValues.lead_status.name
                    if (leadValues.min_budget_view != null || leadValues.max_budget_view != null) {
                        leads_budget.value =
                            leadValues.min_budget_view + " - " + leadValues.max_budget_view
                    }
                    else {
                        leads_budget.value = "N/A"
                    }
                    leads_id.value = leadValues.id

                    leads_location.value = leadValues.location?.name ?: "N/A"

                    if (leadValues.preferred_property_type?.size != 0) {
                        val list = leadValues.preferred_property_type!!.map { it!!.name }

                        preferred_property_type.value = list.joinToString()
                    }
                    else {
                        preferred_property_type.value = "N/A"
                    }
                    if (leadValues.preferred_status?.size != 0) {
                        val list = leadValues.preferred_status!!.map { it!!.name }
                        leads_construction_status.value = list.joinToString()
                    }
                    else {
                        leads_construction_status.value = "N/A"
                    }

                    if (leadValues.location?.name != null) {
                        leads_address.value =
                            leadValues.location.name + " " + leadValues.city?.name
                    } else if (leadValues.city?.name != null) {
                        leads_address.value = leadValues.city?.name
                    }
                    else {
                        leads_address.value = "N/A"
                    }

                    leads_email.value = leadValues.email ?: "N/A"

                    leads_mobile.value = leadValues.phone_number
                    leads_created_date.value = dateConverter(leadValues.created_at)
                    leads_modified_date.value = dateConverter(leadValues.updated_at)

                    if (leadValues.feedback_submitted) {
                        feedback_clickable.value = false
                        leads_feedback_star.value = (leadValues.quality)?.toInt()
                    }

                    if (!leadValues.get_activities.isNullOrEmpty()){
                        leadActivityNotNull.value = leadValues.get_activities
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun postLeadRequestButton(view: View) {
        var showDialog = false
        runBlocking {
            val response = repository.postLeadRequest()

            if (response.isSuccessful) {
                showDialog = true
            } else {
                val jObjError = JSONObject(response.errorBody()!!.string())
                val error = jObjError.getString("error")
                if (error.contains("pending")) {
                    Coroutines.main {
                        Toast.makeText(view.context, error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        if (showDialog) {
            val mDialogView =
                LayoutInflater.from(view.context)
                    .inflate(R.layout.cs_assign_leads_dialog_item, null)

            val mBuilder = AlertDialog.Builder(view.context!!)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.close_dialog_button.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
        }
    }

    fun getAllLeadsData(): LiveData<List<ListOfLeads>> {
        return repository.getLeadsFromDatabase()
    }

    fun getLeadStatusData(): LiveData<List<GetLeadStatus>> {

        return repository.getLeadStatusFromDatabase()
    }

    fun getCompletionStatusData(): LiveData<List<GetCompletionStatus>> {

        return repository.getCompletionStatusFromDatabase()
    }

    fun getPreferredPropertyData(): LiveData<List<GetPreferredProperty>> {

        return repository.getPreferredPropertyFromDatabase()
    }

    fun getCountriesData(): LiveData<List<Countries>> {
        return repository.getCountriesFromDatabase()
    }

    fun getLeadActivityData(): LiveData<List<CreateLeadActivity>?> {
        return repository.getLeadActivityData()
    }

    fun getCountries() {
        Coroutines.io {
            authRepository.getCountries()
        }
    }

    fun getListOfLeads() {
        Coroutines.io {
            val response = repository.getlistofleads()
        }
    }

    fun createLeadWhenEmpty(view: View) {
        view.findNavController().navigate(R.id.action_lead2TabFragment_to_createLeadFragment)
    }

    fun getLeadStatus_Api() {
        Coroutines.io {
            try {
                val response = repository.getLeadStatus()
                if (response.isSuccessful) {
                    // Do Something
                } else {
                    Toast.makeText(
                        this.getApplication(),
                        "Error getting lead status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCompletionStatus_Api() {
        Coroutines.io {
            try {
                val response = repository.getCompletionStatus()
                if (response.isSuccessful) {
                    // Do Something
                } else {
                    Toast.makeText(
                        this.getApplication(),
                        "Error getting completion status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPreferredproperty_Api() {
        Coroutines.io {
            try {
                val response = repository.getPreferredProperty()
                if (response.isSuccessful) {
                    // Do Something
                } else {
                    Toast.makeText(
                        this.getApplication(),
                        "Error getting preferred_property status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun lead_status_items(view: View) {
        if (lead_status_visibility.value == View.GONE) {
            layoutabove_leadstatus_visibility.value = View.GONE
            lead_status_visibility.value = View.VISIBLE
        } else {
            layoutabove_leadstatus_visibility.value = View.VISIBLE
            lead_status_visibility.value = View.GONE
        }

        getLeadStatusData()
    }


    fun leave_feedback_dialog(view: View) {
        val mDialogView =
            LayoutInflater.from(view.context).inflate(R.layout.lead_feedback_quality_item, null)

        val mBuilder = AlertDialog.Builder(view.context).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.feedback_ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            mDialogView.feedback_submit_button.setBackgroundColor(view.resources.getColor(R.color.buttonColor))
            mDialogView.feedback_submit_button.isEnabled = true
        }

        mDialogView.feedback_submit_button.setOnClickListener {
            var response: Response<Result>? = null
            Coroutines.io {
                response = repository.updateLeadFeedback(
                    leads_id.value!!,
                    mDialogView.feedback_text.text.toString(),
                    mDialogView.feedback_ratingBar.rating
                )
                try {
                    mAlertDialog.dismiss()
                    if (response!!.isSuccessful) {
                        Coroutines.main {
                            leads_feedback_star.value =
                                (mDialogView.feedback_ratingBar.rating).toInt()
                            Toast.makeText(view.context, "Feedback Submitted", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Coroutines.main {
                            Toast.makeText(view.context, "Unknown error", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

        mDialogView.feedback_close_dialog_button.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun lead_activity_create_dialog(view: View) {
        val mDialogView =
            LayoutInflater.from(view.context)
                .inflate(R.layout.lead_create_activity_dialog_item, null)

        val mBuilder = AlertDialog.Builder(view.context).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.activity_date.setOnClickListener {
            datePicker(mDialogView.activity_date, view.context)
        }

        mDialogView.activity_time.setOnClickListener {
            timePicker(mDialogView.activity_time, view.context)
        }

        mDialogView.lead_activity_close_dialog_button.setOnClickListener {
            mAlertDialog.dismiss()
        }

        mDialogView.activity_submit_button.setOnClickListener {
            if (mDialogView.short_description.text.isNullOrEmpty() || mDialogView.activity_date.text.isNullOrEmpty()
                || mDialogView.activity_time.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this.getApplication(),
                    "Please fill all the details",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                var response: Response<CreateLeadActivity>? = null
                Coroutines.io {
                    response = repository.updateLeadActivity(
                        leads_id.value!!,
                        mDialogView.short_description.text.toString(),
                        "$leadActivityDate $leadActivityTime"
                    )
                    try {
                        mAlertDialog.dismiss()
                        if (response!!.isSuccessful) {
                            Coroutines.main {
                                leadActivityNotNull.value = listOf(response!!.body()!!)
                                Toast.makeText(view.context, "Lead Activity Created", Toast.LENGTH_LONG)
                                    .show()
                            }
                        } else {
                            Coroutines.main {
                                Toast.makeText(view.context, "Unknown error", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    fun updateLead(id: Int, leadStatusId: Int) {
        Coroutines.io {
            val response = repository.updateLead(id, leadStatusId)

//            TODO Latest
            if (response.isSuccessful) {
//                Toast.makeText(this.getApplication(), "Lead Updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun dateConverter(convertToDate: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = SimpleDateFormat("dd/MM/yyyy")
        var date: Date? = null
        var finalDate: String = ""

        try {
            date = inputFormatter.parse(convertToDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        finalDate = outputFormatter.format(date)

        return finalDate
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun goToCreateDealPage(view: View) {
        val bundle = bundleOf("leadIDforCreateDeal" to leads_id.value)
        view.findNavController()
            .navigate(R.id.action_leadDetailsFragment_to_createDealFragment, bundle)
    }


    fun leadDetailEmailAction(view: View){
        val email = leads_email.value
        if (email != null && email != "N/A") {
            val mIntent = Intent(Intent.ACTION_SENDTO)
            mIntent.data = Uri.parse("mailto:")
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Lead #${leads_id.value}")

            try {
                ContextCompat.startActivity(
                    view.context,
                    Intent.createChooser(mIntent, "Choose Email Client..."),
                    null
                )
            } catch (e: Exception) {
                Toast.makeText(this.getApplication(), e.message, Toast.LENGTH_LONG).show()
            }
        }
        else {
            Toast.makeText(this.getApplication(), "Email not available", Toast.LENGTH_LONG).show()
        }
    }

    fun leadDetailPhoneAction(view: View){
        val mobile = leads_mobile.value
        if (mobile != null && mobile != "N/A") {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: ${mobile}}")
            ContextCompat.startActivity(view.context, intent, null)
        }
        else {
            Toast.makeText(this.getApplication(), "Mobile Number not available", Toast.LENGTH_LONG).show()
        }
    }


    private fun datePicker(dateText: EditText, context: Context) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val mONTHS = listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )

        val datePickerDialog =
            DatePickerDialog(context, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker,
                    year: Int,
                    monthOfYear: Int,
                    dayOfMonth: Int
                ) {
//                  dateText.setText("$dayOfMonth ${monthOfYear+1}, $year")
                    dateText.setText("$dayOfMonth ${mONTHS[monthOfYear]}, $year")
                    leadActivityDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun timePicker(timeText: EditText, context: Context) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

            val second = 2
            var hr = hour
            var min: String = minute.toString()

            if (minute in 0..9) {
                min = "0$min"
            }
            if (hr in 0..11) {
                cal.set(Calendar.HOUR_OF_DAY, hr)
                cal.set(Calendar.MINUTE, minute)
                timeText.setText("$hr:$min AM")
            } else {
                hr -= 12;
                if (hr == 0) {
                    hr = 12
                }
                cal.set(Calendar.HOUR_OF_DAY, hr)
                cal.set(Calendar.MINUTE, minute)
                timeText.setText("$hr:$min PM")
            }
//            timeText.setText(SimpleDateFormat("HH:mm a").format(cal.time))
            leadActivityTime = "$hour:$min"
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }
}
