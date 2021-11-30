package com.prologicwebsolution.microatm.repo

import android.app.Application
import com.prologicwebsolution.microatm.data.savetransaction.SaveTransactionData
import com.prologicwebsolution.microatm.network2.RetrofitClient
import com.prologicwebsolution.microatm.network2.SaveTransactionApiBody
import retrofit2.Response

class ConnectMicroAtmRepository
    (application: Application) {

   suspend fun callSaveTransactionApi(request: SaveTransactionApiBody): Response<SaveTransactionData>? {
       val response = RetrofitClient.getClient()?.SavetransactionApi(request)

        return response
    }

}