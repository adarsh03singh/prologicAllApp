package com.prologicwebsolution.microatm.ui.commission

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.repo.TransactionRepository
import com.prologicwebsolution.microatm.util.Coroutines
import org.json.JSONObject

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class CommissionViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository

    val filteredCommissionData = MutableLiveData<GetTransactionEntity?>()

    var start_date = MutableLiveData<String>().apply { postValue("") }
    var end_date = MutableLiveData<String>().apply { postValue("") }

    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }

    init {
        transactionRepository = TransactionRepository(application)

    }

    fun callCommissionListAfterCheckInternet(view: View) {
        if (isNetworkConnected(view.context!!)) {
            try {
                checkfieldsAndCallPayoutListApi(view)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(view.context, "No internet found.", Toast.LENGTH_LONG).show()
        }
    }

    fun checkfieldsAndCallPayoutListApi(view: View) {
        if (start_date.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter start date", Snackbar.LENGTH_LONG).show()
        } else if (end_date.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter end date", Snackbar.LENGTH_LONG).show()
        } else {
            callPayoutListApi(view)
        }
    }


    fun callPayoutListApi(view: View) {
        progressBar.value = View.VISIBLE
        var response: retrofit2.Response<GetTransactionEntity>?
        Coroutines.io {

            response = transactionRepository.getTransactionDetailApi(start_date.value!!.toString(), end_date.value!!.toString())

            if (response!!.isSuccessful) {

                Coroutines.main {
                    if (!response?.body()?.data!!.isNullOrEmpty()) {

                        filteredCommissionData.value = response!!.body()
                        progressBar.value = View.GONE

                    } else {
                        filteredCommissionData.value = response!!.body()
                        showDialog(view,response!!.body()!!.msg)
                        progressBar.value = View.GONE
                    }
                }
            } else {
                Coroutines.main {

                    try {
                        if (!response!!.errorBody()!!.equals(null)) {
//                            cancelablebutton.value = true
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
