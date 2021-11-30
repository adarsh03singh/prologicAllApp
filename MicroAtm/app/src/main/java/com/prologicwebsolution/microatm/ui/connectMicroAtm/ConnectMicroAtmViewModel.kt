package com.prologicwebsolution.microatm.ui.connectMicroAtm

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.savetransaction.SaveTransactionData
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.network2.SaveTransactionApiBody
import com.prologicwebsolution.microatm.repo.ConnectMicroAtmRepository
import com.prologicwebsolution.microatm.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.lang.NullPointerException


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class ConnectMicroAtmViewModel(application: Application) : AndroidViewModel(application) {

    private var dialog: ProgressDialog? = null

    var pairedDeviceAddress:String? = null

    var amount: String? = null
    var bankremarks: String? = null
    var clientrefid : String? = null
    var cardno: String? = null
    var date: String? = null
    var invoicenumber: String? = null
    var refstan: String? = null
    var mid: String? = "PRO000000000001"
    var rrn: String? = null
    var statuscode: String? = null
    var tid: String? = "PRO00001"
    var txnType: String? = null
    var txnAmount: String? = null

    private val connectMicroAtmRepository: ConnectMicroAtmRepository
    val transactionData = MutableLiveData<String?>()

    val sharedPref: SharedPreferences = application.getSharedPreferences("PREFERENCE_NAME", 0)
    val userId =sharedPref.getString("user_id", "")

    init{
        connectMicroAtmRepository = ConnectMicroAtmRepository(application)
    }

    fun SavetransactionApiCall(view: View) {

        val saveTransactionApiBody = SaveTransactionApiBody()
        saveTransactionApiBody.amount = amount!!
        saveTransactionApiBody.bankremarks = bankremarks!!
        saveTransactionApiBody.cardno = cardno!!
        saveTransactionApiBody.clientrefid = clientrefid!!
        saveTransactionApiBody.date = date!!
        saveTransactionApiBody.invoicenumber = invoicenumber!!
        saveTransactionApiBody.mid = mid!!
        saveTransactionApiBody.refstan = refstan!!
        saveTransactionApiBody.rrn = rrn!!
        saveTransactionApiBody.statuscode = statuscode!!
        saveTransactionApiBody.tid = tid!!
        saveTransactionApiBody.txnType = txnType!!
        saveTransactionApiBody.txnamount = txnAmount!!
        saveTransactionApiBody.user_id = userId!!

            showProgress(view)
            var response: Response<SaveTransactionData>? = null
            viewModelScope.launch {
                kotlin.runCatching {
                    withContext(Dispatchers.IO) {
                        response = connectMicroAtmRepository.callSaveTransactionApi(saveTransactionApiBody)
                    }

                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
                        if (response?.isSuccessful == true) {
                            if (response?.body()?.status!! == "SUCCESS") {

                                val msg = response!!.body()!!.msg
                                Log.d("SaveTransactionResponse", msg)

                            }

                        } else {
                            showToast(view, "Something went wrong")
                        }
                    }

                }.onFailure {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
                        try {
                            showToast(view, RetrofitClient.errorException(it))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                }
            }

    }

    fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    fun showProgress(view: View) {
        dialog = ProgressDialog(view.context)
        dialog!!.setMessage("Please wait.")
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun dismissProgress() {
        if (dialog != null)
            if (dialog!!.isShowing)
                dialog!!.dismiss()
    }


}