package com.prologicwebsolution.microatm.ui.aepes

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.prologicwebsolution.microatm.aeps_utils.AddTextWatcher
import com.prologicwebsolution.microatm.data.aepsBankList.BalanceReq
import com.prologicwebsolution.microatm.data.aepsBankList.BalanceRes
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
import java.text.SimpleDateFormat
import java.util.*


class AepsViewModel(application: Application) : AndroidViewModel(application) {
    var aadhar_no: MutableLiveData<String> = MutableLiveData("")
    val bank_name: MutableLiveData<String> = MutableLiveData("")
    val bank_code: MutableLiveData<String> = MutableLiveData("")
    var amount: MutableLiveData<String> = MutableLiveData("")
    var finger_print_status: MutableLiveData<String> = MutableLiveData("")
    var result_status: MutableLiveData<String> = MutableLiveData("")
    var intent_open: MutableLiveData<String> = MutableLiveData<String>("")
    var error: MutableLiveData<String> = MutableLiveData<String>("")
    var amount_lay: MutableLiveData<Int> = MutableLiveData<Int>(View.GONE)
    var tnx_type: ObservableField<String> = ObservableField<String>("")
    val homeRepository: HomeRepository
    var progress_bar = MutableLiveData<Boolean>(false)

    init {
        homeRepository = HomeRepository(application)
    }

    fun fingerOpenClick(view: View) {
        if (aadhar_no.value?.replace(" ", "")?.length != 12) {
            error.postValue("Please enter Aadhar No")
        } else if (bank_code.value?.isEmpty() == true || bank_name.value?.isEmpty() == true) {
            error.postValue("Select Bank")

        } else {
            intent_open.postValue("aeps")
        }
    }

    val cardNoWatcher = object : AddTextWatcher() {

    }

    fun setTnxType(value: String) {
        tnx_type.set(value)
        Log.d("AEPS_TNX_TYPE", tnx_type.get()!!)
        if (tnx_type.get().equals("CW")) {
            amount_lay.value = View.VISIBLE
        } else {
            amount_lay.value = View.GONE
        }
    }

    fun getAepsBankList(view: View) {
        intent_open.postValue("bank")
    }

    fun clearDataClick(view: View) {
        clearData()
    }

    fun clearData() {
        intent_open.postValue("")
        finger_print_status.postValue("")
        aadhar_no.postValue("")
        bank_code.postValue("")
        bank_name.postValue("")
        amount.postValue("")
        result_status.postValue("")
        amount_lay.value = View.GONE
    }


    fun getAepsApi(jsonData: String, pidData: String) {
        val paramBalanceReq = BalanceReq()
        paramBalanceReq.requestcode = "aepsTxn"
        paramBalanceReq.txType = tnx_type.get()
        paramBalanceReq.username = Constants.USER_NAME
        paramBalanceReq.mid = Constants.AEPS_MID
        paramBalanceReq.tid = Constants.AEPS_TID
        paramBalanceReq.imei = "866532048183953"
        paramBalanceReq.imsi = "null"
        paramBalanceReq.location = "19.052173333333332|73.01934333333334"
        paramBalanceReq.aadharNo = "0" + aadhar_no.value?.replace(" ", "")
        paramBalanceReq.amount = amount.value
        paramBalanceReq.bankCode = bank_code.value
        paramBalanceReq.ifscCode = bank_code.value
        paramBalanceReq.stan = getRetrievalNo()
        paramBalanceReq.data = jsonData
        paramBalanceReq.pidData = pidData
        progress_bar.postValue(true)


        var response: Response<BalanceRes>? = null
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    response = homeRepository.getAepsTnx(paramBalanceReq)
                }
            }.onSuccess {
                progress_bar.postValue(false)
                if (response?.isSuccessful == true) {
                    try {
                        val balanceRes = response?.body()
                        var msg = "\nStatus : " + balanceRes?.msg
                        if (tnx_type.get().equals("BE")) {
                            msg += "\nBalance : " + balanceRes?.balance
                        } else if (tnx_type.get().equals("MS")) {
                            msg += "\nTransaction\n" + balanceRes?.msData!!.replace(";", "\n")
                        } else if (tnx_type.get().equals("CW")) {
                            msg += "\nBalance\n" + balanceRes?.balance
                        }
                        result_status.postValue(msg)
                    }catch (e:Exception){

                    }

                } else {
                    error.postValue("Error")
                }
            }.onFailure {
                progress_bar.postValue(false)
                error.postValue(RetrofitClient.errorException(it))
            }
        }

    }

    fun getRetrievalNo(): String? {
        val stan_random_value = Random().nextInt(999999)
        val rrn = SimpleDateFormat("yyyyDDDHH").format(Date()).substring(3) + stan_random_value
        println("RRN : $rrn")
        return rrn
    }

    override fun onCleared() {
        super.onCleared()
    }

}