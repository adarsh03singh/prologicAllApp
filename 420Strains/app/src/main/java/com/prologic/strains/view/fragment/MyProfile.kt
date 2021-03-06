package com.prologic.strains.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.MyProfileBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.ProfileViewModel
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
        viewModel.registerResponse.observeForever {
            SuccessAlert.showHtml(
                requireActivity(),
                "Your profile has been updated",
                object : OnDialogCloseListener {
                    override fun onClick() {
                        setOnBackResult("profile_update", bundleOf("result" to 1))
                    }
                })
        }
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
            input_password.typeface = ResourcesCompat.getFont(requireActivity(), R.font.normal)
        }

        updateButton.setOnClickListener {
            if (viewModel.update_button.value.equals("Update Profile")) {
                updateProfile()
            } else
                viewModel.update_button.value = "Update Profile"
        }

        viewModel.update_button.observeForever {
            if (it.equals("Edit Profile")) {
                input_first_name.setBackgroundResource(R.drawable.edit_back)
                input_last_name.setBackgroundResource(R.drawable.edit_back)
                input_email.setBackgroundResource(R.drawable.edit_back)
                input_password.setBackgroundResource(R.drawable.edit_back)

                input_first_name.isEnabled = false
                input_last_name.isEnabled = false
                input_email.isEnabled = false
                input_password.isEnabled = false
            } else {
                input_first_name.setBackgroundResource(R.drawable.edit_back)
                input_last_name.setBackgroundResource(R.drawable.edit_back)
                input_email.setBackgroundResource(R.drawable.edit_back)
                input_password.setBackgroundResource(R.drawable.edit_back)
                input_first_name.isEnabled = true
                input_last_name.isEnabled = true
                input_email.isEnabled = true
                input_password.isEnabled = true
            }
        }
        billingView.setOnClickListener {
            val result_key = "billing"
            onBackResult(result_key)
            val fragment = ShippingBilling()
            val bundle = viewModel.getBillingBundle()
            bundle.putString("result_key", result_key)
            bundle.putString(title_name, "Billing Details")
            fragment.arguments = bundle
            addFragment(fragment)
        }
        shippingView.setOnClickListener {
            val result_key = "shipping"
            onBackResult(result_key)
            val fragment = ShippingBilling()
            val bundle = viewModel.getShippingBundle()
            bundle.putString("result_key", result_key)
            bundle.putString(title_name, "Shipping Details")
            fragment.arguments = bundle
            addFragment(fragment)
        }
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


    fun onBackResult(result_key: String) {
        getAppFragmentManager().setFragmentResultListener(
            result_key,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (requestKey.equals("billing")) {
                viewModel.setBillingBundle(bundle)
            } else if (requestKey.equals("shipping")) {
                viewModel.setShippingBundle(bundle)
            }
        }
    }
}

