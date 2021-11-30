package com.csestateconnect.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.CreateDealSelectProjectAdapter
import com.csestateconnect.db.data.DealDetailChequeImageEntity
import com.csestateconnect.db.data.GetDealStatus
import com.csestateconnect.db.data.deals.ListOfDeals
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.repo.DealsRepository
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.utils.DateConverter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dealdetail_doc_submit_dialog.view.*
import kotlinx.android.synthetic.main.project_name_dialog.view.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import retrofit2.Response
import java.io.File


class DealsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DealsRepository(application)

    var deal_status_visibility = MutableLiveData<Int>().apply { postValue(View.GONE) }
    var layoutabove_dealstatus_visibility = MutableLiveData<Int>().apply { postValue(View.VISIBLE) }
    var detailDealsId = 0
    var dealChequeId = 0

    var users: ListOfDeals? = null
    var projectsData: List<Projectsdata>? = null

    // Create Deal
    var createDealLeadId: Int = 0
    var createDealDealStatusId = MutableLiveData<Int>()
    var createDealProject = MutableLiveData<String>().apply { postValue("*Select Project") }
    var createDealTotalCommission = MutableLiveData<String>()
    var totalCommissionCurrency: String? = null
    var createDealPayableCommission = MutableLiveData<String>()
    var payableCommissionCurrency: String? = null
    var createDealPaidCommission = MutableLiveData<String>()
    var paidCommissionCurrency: String? = null
    var createDealUnpaidCommission = MutableLiveData<String>()
    var unpaidCommissionCurrency: String? = null
    var createDealSoldArea = MutableLiveData<String>()
    var createDealCommissionRate = MutableLiveData<String>()
    var createDealCommissionPercent = MutableLiveData<String>()

//    var kycUpdatedBool = MutableLiveData<Boolean>().apply { postValue(false) }
//    var bankaccountUpdatedBool = MutableLiveData<Boolean>().apply { postValue(false) }

    // Deal Detail
    var dealsStatus = MutableLiveData<String>()
    var dealsStatusColor = MutableLiveData<String>()
    var dealsProjectName = MutableLiveData<String>()
    var deals_id = MutableLiveData<String>()
    var deals_location = MutableLiveData<String>()
    var deals_created_date = MutableLiveData<String>()
    var deals_modified_date = MutableLiveData<String>()
    var dealsTotalAmount = MutableLiveData<String>()
    var dealsPayableAmount = MutableLiveData<String>()

    var dealsTotalAmountPaid = MutableLiveData<String>()
    var dealsPayableAmountUnpaid = MutableLiveData<String>()
    var dealsDaysLeftForPayment = MutableLiveData<String>()
    var dealsSoldArea = MutableLiveData<String>()
    var dealsCommissionRate = MutableLiveData<String>()
    var dealsCommission = MutableLiveData<String>()

    var dealsLeadName = MutableLiveData<String>()
    var dealsLeadAddress = MutableLiveData<String>()
    var dealsLeadEmail = MutableLiveData<String>()
    var dealsLeadMobile = MutableLiveData<String>()

    var dealsDocVerifiedOn = MutableLiveData<String>()
    var dealsDocVerifiedStatus = MutableLiveData<String>()
    var dealsDocChequeImage = MutableLiveData<String>()

    val cheque_image = MutableLiveData<String>()
    val chequeImageSuccessful = MutableLiveData<Boolean>().apply { postValue(false) }

    var dealDetailChequeLayoutVisibility = MutableLiveData<Int>().apply { postValue(View.GONE) }

    var dealDetailUploadButtonVisibility = MutableLiveData<Int>().apply { postValue(View.VISIBLE) }

    // Deal Filter
    var filterDealCreatedStart = MutableLiveData<String>()
    var filterDealCreatedEnd = MutableLiveData<String>()
    var filterDealModifiedStart = MutableLiveData<String>()
    var filterDealModifiedEnd = MutableLiveData<String>()

    var selecteddealsId = MutableLiveData<MutableList<Int>>()
    var progressBarFilter = MutableLiveData<Int>().apply { postValue(View.GONE) }

    var clearButtonEnabled = MutableLiveData<Boolean>().apply { postValue(true) }
    var applyButtonEnabled = MutableLiveData<Boolean>().apply { postValue(true) }

    var clearButtonClicked = MutableLiveData<Boolean>().apply { postValue(false) }

    val filteredDealData = MutableLiveData<ListOfDeals?>()

    var showFilteredDeals = MutableLiveData<Boolean>().apply { postValue(false) }

    val listDeal = mutableListOf<Int>()

    fun createDealButton(view: View) {

        if (createDealDealStatusId.value == null || createDealProject.value == "Select Project" ||
            createDealTotalCommission.value.isNullOrEmpty() || createDealPayableCommission.value.isNullOrEmpty() ||
            createDealPaidCommission.value.isNullOrEmpty() || createDealUnpaidCommission.value.isNullOrEmpty() ||
            createDealSoldArea.value.isNullOrEmpty() || createDealCommissionRate.value.isNullOrEmpty() ||
            createDealCommissionPercent.value.isNullOrEmpty()
        ) {
            Toast.makeText(
                this.getApplication(),
                "Please fill all mandatory details",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            val projectId =
                projectsData?.get(0)?.results?.filter { it?.name == createDealProject.value }
                    ?.get(0)?.id

            Coroutines.io {
                val response = repository.getCreateDeal(
                    createDealLeadId,
                    createDealDealStatusId.value!!,
                    projectId!!,
                    createDealTotalCommission.value!! + " " + totalCommissionCurrency,
                    createDealPayableCommission.value!! + " " + payableCommissionCurrency,
                    createDealPaidCommission.value!! + " " + paidCommissionCurrency,
                    createDealUnpaidCommission.value!! + " " + unpaidCommissionCurrency,
                    createDealSoldArea.value!!.toFloat(),
                    createDealCommissionRate.value!!.toFloat(),
                    createDealCommissionPercent.value!!.toFloat()
                )

                if (response.isSuccessful) {
                    Snackbar.make(view, "Deal Created", Snackbar.LENGTH_LONG).show()

                    val jObj = JSONObject(response.body()!!.string())
                    val dealId = jObj.getString("id")

                    val bundle = bundleOf("DealsId" to dealId)

                    Coroutines.main {
                        view.findNavController()
                            .navigate(
                                R.id.action_createDealFragment_to_dealDetailsFragment,
                                bundle
                            )
                    }
                }
            }
        }
    }

    fun createDealSelectProject(view: View) {

//        val resultId = projectsData?.get(0)?.results?.map { it?.id }
        val resultName = projectsData?.get(0)?.results?.map { it?.name }

        val mDialogView =
            LayoutInflater.from(view.context)
                .inflate(R.layout.project_name_dialog, null)

        val mBuilder = AlertDialog.Builder(view.context).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        if (projectsData?.get(0)?.results?.size == 0) {
            mDialogView.zero_assigned_text.visibility = View.VISIBLE
        } else {

            val selectProjectAdapter = CreateDealSelectProjectAdapter(resultName) { it ->
                createDealProject.value = it
                mAlertDialog.dismiss()
            }
            mDialogView.projectNameRecycler.adapter = selectProjectAdapter
            mDialogView.projectNameRecycler.layoutManager = LinearLayoutManager(
                view.context,
                RecyclerView.VERTICAL, false
            )
            mDialogView.projectNameRecycler.setHasFixedSize(true)
        }
        mDialogView.btndialog.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun filterDealsApplyButton(view: View) {
        if (filterDealCreatedStart.value == null && filterDealCreatedEnd.value == null ||
            filterDealCreatedStart.value != null && filterDealCreatedEnd.value != null
        ) {

            if (filterDealModifiedStart.value == null && filterDealModifiedEnd.value == null ||
                filterDealModifiedStart.value != null && filterDealModifiedEnd.value != null
            ) {

                var createdDate: String? = null
                if (filterDealCreatedStart.value != null && filterDealCreatedStart.value != "") {
                    createdDate =
                        filterDealCreatedStart.value + " 00:00:00" + "," + filterDealCreatedEnd.value + " 00:00:00"
                }

                var modifiedDate: String? = null
                if (filterDealModifiedStart.value != null && filterDealModifiedStart.value != "") {
                    modifiedDate =
                        filterDealModifiedStart.value + " 00:00:00" + "," + filterDealModifiedEnd.value + " 00:00:00"
                }

                var response: Response<ListOfDeals>?

                // TODO Progressbar not working
                progressBarFilter.value = View.VISIBLE
                clearButtonEnabled.value = false
                applyButtonEnabled.value = false

                Coroutines.io {
                    var dealStatus: String? = null
                    if (selecteddealsId.value != null) {
                        dealStatus = selecteddealsId.value.toString()
                            .replace("[", "")
                            .replace("]", "")
                    }

                    if (dealStatus != "") {
                        response = repository.getFilterDeals(createdDate, dealStatus, modifiedDate)
                        Coroutines.main {
                            if (response!!.isSuccessful) {

                                if (response?.body()?.count != 0) {
                                    progressBarFilter.value = View.GONE
                                    showFilteredDeals.value = true
                                    filteredDealData.value = response!!.body()

//                        var bundle = bundleOf("responseFilterLead" to response!!.body())
//                        view.findNavController().navigate(R.id.confirmationAction, bundle)

                                    view.findNavController().navigateUp()
                                    clearButtonEnabled.value = true
                                    applyButtonEnabled.value = true

                                } else {
                                    progressBarFilter.value = View.GONE
                                    clearButtonEnabled.value = true
                                    applyButtonEnabled.value = true
                                    Toast.makeText(
                                        this.getApplication(),
                                        "No deals found based on your filters.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Coroutines.main {
                            showFilteredDeals.value = false
                            listDeal.clear()
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

    fun dealChequeImage(view: View) {
        var file: File? = null
        file = File(cheque_image.value!!)

        var response: retrofit2.Response<DealDetailChequeImageEntity>? = null
        Coroutines.main {
            try {
                if (deals_id.value != null) {

                    response =
                        repository.setDealChequeImage(Integer.parseInt(deals_id.value!!), file)
                    Log.i("verification_", response?.body().toString())

                    if (response!!.isSuccessful) {
                        Toast.makeText(
                            this.getApplication(), "Successfully Updated", Toast.LENGTH_SHORT
                        ).show()

                        val mDialogView =
                            LayoutInflater.from(view.context)
                                .inflate(R.layout.dealdetail_doc_submit_dialog, null)

                        val mBuilder = AlertDialog.Builder(view.context)
                            .setView(mDialogView)
                        val mAlertDialog = mBuilder.show()

                        mDialogView.deal_close_dialog_button.setOnClickListener {
                            //dismiss dialog
                            mAlertDialog.dismiss()
                            view.findNavController()
                                .navigate(R.id.action_dealUploadDocFragment_to_dealDetailsFragment)
                            dealDetailChequeLayoutVisibility.value = View.VISIBLE
                            dealChequeId = Integer.parseInt(deals_id.value!!)
                            dealDetailUploadButtonVisibility.value = View.GONE

                        }

                    }
                } else {
                    Toast.makeText(
                        this.getApplication(), "Id not found", Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun filterDealsClearButton(view: View) {
        filterDealCreatedStart.value = ""
        filterDealCreatedEnd.value = ""
        filterDealModifiedStart.value = ""
        filterDealModifiedEnd.value = ""

        selecteddealsId.value?.clear()
        clearButtonClicked.value = true
    }

    fun getListOfDeals() {
        Coroutines.io {
            repository.getlistofdeals()
        }
    }

    fun dealDetailsUpdateImage(view: View) {
        val bundle = bundleOf("updateDealKyc" to dealsDocChequeImage.value)
        view.findNavController()
            .navigate(R.id.action_dealDetailsFragment_to_dealUploadDocFragment, bundle)
    }

    fun getDealStatus_Api() {
        Coroutines.io {
            try {
                val response = repository.getDealStatus()
                if (response.isSuccessful) {
                    // Do Something
                } else {
                    Toast.makeText(
                        this.getApplication(),
                        "Error getting deal status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deal_status_items(view: View) {
        if (deal_status_visibility.value == View.GONE) {
            layoutabove_dealstatus_visibility.value = View.GONE
            deal_status_visibility.value = View.VISIBLE
        } else {
            layoutabove_dealstatus_visibility.value = View.VISIBLE
            deal_status_visibility.value = View.GONE
        }

        getDealStatusData()
    }

    fun updateDeal(id: Int, dealStatusId: Int) {
        Coroutines.io {
            val response = repository.updateDeal(id, dealStatusId)
            if (response.isSuccessful) {
                Toast.makeText(this.getApplication(), "Deal Updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getAllDealsData(): LiveData<List<ListOfDeals>> {
        return repository.getDealsFromDatabase()
    }

    fun getDealStatusData(): LiveData<List<GetDealStatus>> {
        return repository.getDealStatusFromDatabase()
    }

    fun getDealDetailByPreviousId(dealData: List<ListOfDeals>) {

        if (dealData.isNotEmpty()) {
            val users: ListOfDeals = dealData.get(0)
            try {

                val dealDataId = users!!.results.map { it.id }

                val i = dealDataId.indexOf(detailDealsId)

                if (i == -1) {
                    runBlocking {
                        val response = repository.getlistofdeals()
                        if (response!!.isSuccessful) {
                            getAllDealsData()
                        }
                    }

                }

                val dealValues = users!!.results.get(i)

                dealsStatusColor.value = dealValues.deal_status.color

                dealsStatus.value = dealValues.deal_status.name
                dealsProjectName.value = dealValues.project.name
                deals_id.value = "#" + dealValues.id.toString()

                if (dealValues.lead.location?.name != null){
                    deals_location.value =
                        dealValues.lead.location?.name + ", " + dealValues.lead.city?.name
                } else if (dealValues.lead.city?.name != null) {
                    deals_location.value = dealValues.lead.city?.name
                } else {
                    deals_location.value = "N/A"
                }

                deals_created_date.value =
                    DateConverter().dateConverter(dealValues.created_at)
                deals_modified_date.value =
                    DateConverter().dateConverter(dealValues.updated_at)
                dealsTotalAmount.value = dealValues.commission_amount_total_view
                dealsPayableAmount.value = dealValues.commission_amount_payable_view ?: "N/A"


                dealsTotalAmountPaid.value = dealValues.commission_amount_paid_view
                dealsPayableAmountUnpaid.value =
                    dealValues.commission_amount_unpaid_view

                if (dealValues.days_left_for_payment == 9999) {
                    dealsDaysLeftForPayment.value = "N/A"
                } else {
                    dealsDaysLeftForPayment.value =
                        dealValues.days_left_for_payment.toString() + " days"
                }

                dealsSoldArea.value = dealValues.sold_area_sq_ft
                dealsCommissionRate.value = dealValues.commission_rate_sq_ft
                dealsCommission.value = dealValues.commission_percentage


                dealsLeadName.value = dealValues.lead.name

                if (dealValues.lead.location?.name != null && dealValues.lead.city?.name != null){
                    dealsLeadAddress.value =
                        dealValues.lead.location?.name + ", " + dealValues.lead.city?.name
                } else if (dealValues.lead.city?.name != null){
                    dealsLeadAddress.value = dealValues.lead.city?.name
                } else {
                    dealsLeadAddress.value = "N/A"
                }

                dealsLeadEmail.value = dealValues.lead.email ?: "N/A"
                dealsLeadMobile.value = dealValues.lead.phone_number

                if (dealValues.cheque_image != null) {
                    dealDetailChequeLayoutVisibility.value = View.VISIBLE
                    dealDetailUploadButtonVisibility.value = View.GONE
                    dealsDocVerifiedOn.value = dealValues.payment_approval_date ?: "N/A"
                    dealsDocVerifiedStatus.value =
                        dealValues.payment_approved.toString()
                    dealsDocChequeImage.value = dealValues.cheque_image
                } else {
                    dealDetailChequeLayoutVisibility.value = View.GONE
                    dealDetailUploadButtonVisibility.value = View.VISIBLE
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun getDealsDetailsChequeData(): LiveData<DealDetailChequeImageEntity> {
        return repository.getDealChequeImage()
    }

    fun loadChequeData(
        data: DealDetailChequeImageEntity
    ) {
        val idList = (users!!.results.map { it.id })
        val position = idList.indexOf(dealChequeId)

        dealsDocVerifiedOn.value = users!!.results.get(position).payment_approval_date
        dealsDocVerifiedStatus.value = users!!.results.get(position).payment_approved.toString()
        dealsDocChequeImage.value = data.cheque_image
    }

    fun dealDetailEmailAction(view: View){
        val email = dealsLeadEmail.value
        if (email != null && email != "N/A") {
            val mIntent = Intent(Intent.ACTION_SENDTO)
            mIntent.data = Uri.parse("mailto:")
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Deal ${deals_id.value}")

            try {
                startActivity(
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

    fun dealDetailPhoneAction(view: View){
        val mobile = dealsLeadMobile.value
        if (mobile != null && mobile != "N/A") {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: ${mobile}}")
            startActivity(view.context, intent, null)
        }
        else {
            Toast.makeText(this.getApplication(), "Mobile Number not available", Toast.LENGTH_LONG).show()
        }
    }

    fun getAllProjectData(): LiveData<List<Projectsdata>> {
        return repository.getProjectsFromDatabase()
    }

}
