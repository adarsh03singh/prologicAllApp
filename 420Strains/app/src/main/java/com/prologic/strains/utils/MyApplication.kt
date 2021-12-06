package com.prologic.strains.utils

import android.app.Application

class MyApplication : Application() {

    companion object {
        private lateinit var instance: MyApplication
        fun getMyApplication(): Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}