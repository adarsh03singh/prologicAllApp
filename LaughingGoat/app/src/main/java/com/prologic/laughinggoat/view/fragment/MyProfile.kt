package com.prologic.laughinggoat.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import kotlinx.android.synthetic.main.activity_login.input_email
import kotlinx.android.synthetic.main.activity_login.input_first_name
import kotlinx.android.synthetic.main.activity_login.input_last_name
import kotlinx.android.synthetic.main.activity_login.input_password

import androidx.core.content.res.ResourcesCompat
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.MyProfileBinding
import com.prologic.laughinggoat.utils.*
import com.prologic.laughinggoat.view.activity.MainActivity
import com.prologic.laughinggoat.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.my_profile.*


class MyProfile : Fragment() {
    lateinit var viewModel: ProfileViewModel
    lateinit var binding: MyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_profile, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
        }
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader("My Profile", true, false, true, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        viewModel.setView(requireActivity())
        viewModel.isLoaderVisible.observe(viewLifecycleOwner, Observer { it ->
            if (it == 1)
                DialogLoading.show(requireActivity())
            else if (it == 2)
                DialogLoading.hide()
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { it ->
            if (it.isNotEmpty())
                showToast(it)
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
            input_password.typeface = ResourcesCompat.getFont(requireActivity(), R.font.medium)
        }

        updateButton.setOnClickListener {
            if (viewModel.update_button.value.equals("Update Profile")) {
                updateProfile()
            } else
                viewModel.update_button.value = "Update Profile"
        }

        viewModel.update_button.observeForever {
            if (it.equals("Edit Profile")) {
                input_first_name.setBackgroundResource(R.color.trans)
                input_last_name.setBackgroundResource(R.color.trans)
                input_email.setBackgroundResource(R.color.trans)
                input_password.setBackgroundResource(R.color.trans)

                input_first_name.isEnabled = false
                input_last_name.isEnabled = false
                input_email.isEnabled = false
                input_password.isEnabled = false
            } else {
                input_first_name.setBackgroundResource(R.drawable.edt_bg_selector)
                input_last_name.setBackgroundResource(R.drawable.edt_bg_selector)
                input_email.setBackgroundResource(R.drawable.edt_bg_selector)
                input_password.setBackgroundResource(R.drawable.edt_bg_selector)
                input_first_name.isEnabled = true
                input_last_name.isEnabled = true
                input_email.isEnabled = true
                input_password.isEnabled = true
            }
        }
        onBackResult()
    }


    fun updateProfile() {
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
            viewModel.updateProfile()
        }
    }


    fun onBackResult() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            on_back_key,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (requestKey.equals(on_back_key)) {
                val bundle = bundle.getBundle(billing_shipping)!!
                val view_type = bundle.getString(view_type)
                if (view_type.equals(billing)) {
                    viewModel.setBillingBundle(bundle)
                } else if (view_type.equals(shipping)) {
                    viewModel.setShippingBundle(bundle)
                }
            }
        }
    }
}

