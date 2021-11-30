package com.prologicwebsolution.microatm.repo

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import com.prologicwebsolution.microatm.database.MyDatabase
import com.prologicwebsolution.microatm.network2.PayoutWithdrawBody
import com.prologicwebsolution.microatm.network2.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class PayoutRepository
    (application: Application) {

    val database: MyDatabase

    val sharedPref: SharedPreferences = application.getSharedPreferences("PREFERENCE_NAME", 0)
    val userId =sharedPref.getString("user_id", "")


    init {
        database = MyDatabase.getInstance(application.applicationContext)
    }


    suspend fun callPaymwntWithdrawApi(mode: String, amount: String): Response<PayoutDataEntity>? {
        val responseData = RetrofitClient.getClient()?.PayoutWithdrawApi(PayoutWithdrawBody("credit_wallet_balance",userId,mode,amount))

        when (responseData!!.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.payoutWithdrawlDao.delete()
                    database.payoutWithdrawlDao.insert(responseData.body()!!)

                }
            }
        }

        return responseData
    }


    fun getWalletBalnsFromDatabase(): LiveData<List<WalletEntity>> {
        return database.walletBalncDao.getbyLiveAllData()
    }

}