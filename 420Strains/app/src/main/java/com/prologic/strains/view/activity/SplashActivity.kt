package com.prologic.strains.view.activity


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.BuildConfig
import com.prologic.strains.R

import com.prologic.strains.databinding.SplashActivityBinding
import com.prologic.strains.utils.SharedPreference
import com.prologic.strains.utils.UrlValue
import com.prologic.strains.utils.fragmentActivity

import com.prologic.strains.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.splash_activity.*
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity() {
    val sharedPreference = SharedPreference();
    lateinit var binding: SplashActivityBinding
    lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        version_name.text = "VERSION NAME : " + BuildConfig.VERSION_NAME

        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(1))
            withContext(Dispatchers.Main) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
/*                val intent = Intent(applicationContext, MyWebView::class.java)
                intent.putExtra("title", "Contact Us")
                intent.putExtra("url", "https://www.24x7wpsupport.com/")
                startActivity(intent)
                finish()*/
            }
        }
    }


}

