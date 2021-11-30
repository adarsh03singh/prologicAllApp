package com.csestateconnect.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.csestateconnect.R
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.countries.Countries
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfClients.CreateClientData
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.db.data.reimbursements.CreateReimbursementData
import com.csestateconnect.db.data.reimbursements.GetReimbursementTypeEntity
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.GetReimburseDocListEntity
import com.csestateconnect.repo.*
import com.csestateconnect.ui.home.HomeActivity
import com.csestateconnect.utils.Coroutines
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class ReimbursementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReimbursementRepository
    private val authRepository: AuthRepository
    val reimburseDetails = MutableLiveData<com.csestateconnect.db.data.reimbursements.reimburseList.Result>()
    val clientRecentId = MutableLiveData<Int>()
    val reimburseEditId = MutableLiveData<Int>()

   var progressBar = MutableLiveData<Int>().apply{postValue(View.GONE)}
    var checkClientsRemove = MutableLiveData<Boolean>().apply{postValue(false)}
    var cancelablebutton = MutableLiveData<Boolean>().apply { postValue(true) }

    var cityIndexValue = MutableLiveData<Int>().apply { postValue(0) }
    var countryIndexValue  = MutableLiveData<Int>().apply { postValue(0) }
    var locationIndexValue = MutableLiveData<Int>().apply { postValue(0) }


    //for Edit client profile from server
    var editAmount = MutableLiveData<String>()
    var editType = MutableLiveData<String>()


    //for get client profile from server

    var amount = MutableLiveData<String>()
    var type = MutableLiveData<String>()
    var reimbursementTypeId = MutableLiveData<String>()
    var count: Int? = 0

    //CREATE/UPDATE CLIENT DOC
    var doc_Id = MutableLiveData<Int>()
    var document_name = MutableLiveData<String>()
    var reimburse_id_forDoc = MutableLiveData<String>()
    var doc_image_url = MutableLiveData<String>()



    val sharedPref: SharedPreferences = application.getSharedPreferences("tokenValue", 0)
    val token ="Token " + sharedPref.getString("tokenValue", "")

    init {
        var reimburseOldData: List<com.csestateconnect.db.data.reimbursements.reimburseList.Result?>? = null
        authRepository = AuthRepository(application)
        repository = ReimbursementRepository(application)
        Coroutines.io {

                        try {
                            reimburseOldData =  repository.getReimburseDataFromList()
             } catch (e: Exception) {
            e.printStackTrace()
        }
            Coroutines.main {
                try {
                    reimburseOldData!!.forEach {
                    if (it!!.id!!.equals(reimburseEditId.value)) {
                        editAmount.value = it!!.amount
                        editType.value = it!!.type!!.name
                        reimbursementTypeId.value = it!!.type!!.id.toString()

                    }
                }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
    fun getReimburseListFromDatabase(): LiveData<GetReimbursementListEntity> {
        return repository.getReimburseDataFromLiveData()
    }

    fun getReimburseTypesListFromDatabase(): LiveData<GetReimbursementTypeEntity> {
        return repository.getReimburseTypeListData()
    }

    fun callReimburseListAPIAndStore() {
        Coroutines.main {
            repository.getReimburseDataListApi()
        }
    }

    fun callReimburseTypesListAPIAndStore() {
        Coroutines.main {
            repository.getReimburseTypeListApi()
        }
    }

    fun getReimburseDocListFromDatabase(): LiveData<GetReimburseDocListEntity> {
        return repository.getReimburseDocListData()
    }

    fun callReimburseDocListAPIAndStore() {
        Coroutines.main {
            repository.getReimburseDocListApi()
        }
    }



     fun callRemoveReimburseDocsApi(docId: Int,view: View) {
         Coroutines.main {
             val response = repository.removeReimburseDocumentApi(docId)
             if(response!!.isSuccessful){
                 repository.getReimburseDocListApi()
                 Snackbar.make(
                     view!!,
                     "Document has been removed.",
                     Snackbar.LENGTH_LONG
                 ).show()

             }
         }
     }

    fun checkfields(view: View){
        if (amount.value.isNullOrEmpty()){
            Snackbar.make(view, "Please Enter Amount", Snackbar.LENGTH_LONG).show()
        }
        else if (reimbursementTypeId.value.isNullOrEmpty()){
            Snackbar.make(view, "Please Enter Reimbursement Type", Snackbar.LENGTH_LONG).show()
        }

        else{
            cancelablebutton.value = false
            createReimbursement(view)
        }
    }

    fun createReimbursement(view: View){
        progressBar.value = View.VISIBLE
        var response : Response<CreateReimbursementData>?
        Coroutines.io {
            response = repository.getCreateReimburse(amount.value!!, reimbursementTypeId.value!!)

            if (response!!.isSuccessful){
                Coroutines.main {

                    callReimburseListAPIAndStore()
                    Snackbar.make(view, "Reimbursement Added Successfully", Snackbar.LENGTH_LONG).show()
                    onclickToReimbursementListfrag(view)
                    progressBar.value = View.GONE

                }
            } else {
                Coroutines.main {

                    try {
                        if (!response!!.errorBody()!!.equals(null)) {
                            cancelablebutton.value = true
                            val jObjError = JSONObject(response!!.errorBody()!!.string())
                            val error = jObjError.getString("error")
                            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                            snackbar.show()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    progressBar.value = View.GONE
                }
            }
        }

    }

    fun checkfieldsForEditReimburse(view: View){
        if (editAmount.value.isNullOrEmpty() || reimbursementTypeId.value.isNullOrEmpty()) {

            Snackbar.make(view, "Please fill all the details", Snackbar.LENGTH_LONG).show()
            }
        else {
            cancelablebutton.value = false
            updateReimbursement(view)
        }
    }

    fun updateReimbursement(view: View){
        progressBar.value = View.VISIBLE
        var response : Response<CreateReimbursementData>?
        Coroutines.io {
            response = repository.updateReimbursements(reimburseEditId.value!!,editAmount.value!!, reimbursementTypeId.value!!)

            if (response!!.isSuccessful){
                Coroutines.main {

                    callReimburseListAPIAndStore()
                    Snackbar.make(view, "Reimbursement Updated", Snackbar.LENGTH_LONG).show()
                    onclickToReimbursementDetail(view)
                    progressBar.value = View.GONE

                }
            } else {
                Coroutines.main {

                    try {
                        if (!response!!.errorBody()!!.equals(null)) {
                            cancelablebutton.value = true
                            val jObjError = JSONObject(response!!.errorBody()!!.string())
                            val error = jObjError.getString("error")
                            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                            snackbar.show()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    progressBar.value = View.GONE
                }
            }
        }

    }

    fun getProfile(){
        progressBar.value = View.VISIBLE
        Coroutines.io {

            Coroutines.main {
                try {
                    amount.value =  reimburseDetails.value!!.amount
                    type.value = reimburseDetails.value!!.type!!.name
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                                    progressBar.value = View.GONE
            }
        }


    }

    fun callRemoveReimburseApi(reimburseId: Int,view: View) {
        Coroutines.main {
            val response = repository.removeReimbursementsApi(reimburseId)
            if(response!!.isSuccessful){
                Snackbar.make(
                    view!!,
                    "Reimbursement has been removed.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    fun createReimbursementDocument(view: View) {

        if (doc_image_url.value.isNullOrEmpty() || document_name.value.isNullOrEmpty()) {

            Snackbar.make(view, "Please add files & file name", Snackbar.LENGTH_LONG).show()

        }
        else {
            progressBar.value = View.VISIBLE
            cancelablebutton.value = false

            Coroutines.io {

                val docFile = File(doc_image_url.value!!)

                val response = repository.getCreateReimburseDocsApi(
                    document_name.value!!,
                    reimburse_id_forDoc.value!!,
                    docFile

                )
                if (response!!.isSuccessful) {

                    Coroutines.main {
                        callReimburseDocListAPIAndStore()
                        onclickToReimbursementDetail(view)
                        Toast.makeText(
                            view.context,
                            "Successfully Document Uploaded",
                            Toast.LENGTH_SHORT
                        ).show()
                        cancelablebutton.value = true
                        progressBar.value = View.GONE

                    }
                } else {
                    Coroutines.main {

                        try {
                            if (!response!!.errorBody()!!.equals(null)) {
                                val jObjError =
                                    JSONObject(response!!.errorBody()!!.string())
                                val error = jObjError.getString("error")
                                val snackbar =
                                    Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                snackbar.show()

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        cancelablebutton.value = true
                        progressBar.value = View.GONE
                    }
                }
            }
        }
    }


   /* fun updateDocument(view: View) {

        if (doc_image_url.value.equals(null) || document_name.value.isNullOrEmpty()) {

            Snackbar.make(view, "Please add files & file name", Snackbar.LENGTH_LONG).show()

        }
        else {
            progressBar.value = View.VISIBLE
            cancelablebutton.value = false

            Coroutines.io {

                val docFile = File(doc_image_url.value!!)

                val response = repository.getUpdateClientDoc(
                    doc_Id.value!!,
                    document_name.value!!,
                    docFile

                )
                if (response!!.isSuccessful) {

                    Coroutines.main {
                        callClientDocListAPIAndStore()
                        onclickToClientDetail(view)
                        Toast.makeText(
                            view.context,
                            "Successfully Document Updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        cancelablebutton.value = true
                        progressBar.value = View.GONE

                    }
                } else {
                    Coroutines.main {

                        try {
                            if (!response!!.errorBody()!!.equals(null)) {
                                val jObjError =
                                    JSONObject(response!!.errorBody()!!.string())
                                val error = jObjError.getString("error")
                                val snackbar =
                                    Snackbar.make(view, error, Snackbar.LENGTH_LONG)
                                snackbar.show()

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        cancelablebutton.value = true
                        progressBar.value = View.GONE
                    }
                }
            }
        }
    }*/

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun goToUpdateReimburseFragment(view: View){
        view.findNavController().navigate(R.id.updateReimbursementFragment)
    }
    fun onclickToReimbursementListfrag(view: View) {
        view.findNavController().navigate(R.id.reimbursementListFragment)

    }
    fun onclickToReimbursementDetail(view: View) {
        view.findNavController().navigate(R.id.reimbursementDetailFragment)

    }
    fun onclickToCreateReimburseDocs(view: View) {
        view.findNavController().navigate(R.id.createReimbureDocumentFragment)
    }


}