package com.prologicwebsolution.microatm.ui.transaction

import android.app.Activity
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
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.repo.TransactionRepository
import com.prologicwebsolution.microatm.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private var dialog: ProgressDialog? = null
     val transactionRepository: TransactionRepository
    val filteredTransactionData = MutableLiveData<GetTransactionEntity?>()

    var startDate = MutableLiveData<String>().apply { postValue("") }
    var endDate = MutableLiveData<String>().apply { postValue("") }
    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }

    init {
        transactionRepository = TransactionRepository(application)
    }



    fun callTransactionApiAfterCheckInternet(view: View) {
        if (isNetworkConnected(view.context!!)) {
            try {
                checkfieldsAndCallTransactionApi(view)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(view.context, "No internet found.", Toast.LENGTH_LONG).show()
        }
    }


    fun checkfieldsAndCallTransactionApi(view: View) {
        if (startDate.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter start date", Snackbar.LENGTH_LONG).show()
        } else if (endDate.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter end date", Snackbar.LENGTH_LONG).show()
        } else {
            transactionADetailpi(view)
        }
    }


    fun transactionADetailpi(view: View) {

        if (startDate.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter start date", Snackbar.LENGTH_LONG).show()
        } else if (endDate.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter end date", Snackbar.LENGTH_LONG).show()
        } else {
            showProgress(view)
            var response: Response<GetTransactionEntity>? = null
            viewModelScope.launch {
                kotlin.runCatching {
                    withContext(Dispatchers.IO) {
                        response =
                            transactionRepository.getTransactionDetailApi(startDate.value!!.toString(),
                                endDate.value!!.toString())
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
                        if (response?.isSuccessful == true) {

                            if (!response?.body()?.data!!.isNullOrEmpty()) {
                                filteredTransactionData.value = response!!.body()
                            } else {
                                filteredTransactionData.value = response!!.body()
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

    fun showToast(view: View,msg: String) {
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

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun showDialog(view: View, message: String){

        val dialogBuilder = AlertDialog.Builder(view.context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
                // if the dialog is cancelable
                .setCancelable(false)
                // negative button text and action
                .setNegativeButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Oops!")
        // show alert dialog
        alert.show()
    }
}
