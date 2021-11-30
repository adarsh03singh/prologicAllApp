package com.prologic.laughinggoat.view.activity

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.ActivityLoginBinding
import com.prologic.laughinggoat.utils.DialogLoading
import com.prologic.laughinggoat.utils.showToastShort
import com.prologic.laughinggoat.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        viewModel.set(this)
        viewModel.isLoaderVisible.observe(this, Observer { it ->
            if (it)
                DialogLoading.show(this)
            else
                DialogLoading.hide()
        })
        viewModel.errorMessage.observe(this, Observer { it ->
            if (it.isNotEmpty())
                showToastShort(it)
        })
        password_show.setOnClickListener {
            if (viewModel.password_show.value.equals("Show")) {
                viewModel.password_show.value = "Hide"
            } else
                viewModel.password_show.value = "Show"
        }

        viewModel.password_show.observeForever {
            if (it.equals("Hide")) {
                input_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                input_password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            input_password.setSelection(input_password.length())
            input_password.typeface = ResourcesCompat.getFont(this, R.font.medium)
        }
    }

    fun login(view: View) {
        if (!Patterns.EMAIL_ADDRESS.matcher(input_email.text.toString()).matches()) {
            input_email.setError("Email can't be blank or Invalid!")
            input_email.requestFocus()
        } else if (input_password.text.toString().length < 5) {
            input_password.setError("Password minimum 5 digit")
            input_password.requestFocus()
        } else {
            viewModel.login()
        }
    }

    fun register(view: View) {
        if (input_first_name.text.toString().isEmpty()) {
            input_first_name.setError("First Name can't be blank!")
            input_first_name.requestFocus()
        } else if (input_last_name.text.toString().isEmpty()) {
            input_last_name.setError("Last Name can't be blank!")
            input_last_name.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(input_email.text.toString()).matches()) {
            input_email.setError("Email can't be blank or Invalid!")
            input_email.requestFocus()
        } else if (input_password.text.toString().length < 5) {
            input_password.setError("Password minimum 5 digit")
            input_password.requestFocus()
        } else {
            viewModel.register()
        }
    }

    override fun onBackPressed() {
        if (viewModel.onBackPressed())
            super.onBackPressed()
    }

    fun backClick(v: View) {
        if (viewModel.onBackPressed())
            super.onBackPressed()
    }
}

