package com.prologicwebsolution.microatm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prologicwebsolution.eventshare.data.loginData.UserLoginDataEntity
import com.prologicwebsolution.microatm.GetUserDataDao


@Database(entities = [UserLoginDataEntity::class],
    version = 2, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract val getUserDataDao: GetUserDataDao


    companion object{
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
