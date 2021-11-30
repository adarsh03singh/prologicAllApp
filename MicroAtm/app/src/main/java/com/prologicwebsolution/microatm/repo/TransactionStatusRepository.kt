package com.prologicwebsolution.microatm.repo

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import com.prologicwebsolution.microatm.database.MyDatabase
import com.prologicwebsolution.microatm.network2.PayoutListBody
import com.prologicwebsolution.microatm.network2.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TransactionStatusRepository
    (application: Application) {

    val database: MyDatabase

    val sharedPref: SharedPreferences = application.getSharedPreferences("PREFERENCE_NAME", 0)
    val userId =sharedPref.getString("user_id", "")


    init {
        database = MyDatabase.getInstance(application.applicationContext)
    }


    suspend fun callPaymwntListApi( startDate: String, lastDate: String): Response<PayoutListEntity>? {

        val responseData = RetrofitClient.getClient()?.PayoutListApi(PayoutListBody("get_payout_transactions",userId!!,startDate, lastDate))

        when (responseData!!.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.payoutListDao.delete()
                    database.payoutListDao.insert(responseData.body()!!)

                }
            }
        }

        return responseData
    }


    fun getWalletBalnsFromDatabase(): LiveData<List<WalletEntity>> {
        return database.walletBalncDao.getbyLiveAllData()
    }
}