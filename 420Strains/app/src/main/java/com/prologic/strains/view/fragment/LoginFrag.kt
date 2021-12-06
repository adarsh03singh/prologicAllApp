package com.prologic.strains.view.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.LoginFragBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.LoginActivity
import com.prologic.strains.viewmodel.LoginFragViewModel
import kotlinx.android.synthetic.main.login_frag.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class LoginFrag : Fragment() {
    lateinit var viewModel: LoginFragViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(LoginFragViewModel::class.java)
        val binding: LoginFragBinding =
            DataBindingUtil.inflate(inflater, R.layout.login_frag, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        viewModel.userResult.observeForever {
            (requireActivity() as LoginActivity).setResult()
        }
        viewModel.isLoaderVisible.observeForever {
            if (it)
                DialogLoading.show(requireActivity())
            else
                DialogLoading.hide()
        }
        viewModel.errorMessage.observeForever {
            if (it.isNotEmpty())
                showToastShort(it)
        }
        password_show.setOnClickListener {
            if (viewModel.password_show.value.equals("Show")) {
                viewModel.password_show.value = "Hide"
            } else
                viewModel.password_show.value = "Show"
        }

        viewModel.password_show.observeForever {
            showHidePassword(it, input_password)
        }
        login.setOnClickListener { login() }
        register.setOnClickListener {
            setButtonEnabled(it)
            addFragment(RegisterFrag())
        }
        back.setOnClickListener {
            (requireActivity() as LoginActivity).onBackPressed()
        }
        onBackResult()
    }

    fun showHidePassword(it: String, editText: EditText) {
        if (it.equals("Hide" ,true)) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.length())
        editText.typeface = ResourcesCompat.getFont(requireContext(), R.font.medium)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
    }

    fun login() {
        if (!isEmailValid(input_email.text.toString())) {
            input_email.setError("Email can't be blank or Invalid!")
            input_email.requestFocus()
        } else if (input_password.text.toString().length < 5) {
            input_password.setError("Password minimum 5 digit")
            input_password.requestFocus()
        } else {
            viewModel.login()
        }
    }

    fun onBackResult() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            "register",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            viewModel.input_email.value = bundle.getString("email")
            viewModel.input_password.value = bundle.getString("password")

            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.MILLISECONDS.toMillis(500))
                withContext(Dispatchers.Main) {
                    viewModel.login()
                }
            }

        }
    }

}

