package com.prologicwebsolution.eventshare.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.databinding.ActivityLoginBinding
import com.prologicwebsolution.eventshare.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        submitButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}