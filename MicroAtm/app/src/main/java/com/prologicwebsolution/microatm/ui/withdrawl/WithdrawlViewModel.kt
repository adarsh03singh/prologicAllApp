package com.prologicwebsolution.microatm.ui.withdrawl

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.repo.LoginRepository
import com.prologicwebsolution.microatm.repo.PayoutRepository
import com.prologicwebsolution.microatm.repo.TransactionRepository
import com.prologicwebsolution.microatm.util.Coroutines
import kotlinx.android.synthetic.main.fragment_withdrawl.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class WithdrawlViewModel(application: Application) : AndroidViewModel(application) {

    private val payoutRepository: PayoutRepository
    private val transactionRepository: TransactionRepository
    private var dialog: ProgressDialog? = null
    var totalAmountValue = MutableLiveData<String>()
    var amountValue = MutableLiveData<String>().apply { postValue("") }
    var modeypeValue = MutableLiveData<String>().apply { postValue("") }
    var walletbalance: String? = null


    init {
        payoutRepository = PayoutRepository(application)
        transactionRepository = TransactionRepository(application)

        // GETTING WALLET BALNCE
        Coroutines.io {
            try {
                getAllWalletDataFromDatabase()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    fun callWithdrawPayoutApi(view: View) {

        if (amountValue.value.isNullOrEmpty()) {
            Snackbar.make(view, "Please enter amount", Snackbar.LENGTH_LONG).show()
        } else if (amountValue.value.toString() > walletbalance.toString()) {
            showDialog(view, "Sorry But your wallet does not have sufficients", "OOPS")

        } else if (modeypeValue.value!!.isEmpty()) {
            Snackbar.make(view, "Please Select Withdrawl Method", Snackbar.LENGTH_LONG).show()

        } else {
            showProgress(view)
            var response: Response<PayoutDataEntity>? = null
            viewModelScope.launch {
                kotlin.runCatching {
                    withContext(Dispatchers.IO) {
                        response = payoutRepository.callPaymwntWithdrawApi(modeypeValue.value!!,
                            amountValue.value!!)
                    }

                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        dismissProgress()
                        if (response?.isSuccessful == true) {
                            if (response?.body()?.status!! == "SUCCESS") {

                                val msg = response?.body()?.msg!!
                                val titleMsg = response?.body()?.status!!
                                showDialog(view, msg, titleMsg)
                            } else {
                                try {
                                    if (response?.body()?.status!! == "ERR") {
                                        val msg = response?.body()?.msg!!
                                        showDialog(view, msg, "OOPS")

                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

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


    fun getAllWalletDataFromDatabase() {

            try {
                transactionRepository.getWalletBalnseFromDatabase().forEach {
                    walletbalance = it.balance
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()

            }
            Coroutines.main {
                try {
                    totalAmountValue.value = "\u20B9" + " " + walletbalance
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()

                }
            }

    }

    fun showDialog(view: View, message: String, titleMsg: String) {

        val dialogBuilder = AlertDialog.Builder(view.context)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            // if the dialog is cancelable
            .setCancelable(false)
            // negative button text and action
            .setNegativeButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                if (titleMsg.equals("SUCCESS")) {
                    view.findNavController().navigate(R.id.dashboardFragment)
                }

            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(titleMsg)
        // show alert dialog
        alert.show()
    }
}
