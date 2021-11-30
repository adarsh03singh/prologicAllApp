package com.prologicwebsolution.microatm.ui.aeps_bank

import android.app.Application

import android.text.Editable
import androidx.lifecycle.MutableLiveData


import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope


import com.prologicwebsolution.microatm.data.aepsBankList.BankCodeValue
import com.prologicwebsolution.microatm.data.aepsBankList.BankReqModel
import com.prologicwebsolution.microatm.data.aepsBankList.BankResModel
import com.prologicwebsolution.microatm.network2.RetrofitClient

import com.prologicwebsolution.microatm.repo.HomeRepository

import com.prologicwebsolution.microatm.util.Constants
import com.prologicwebsolution.microatm.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class BankListViewModel(application: Application) : AndroidViewModel(application) {
    val onBackPressedCall = MutableLiveData<String>()
    val codeValueList = MutableLiveData<ArrayList<BankCodeValue>>()
    val errorMessage = MutableLiveData<String>()
    val searchValue = MutableLiveData<String>()
    val error_msg = MutableLiveData<String>()
    val homeRepository: HomeRepository
    val progress_bar = MutableLiveData<Boolean>()

    init {
        homeRepository = HomeRepository(application)
    }

    fun back(view: View) {
        onBackPressedCall.value = ""
    }

    fun search_et_watcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                searchValue.value = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
            }
        }
    }

    fun getApi() {
        val bankReqModel = BankReqModel()
        bankReqModel.requestcode = "aepsBanks"
        bankReqModel.username = Constants.USER_NAME
        bankReqModel.mid = Constants.AEPS_MID
        bankReqModel.tid = Constants.AEPS_TID
        bankReqModel.imei = "866532048183953"
        bankReqModel.imsi = "null"
        progress_bar.postValue(true)

        var response: Response<BankResModel>?=null
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                     response = homeRepository.getAepsBankList(bankReqModel)
                }
            }.onSuccess {
                progress_bar.postValue(false)
                codeValueList.postValue(response?.body()?.codeValues)
            }.onFailure {
                progress_bar.postValue(false)
                error_msg.postValue(RetrofitClient.errorException(it))
            }
        }


        //  var response: retrofit2.Response<BankResModel>?
//        Coroutines.io {
//            var response = homeRepository.getAepsBankList(bankReqModel)
//            Coroutines.main {
//                progress_bar.postValue(false)
//                if (response.isSuccessful) {
//                    codeValueList.postValue(response.body()?.codeValues)
//                } else {
//                }
//            }
//
//        }
    }


}