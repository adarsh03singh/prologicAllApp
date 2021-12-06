package com.prologic.strains.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prologic.strains.utils.getMyApplication


@Database(entities = [ProductRoom::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun productRoomDao(): ProductRoomDao

    companion object {

        private var instance: AppRoomDatabase? = null

        fun getRoomDatabase(): AppRoomDatabase {

            if (instance == null) {
                synchronized(this) {
                    val application = getMyApplication()
                    instance = Room
                        .databaseBuilder(application, AppRoomDatabase::class.java, "prologic")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance!!
        }
    }
}