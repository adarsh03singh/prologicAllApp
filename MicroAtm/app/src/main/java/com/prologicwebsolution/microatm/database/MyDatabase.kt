package com.prologicwebsolution.microatm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prologicwebsolution.microatm.*
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.payoutWithdrawl.PayoutDataEntity
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.data.wallet.WalletEntity


@Database(
    entities = [GetTransactionEntity::class, WalletEntity::class, PayoutListEntity::class, PayoutDataEntity::class],
    version = 8,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {

    abstract val getTransactionDetailDao: GetTransactionDetailDao
    abstract val walletBalncDao: WalletBalncDao
    abstract val payoutListDao: PayoutListDao
    abstract val payoutWithdrawlDao: PayoutWithdrawlDao


    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "database_name"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}