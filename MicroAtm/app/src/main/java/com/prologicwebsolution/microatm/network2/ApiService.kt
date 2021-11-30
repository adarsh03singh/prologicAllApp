package com.prologicwebsolution.microatm.network2

import com.prologicwebsolution.microatm.data.aepsBankList.BalanceReq
import com.prologicwebsolution.microatm.data.aepsBankList.BalanceRes
import com.prologicwebsolution.microatm.data.aepsBankList.BankReqModel
import com.prologicwebsolution.microatm.data.aepsBankList.BankResModel
import com.prologicwebsolution.microatm.data.loginData.LoginEntity
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.savetransaction.SaveTransactionData
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {

    //Call Login Api here
    @POST("mobi/user_auth.php")
    suspend fun LoginApi(@Body createLoginBody: CreateLoginBody): Response<LoginEntity>

    //Call wallet balance Api
    @POST("mobi/api.php")
    suspend fun WalletBalanceApi(@Body walletBody: WalletBody): Response<WalletEntity>

    @POST("sdk/MobileReq/public/aepssbm")
    suspend fun GetAepsBankListApi(@Body request: BankReqModel): Response<BankResModel>

    @POST("sdk/MobileReq/public/aepssbm")
    suspend fun getAepsTnx(@Body request: BalanceReq): Response<BalanceRes>

    @Headers("Content-Type: application/json")
    @POST("mobi/get_transactions.php")
    suspend fun GetTransactionApi(@Body transactionDetailsBody: TransactionDetailsBody): Response<GetTransactionEntity>

    //call Payout List Api
    @POST("mobi/api.php")
    suspend fun PayoutListApi(@Body payoutListBody: PayoutListBody): Response<PayoutListEntity>

    //call Payout Api
    @POST("mobi/api.php")
    suspend fun PayoutWithdrawApi(@Body payoutWithdrawBody: PayoutWithdrawBody): Response<PayoutDataEntity>

    //call transaction api for Data inserted
    @POST("mobi/save_transactions.php")
    suspend fun SavetransactionApi(@Body saveTransactionApiBody: SaveTransactionApiBody): Response<SaveTransactionData>



}

class CreateLoginBody {
    var username = ""
    var password = ""
}

class WalletBody(
    payout_type: String, user_id: String?
) {
    val payout_type: String = payout_type
    val user_id: String? = user_id
}

class TransactionDetailsBody(
    user_id: String?, start_date: String?, end_date: String?
) {
    val user_id: String? = user_id
    val start_date: String? = start_date
    val end_date: String? = end_date
}

class PayoutListBody(
    payout_type: String, user_id: String?, start_date: String?, end_date: String?
) {
    val payout_type: String = payout_type
    val user_id: String? = user_id
    val start_date: String? = start_date
    val end_date: String? = end_date
}

class PayoutWithdrawBody(
    payout_type: String, user_id: String?, mode: String?, amount: String?
) {
    val payout_type: String = payout_type
    val user_id: String? = user_id
    val mode: String? = mode
    val amount: String? = amount

}

class SaveTransactionApiBody {
    var amount: String = ""
    var bankremarks: String? = ""
    var cardno: String? = ""
    var clientrefid: String? = ""
    var date: String = ""
    var invoicenumber: String? = ""
    var rrn: String? = ""
    var mid: String? = ""
    var refstan: String? = ""
    var statuscode: String? = ""
    var tid: String = ""
    var txnType: String? = ""
    var txnamount: String? = ""
    var user_id: String? = "user_id"


}