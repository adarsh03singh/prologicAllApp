package com.csestateconnect

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.d(TAG, "Vishal: Working 3")

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data

        Log.d(TAG, "applinkintent: $appLinkIntent appLinkAction: $appLinkAction appLinkData: $appLinkData")


        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
