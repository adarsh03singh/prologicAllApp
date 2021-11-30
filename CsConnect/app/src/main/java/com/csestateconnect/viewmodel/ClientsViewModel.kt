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
class ClientsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ClientRepository
    private val authRepository: AuthRepository
    val clDetails = MutableLiveData<Result>()
    val clientRecentId = MutableLiveData<Int>()
    val clientEditId = MutableLiveData<Int>()

   var progressBar = MutableLiveData<Int>().apply{postValue(View.GONE)}
    var checkClientsRemove = MutableLiveData<Boolean>().apply{postValue(false)}
    var cancelablebutton = MutableLiveData<Boolean>().apply { postValue(true) }

    var cityIndexValue = MutableLiveData<Int>().apply { postValue(0) }
    var countryIndexValue  = MutableLiveData<Int>().apply { postValue(0) }
    var locationIndexValue = MutableLiveData<Int>().apply { postValue(0) }


    //for Edit client profile from server
    var editClientName = MutableLiveData<String>()
    var editClientEmailId = MutableLiveData<String>()
    var editClientAddress = MutableLiveData<String>()
    var editClientMobile = MutableLiveData<String>()
    var editClientCountry = MutableLiveData<String>().apply { postValue("") }
    var editClientLocation = MutableLiveData<String>()
    var editClientCity = MutableLiveData<String>()

    //for get client profile from server
    var clientName = MutableLiveData<String>()
    var clientEmailId = MutableLiveData<String>()
    var clientAddress = MutableLiveData<String>()
    var clientMobile = MutableLiveData<String>()
    var clientCountry = MutableLiveData<String>()
    var clientLocation = MutableLiveData<String>()
    var clientCity = MutableLiveData<String>()
    var count: Int? = 0

    //CREATE/UPDATE CLIENT DOC
    var doc_Id = MutableLiveData<Int>()
    var document_name = MutableLiveData<String>()
    var client_id_forDoc = MutableLiveData<String>()
    var doc_image_url = MutableLiveData<String>()



    val sharedPref: SharedPreferences = application.getSharedPreferences("tokenValue", 0)
    val token ="Token " + sharedPref.getString("tokenValue", "")

    init {
        var clientOldData: List<Result?>? = null
        authRepository = AuthRepository(application)
        repository = ClientRepository(application)
        Coroutines.io {

                        try {
                            repository.getClientListData()
                            clientOldData =  repository.getClientResultData()
             } catch (e: Exception) {
            e.printStackTrace()
        }
            Coroutines.main {
                try {
                clientOldData!!.forEach {
                    if (it!!.id!!.equals(clientEditId.value)) {
                        if (it!!.name != null) {
                            count = 1
                        }
                        editClientName.value = it!!.name
                        editClientMobile.value = it!!.phoneNumber
                        editClientEmailId.value = it!!.email
                        editClientAddress.value = it!!.address
                        editClientCountry.value = it!!.country!!.name
                        editClientCity.value = it!!.city!!.name
                        editClientLocation.value = it!!.location!!.name
                        countryIndexValue.value = it!!.country!!.id
                        cityIndexValue.value = it!!.city!!.id
                        locationIndexValue.value = it!!.location!!.id
                    }
                }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }
    fun getClientListFromDatabase(): LiveData<ClientList> {
        return repository.getClientListData()
    }

    fun callClientListAPIAndStore() {
        Coroutines.main {
            repository.getClientListApi()
        }
    }

    fun getClientDocListFromDatabase(): LiveData<GetClientDocListEntity> {
        return repository.getClientDocListData()
    }

    fun callClientDocListAPIAndStore() {
        Coroutines.main {
            repository.getClientDocListApi()
        }
    }

    fun callRemoveClientsDocsApi(docId: Int,view: View) {
        Coroutines.main {
            val response = repository.removeClientsDocument(docId)
            if(response!!.isSuccessful){
                repository.getClientDocListApi()
                Snackbar.make(
                    view!!,
                    "Document has been removed.",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun checkfields(view: View){
        if (clientName.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter First Name", Snackbar.LENGTH_LONG).show()
        }
        else if (!isEmailValid(clientEmailId.value)){
            Snackbar.make(view, "Please enter valid Email Address", Snackbar.LENGTH_LONG).show()
        }
        else if (clientCountry.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter Country", Snackbar.LENGTH_LONG).show()
        }
        else if (clientCity.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter City", Snackbar.LENGTH_LONG).show()
        }

        else if (clientLocation.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter Location", Snackbar.LENGTH_LONG).show()
        }
        else if (clientAddress.value.isNullOrEmpty()){
            Snackbar.make(view, "Please enter Address", Snackbar.LENGTH_LONG).show()
        }

        else{
            cancelablebutton.value = false
            createProfileClient(view)
        }
    }

    fun checkfieldsForEditClient(view: View){
        if (editClientName.value.isNullOrEmpty()
            || editClientMobile.value.isNullOrEmpty()  || editClientEmailId.value.isNullOrEmpty()
            || editClientCountry.value.isNullOrEmpty() || editClientCity.value.isNullOrEmpty()
            || editClientLocation.value.isNullOrEmpty() || editClientAddress.value.isNullOrEmpty()
        ) {

            Snackbar.make(view, "Please fill all the details", Snackbar.LENGTH_LONG).show()
        } else if (!isEmailValid(editClientEmailId.value)){
            Snackbar.make(view, "Please enter valid Email Address", Snackbar.LENGTH_LONG).show()
        }
            else {
            cancelablebutton.value = false
            updateProfileClient(view)
        }
    }


    fun createProfileClient(view: View){
        progressBar.value = View.VISIBLE
        var response : Response<CreateClientData>?
        Coroutines.io {
            response = repository.getCreateClient(clientName.value!!, clientMobile.value, clientEmailId.value!!,
                countryIndexValue.value, cityIndexValue.value, locationIndexValue.value.toString(), clientAddress.value!!)

            if (response!!.isSuccessful){
                Coroutines.main {

                    callUserProfileGetApi()
                    Snackbar.make(view, "Client Profile Created", Snackbar.LENGTH_LONG).show()
                    onclickToClientListfrag(view)
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

    fun updateProfileClient(view: View){
        progressBar.value = View.VISIBLE
        var response : Response<CreateClientData>?
        Coroutines.io {
            response = repository.updateClientProfile(clientEditId.value!!,editClientName.value!!, editClientMobile.value, editClientEmailId.value!!,
                countryIndexValue.value, cityIndexValue.value, locationIndexValue.value.toString(), editClientAddress.value!!)

            if (response!!.isSuccessful){
                Coroutines.main {

                    callUserProfileGetApi()
                    Snackbar.make(view, "Client Profile Updated", Snackbar.LENGTH_LONG).show()

                    onclickToClientDetail(view)
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
    fun getCountriesData():  LiveData<List<Countries>>{
        return authRepository.getallLiveDataCountriesDatabase()
    }


    fun getProfile(){
        progressBar.value = View.VISIBLE
        Coroutines.io {

            Coroutines.main {
                try {
                    clientName.value =  clDetails.value!!.name
                    clientEmailId.value = clDetails.value!!.email
                    clientMobile.value = clDetails.value!!.phoneNumber
                    clientAddress.value = clDetails.value!!.address
                    clientCountry.value = clDetails.value!!.country!!.name
                    clientCity.value = clDetails.value!!.city!!.name
                    clientLocation.value = clDetails.value!!.location!!.name

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                                    progressBar.value = View.GONE
            }
        }


    }

    fun callRemoveClientsApi(clientId: Int,view: View) {
        Coroutines.main {
            val response = repository.removeClientsData(clientId)
            if(response!!.isSuccessful){
                Snackbar.make(
                    view!!,
                    "Client has been removed.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }


    fun createDocument(view: View) {

        if (doc_image_url.value.isNullOrEmpty() || document_name.value.isNullOrEmpty()) {

            Snackbar.make(view, "Please add files & file name", Snackbar.LENGTH_LONG).show()

        }
        else {
            progressBar.value = View.VISIBLE
            cancelablebutton.value = false

            Coroutines.io {

                val docFile = File(doc_image_url.value!!)

                val response = repository.getCreateClientDoc(
                    document_name.value!!,
                    client_id_forDoc.value!!,
                    docFile

                )
                if (response!!.isSuccessful) {

                    Coroutines.main {
                        callClientDocListAPIAndStore()
                        onclickToClientDetail(view)
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


    fun updateDocument(view: View) {

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
    }


    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }

    fun goToEditClientFragment(view: View){
        view.findNavController().navigate(R.id.edit_clientFragment)
    }
    fun onclickToClientListfrag(view: View) {
        view.findNavController().navigate(R.id.client_listFragment)

    }
    fun onclickToClientDetail(view: View) {
        view.findNavController().navigate(R.id.clients_detailFragment)

    }



    fun callUserProfileGetApi() {
        Coroutines.io {
            try {
                repository.getClientListData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}