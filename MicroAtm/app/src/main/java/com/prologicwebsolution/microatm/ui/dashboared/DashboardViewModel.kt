package com.prologicwebsolution.microatm.ui.dashboared

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.data.loginData.LoginEntity
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import com.prologicwebsolution.microatm.database.MyDatabase
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.repo.LoginRepository
import com.prologicwebsolution.microatm.repo.TransactionRepository
import com.prologicwebsolution.microatm.ui.loginUi.LoginActivity
import com.prologicwebsolution.microatm.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.lang.NullPointerException

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val myDatabase = MyDatabase.getInstance(application)
    private val transactionRepository: TransactionRepository
    val walletAllData = MutableLiveData<WalletEntity?>()
    private var dialog: ProgressDialog? = null
    var walletText = MutableLiveData<String>().apply { postValue("") }
     val _isLoading = MutableLiveData<Boolean>()


    init {
        transactionRepository = TransactionRepository(application)
    }


    fun walletBalanceApiCall(view: View){

//        showProgress(view)
        _isLoading.value = true
        var response: Response<WalletEntity>? = null
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    response = transactionRepository.callWalletBalnsApi("get_wallet_balance")
                }
            }.onSuccess {
                withContext(Dispatchers.Main) {
//                    dismissProgress()
                    _isLoading.value = false
                    if (response?.isSuccessful == true) {

                        if (response?.body()?.status!!.equals("SUCCESS")) {
                            try {
                                walletText.value ="\u20B9 " + response!!.body()!!.balance
                            }catch (e: java.lang.Exception){
                                e.printStackTrace()
                            }

                        }

                    } else {
                        showToast(view, "Something went wrong")
                    }
                }

            }.onFailure {
                withContext(Dispatchers.Main) {
//                    dismissProgress()
                    _isLoading.value = false
                    try {
                        showToast(view, RetrofitClient.errorException(it))
                    }catch (e: Exception){
                        e.printStackTrace()
                        showToast(view,e.toString())
                    }

                }

            }
        }

    }

    fun showToast(view: View,msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    fun showProgress(view: View) {
        val activity = view.context as Activity
        dialog = ProgressDialog(activity)
        dialog!!.setMessage("Please wait.")
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun dismissProgress() {
        if (dialog != null)
            if (dialog!!.isShowing)
                dialog!!.dismiss()
    }


        fun getAllWalletDataFromDatabase(): LiveData<List<WalletEntity>> {
        return transactionRepository.getWalletBalnsFromDatabase()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun logout(view: View) {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    myDatabase.clearAllTables()
//                    sharePreference.clearallSharedPrefernce()
                }
            }.onSuccess {
                val intent = Intent(view.context, LoginActivity::class.java)
                view.context?.startActivity(intent)
                (view.context as Activity).finish()
            }.onFailure {

            }
        }
    }

}
