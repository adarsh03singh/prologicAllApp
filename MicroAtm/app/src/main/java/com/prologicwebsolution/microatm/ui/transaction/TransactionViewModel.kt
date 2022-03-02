package com.prologicwebsolution.microatm.ui.transaction

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.repo.TransactionRepository
import com.prologicwebsolution.microatm.util.hideSoftKeyBoard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun transactionADetailpi(view: View) {
        hideSoftKeyBoard()
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
    fun showDialog(view: View, message: String){
        val dialogBuilder = AlertDialog.Builder(view.context)
        dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                })

        val alert = dialogBuilder.create()
        alert.setTitle("Oops!")
        alert.show()
    }
}
