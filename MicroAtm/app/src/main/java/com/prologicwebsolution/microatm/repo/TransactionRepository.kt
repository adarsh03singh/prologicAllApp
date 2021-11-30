package com.prologicwebsolution.microatm.repo

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import com.prologicwebsolution.microatm.database.MyDatabase
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.network2.TransactionDetailsBody
import com.prologicwebsolution.microatm.network2.WalletBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TransactionRepository
    (application: Application) {

    val database: MyDatabase

    val sharedPref: SharedPreferences = application.getSharedPreferences("PREFERENCE_NAME", 0)
    val userId =sharedPref.getString("user_id", "")


    init {
        database = MyDatabase.getInstance(application.applicationContext)
    }


    suspend fun getTransactionDetailApi( startDate: String, lastDate: String): Response<GetTransactionEntity>? {

        val responseData = RetrofitClient.getClient()?.GetTransactionApi(TransactionDetailsBody(userId,startDate, lastDate))

        when (responseData!!.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.getTransactionDetailDao.delete()
                    database.getTransactionDetailDao.insert(responseData.body()!!)

                    Log.i("tag whole response", responseData.body().toString())

                }
            }
        }

        return responseData
    }

    suspend fun callWalletBalnsApi(payout_type: String ): Response<WalletEntity>? {
        val response = RetrofitClient.getClient()?.WalletBalanceApi(WalletBody(payout_type, userId))
        when (response!!.isSuccessful) {
            true -> {
                withContext(Dispatchers.IO) {
                    database.walletBalncDao.delete()
                    database.walletBalncDao.insert(response.body()!!)


                }
            }
        }
        return response
    }

    fun getWalletBalnsFromDatabase(): LiveData<List<WalletEntity>> {
        return database.walletBalncDao.getbyLiveAllData()
    }

    fun getWalletBalnseFromDatabase(): List<WalletEntity> {
        return database.walletBalncDao.getAllData()
    }

}