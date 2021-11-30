package com.prologicwebsolution.microatm


import androidx.lifecycle.LiveData
import androidx.room.*
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity


// DAO CAN BE ABSTRACT CLASS OR INTERFACE
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)
}

@Dao
abstract class GetTransactionDetailDao: BaseDao<GetTransactionEntity> {
    @Query("DELETE FROM transaction_detail_table")
    abstract fun delete()

    @Query("SELECT * from transaction_detail_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<GetTransactionEntity>
}

@Dao
abstract class WalletBalncDao: BaseDao<WalletEntity> {
    @Query("DELETE FROM wallet_table")
    abstract fun delete()

    @Query("SELECT * from wallet_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<WalletEntity>

    @Query("SELECT * from wallet_table")
    @JvmSuppressWildcards
    abstract fun getbyLiveAllData(): LiveData<List<WalletEntity>>
}

@Dao
abstract class PayoutListDao: BaseDao< PayoutListEntity> {
    @Query("DELETE FROM payout_list_table")
    abstract fun delete()

    @Query("SELECT * from payout_list_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<PayoutListEntity>

    @Query("SELECT * from payout_list_table")
    @JvmSuppressWildcards
    abstract fun getbyLiveAllData(): LiveData<List<PayoutListEntity>>
}

@Dao
abstract class PayoutWithdrawlDao: BaseDao< PayoutDataEntity> {
    @Query("DELETE FROM payout_WithdrawData_table")
    abstract fun delete()

    @Query("SELECT * from payout_WithdrawData_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<PayoutDataEntity>

    @Query("SELECT * from payout_WithdrawData_table")
    @JvmSuppressWildcards
    abstract fun getbyLiveAllData(): LiveData<List<PayoutDataEntity>>
}


