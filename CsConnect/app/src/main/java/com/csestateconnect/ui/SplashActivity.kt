package com.csestateconnect.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.csestateconnect.MainActivity
import com.csestateconnect.R
import com.csestateconnect.databinding.ActivitySplashBinding
import com.csestateconnect.databinding.ActivityWalkThroughBinding
import com.csestateconnect.ui.home.HomeActivity
import com.csestateconnect.utils.ConnectivityReceiver
import com.csestateconnect.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception
import java.util.*

class SplashActivity : AppCompatActivity() {


    var constraintLayout: ConstraintLayout? = null
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData = appLinkIntent.data

        Log.d(TAG, "applinkintent: $appLinkIntent appLinkAction: $appLinkAction appLinkData: $appLinkData")


        intent.extras?.let {
            if (it.get("click_action") == "MAINACTIVITY"){
                val new_intent = Intent(this, MainActivity::class.java)

                Log.d(TAG, "Vishal: Working")

                for (key in it.keySet()) {
                    val value = intent.extras?.get(key)
                    Log.d(TAG, "Vishal: Working 1")
                    new_intent.putExtra(key,value.toString())
                }

                startActivity(new_intent)
                finish()
                return
            }
        }

        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_splash
        )
         viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        // Animating font and image to fade in on splash screen.
        animate_splash_screen()

    }

    private fun animate_splash_screen() {
        val animation = AnimationUtils.loadAnimation(this,
            R.anim.splash_fade_in
        )
        splash_main_layout.startAnimation(animation)
        footer_layout.startAnimation(animation)
        scheduleGoToWalkThroughActivity()
    }

    private fun scheduleGoToWalkThroughActivity() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val profile_updated = viewModel.profile_UserName.value
               val number = viewModel.userNumber.value
                if(number == null){
                    val intent = Intent(applicationContext, WalkThroughActivity::class.java)
                    startActivity(intent)
                }else {

                if(profile_updated == true) {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(applicationContext, GenerateOtpActivity::class.java)
                    startActivity(intent)
                }
                }

                overridePendingTransition(
                    R.anim.come_in_from_right,
                    R.anim.fade_out
                )
            finish()
            }
        }, 2000)
    }


    companion object {
        private const val TAG = "SplashActivity"
    }


}
