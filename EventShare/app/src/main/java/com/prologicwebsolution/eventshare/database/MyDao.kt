package com.prologicwebsolution.microatm



import androidx.room.*
import com.prologicwebsolution.eventshare.data.loginData.UserLoginDataEntity


// DAO CAN BE ABSTRACT CLASS OR INTERFACE
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)
}

@Dao
abstract class GetUserDataDao: BaseDao<UserLoginDataEntity> {
    @Query("DELETE FROM user_data_table")
    abstract fun delete()

    @Query("SELECT * from user_data_table")
    @JvmSuppressWildcards
    abstract fun getAllData(): List<UserLoginDataEntity>
}

