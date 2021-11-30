package com.prologicwebsolution.microatm.ui.withdrawlStatus

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.repo.LoginRepository
import com.prologicwebsolution.microatm.repo.TransactionRepository
import com.prologicwebsolution.microatm.repo.TransactionStatusRepository
import com.prologicwebsolution.microatm.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class WithdrawlStatusViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionStatusRepository: TransactionStatusRepository

    val filteredCommissionData = MutableLiveData<PayoutListEntity?>()
    private var dialog: ProgressDialog? = null
    var start_date = MutableLiveData<String>().apply { postValue("") }
    var end_date = MutableLiveData<String>().apply { postValue("") }

    init {
        transactionStatusRepository = TransactionStatusRepository(application)

    }


    fun callPayoutListApi(view: View) {

        if (start_date.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter start date", Snackbar.LENGTH_LONG).show()
        } else if (end_date.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter end date", Snackbar.LENGTH_LONG).show()
        } else {
            showProgress(view)
            var response: Response<PayoutListEntity>? = null
            viewModelScope.launch {
                kotlin.runCatching {
                    withContext(Dispatchers.IO) {
                        response =
                            transactionStatusRepository.callPaymwntListApi(start_date.value!!.toString(),
                                end_date.value!!.toString())

                    }
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
                        if (response?.isSuccessful == true) {

                            if (!response?.body()?.data!!.isNullOrEmpty()) {

                                filteredCommissionData.value = response!!.body()

                            } else {
                                filteredCommissionData.value = response!!.body()
                                showDialog(view, response!!.body()!!.msg)

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


    fun showDialog(view: View, message: String) {

        val dialogBuilder = AlertDialog.Builder(view.context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            // if the dialog is cancelable
            .setCancelable(false)
            // negative button text and action
            .setNegativeButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Oops!")
        // show alert dialog
        alert.show()
    }


}
