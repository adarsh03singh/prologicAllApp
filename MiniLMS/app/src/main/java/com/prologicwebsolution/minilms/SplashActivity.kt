package com.prologicwebsolution.minilms

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animateSplashLogo()
        scheduleGoToMainActivity()
    }

    private fun scheduleGoToMainActivity() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                goToMainActivity()
            }
        }, 3000)
    }


    private fun animateSplashLogo() {
        val splash_logo: ImageView = findViewById(R.id.minilms_logoId)
        val poweredTxt = findViewById<TextView>(R.id.poweredByTxt)
        val companyNameTxt = findViewById<TextView>(R.id.companyNametxt)
        val splash_cslogo_animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.splash_logo_anim)
        splash_cslogo_animation.interpolator = LinearInterpolator()
        splash_cslogo_animation.repeatCount = Animation.INFINITE
        splash_cslogo_animation.duration = 2000
        splash_logo.startAnimation(splash_cslogo_animation)
        poweredTxt.startAnimation(splash_cslogo_animation)
        companyNameTxt.startAnimation(splash_cslogo_animation)
    }

    fun goToMainActivity(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}