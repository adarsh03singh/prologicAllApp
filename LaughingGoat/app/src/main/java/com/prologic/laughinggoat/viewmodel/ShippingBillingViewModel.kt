package com.prologic.laughinggoat.viewmodel


import android.app.Application
import android.os.Bundle
import android.view.View

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blog.prologic.model.country_state.CountryState

import com.google.gson.Gson
import com.prologic.laughinggoat.network.Repository
import com.prologic.laughinggoat.network.errorException
import com.prologic.laughinggoat.search_dialog.SearchItem
import com.prologic.laughinggoat.utils.SharedPreference

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShippingBillingViewModel(application: Application) : AndroidViewModel(application) {
    val apiRepository = Repository()
    var firstName = MutableLiveData<String>("")
    var lastName = MutableLiveData<String>("")
    var address_1 = MutableLiveData<String>("")
    var address_2 = MutableLiveData<String>("")
    var companyName = MutableLiveData<String>("")
    var countryRegionName = MutableLiveData<String>("")
    var countryRegionId = MutableLiveData<String>("")
    var cityName = MutableLiveData<String>("")
    var stateName = MutableLiveData<String>("")
    var postcode = MutableLiveData<String>("")
    var phoneNumber = MutableLiveData<String>("")
    var emailAddress = MutableLiveData<String>("")
    var countryState = MutableLiveData<CountryState>()
    var errorMessage = MutableLiveData<String>("")


    var view_type = MutableLiveData<Int>(View.GONE)
    val sharedPreference = SharedPreference()


    fun setValue(bundle: Bundle) {
        if (bundle.size() > 0) {
            firstName.value = bundle.getString("firstName")
            lastName.value = bundle.getString("lastName")
            address_1.value = bundle.getString("address_1")
            address_2.value = bundle.getString("address_2")
            companyName.value = bundle.getString("companyName")
            countryRegionName.value = bundle.getString("countryRegionName")
            countryRegionId.value = bundle.getString("countryRegionId")
            stateName.value = bundle.getString("stateName")
            cityName.value = bundle.getString("cityName")
            postcode.value = bundle.getString("postcode")
            phoneNumber.value = bundle.getString("phoneNumber")
            emailAddress.value = bundle.getString("emailAddress")

        }
    }

    fun getBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("firstName", firstName.value)
        bundle.putString("lastName", lastName.value)
        bundle.putString("address_1", address_1.value)
        bundle.putString("address_2", address_2.value)
        bundle.putString("companyName", companyName.value)
        bundle.putString("countryRegionName", countryRegionName.value)
        bundle.putString("countryRegionId", countryRegionId.value)
        bundle.putString("stateName", stateName.value)
        bundle.putString("cityName", cityName.value)
        bundle.putString("postcode", postcode.value)
        bundle.putString("phoneNumber", phoneNumber.value)
        bundle.putString("emailAddress", emailAddress.value)
        return bundle
    }

    fun getCountry(): ArrayList<SearchItem> {
        val listSearch = ArrayList<SearchItem>()
        val countries = countryState.value!!.countries
        for (item in countries) {
            val searchItem = SearchItem()
            searchItem.id = item.alpha2Code
            searchItem.title = item.country
            listSearch.add(searchItem)
        }
        return listSearch
    }

    fun getState(): ArrayList<SearchItem> {
        val stateArray = getStateArray()
        val listSearch = ArrayList<SearchItem>()
        for (item in stateArray) {
            val searchItem = SearchItem()
            searchItem.id = ""
            searchItem.title = item
            listSearch.add(searchItem)
        }
        return listSearch
    }

    fun getStateArray(): List<String> {
        val countries = countryState.value!!.countries
        for (item in countries) {
            if (item.alpha2Code.equals(countryRegionId.value)) {
                return item.states
            }
        }
        return arrayListOf()
    }

    fun getCountryState() {
        val json = sharedPreference.getString("country_state")
        if (json!!.isNotEmpty()) {
            countryState.postValue(Gson().fromJson(json, CountryState::class.java))
        }
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    countryState.postValue(apiRepository.getCountryState())
                }
            }.onSuccess {
            }.onFailure {
                errorMessage.postValue(errorException(it))
            }
        }
        countryState.observeForever {
            if (it.countries.size > 0) {

            }
        }
    }


}