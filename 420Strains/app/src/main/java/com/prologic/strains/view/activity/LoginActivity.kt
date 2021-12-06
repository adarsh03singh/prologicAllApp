package com.prologic.strains.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.ActivityLoginBinding
import com.prologic.strains.utils.fragmentActivity
import com.prologic.strains.utils.getAppFragmentManager
import com.prologic.strains.utils.hideSoftKeyBoard
import com.prologic.strains.utils.replaceFragment
import com.prologic.strains.view.fragment.LoginFrag
import com.prologic.strains.viewmodel.LoginViewModel


class LoginActivity : FragmentActivity() {
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()

        fragmentActivity = this
        if (savedInstanceState == null) {
            replaceFragment(LoginFrag())
        }

    }

    fun setResult() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        hideSoftKeyBoard(this)
        val count = getAppFragmentManager().backStackEntryCount
        if (count == 0) {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        } else {
            getAppFragmentManager().popBackStack()
        }
    }
}

