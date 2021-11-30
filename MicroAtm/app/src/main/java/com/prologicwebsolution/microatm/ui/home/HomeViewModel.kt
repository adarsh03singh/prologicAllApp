package com.prologicwebsolution.microatm.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.snackbar.Snackbar
import com.prologicwebsolution.microatm.repo.HomeRepository
import com.prologicwebsolution.microatm.util.Coroutines
import org.json.JSONObject


@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val homeRepository: HomeRepository

    val requestcode = "aepsBanks"
    val username = "SHANKY"
    val imei = "866532048183953"
    val imsi = "null"
    val mid = "442000227361448"
    val tid = "42200333"


//    val filteredTransactionData = MutableLiveData<GetTransactionEntity?>()

//    var progressBar = MutableLiveData<Int>().apply { postValue(View.GONE) }
//    var buttonEabled = MutableLiveData<Boolean>().apply { postValue(false) }

    init{
        homeRepository = HomeRepository(application)

    }

/*
    fun callTransactionApiAfterCheckInternet(view: View) {
        if (isNetworkConnected(view.context!!)) {
            try {
                getAepsBankListApi(view)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(view.context, "No internet found.", Toast.LENGTH_LONG).show()
        }
    }
*/


/*
    fun getAepsBankListApi(view: View) {

        var response: retrofit2.Response<AepsBankListEntity>?
        Coroutines.io {

            response = homeRepository.GetAepsBanklist(requestcode,username,imei,imsi,mid,tid)

            if (response!!.isSuccessful) {

                Coroutines.main {
                    if (!response?.body()?.status!!.equals(true)) {

                       Log.e("data",response.toString() )
                    } else {
                        Log.e("data",response.toString() )
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

                }
            }
        }

    }
*/

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}