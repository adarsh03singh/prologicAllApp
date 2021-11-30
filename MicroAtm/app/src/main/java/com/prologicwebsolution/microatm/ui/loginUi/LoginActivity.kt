package com.prologicwebsolution.microatm.ui.loginUi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.databinding.ActivityLoginBinding
import com.prologicwebsolution.microatm.util.MyProgressDialog.Companion.dismiss
import com.prologicwebsolution.microatm.util.MyProgressDialog.Companion.show

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this

    }

}