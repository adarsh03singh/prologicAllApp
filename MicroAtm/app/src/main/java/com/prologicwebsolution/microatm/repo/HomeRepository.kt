package com.prologicwebsolution.microatm.repo

import android.app.Application
import com.prologicwebsolution.microatm.data.aepsBankList.BalanceReq
import com.prologicwebsolution.microatm.data.aepsBankList.BalanceRes
import com.prologicwebsolution.microatm.data.aepsBankList.BankReqModel
import com.prologicwebsolution.microatm.data.aepsBankList.BankResModel
import com.prologicwebsolution.microatm.database.MyDatabase

import com.prologicwebsolution.microatm.network2.RetrofitClient

import retrofit2.Response

class HomeRepository(application: Application) {
    val database: MyDatabase

    init {
        database = MyDatabase.getInstance(application.applicationContext)
    }

    suspend fun getAepsBankList(request: BankReqModel): Response<BankResModel> {
        val response = RetrofitClient.getClientAeps()?.GetAepsBankListApi(request)
        return response!!
    }

    suspend fun getAepsTnx(request: BalanceReq): Response<BalanceRes> {
        val response = RetrofitClient.getClientAeps()?.getAepsTnx(request)
        return response!!
    }
}