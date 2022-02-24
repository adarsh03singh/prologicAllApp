package com.prologic.strains.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.prologic.strains.R
import com.prologic.strains.databinding.ShippingBillingBinding
import com.prologic.strains.search_dialog.SearchDialog
import com.prologic.strains.search_dialog.SearchItem
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.ShippingBillingViewModel


import kotlinx.android.synthetic.main.shipping_billing.*


class ShippingBilling : Fragment() {
    lateinit var viewModel: ShippingBillingViewModel
    lateinit var binding: ShippingBillingBinding
    lateinit var searchDialog: SearchDialog
    lateinit var bundle: Bundle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.shipping_billing, container, false)
        viewModel = ViewModelProvider(this).get(ShippingBillingViewModel::class.java)
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
        (activity as MainActivity).setHeader(bundle.getString(title_name)!!, true, false,true,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCountryState()
        bundle = requireArguments()
        val view_type = bundle.getString("result_key")
        setHeader()
        if (view_type.equals("billing"))
            viewModel.view_type.value = View.VISIBLE
        else if (view_type.equals("shipping"))
            viewModel.view_type.value = View.GONE

        viewModel.setValue(bundle)

        add_details.setOnClickListener {
            validation()

        }
        countryId.setOnClickListener {
            searchDialog = SearchDialog(viewModel.getCountry(), requireActivity())
            searchDialog.setHint("Search Country")
            searchDialog.setOnItemClickListItem(object : OnSearchDialogListener {
                override fun onClick(item: SearchItem) {
                    viewModel.countryId.postValue(item.id)
                    viewModel.stateName.postValue("")
                }
            })
        }

        stateName.setOnClickListener {
            searchDialog = SearchDialog(viewModel.getState(), requireActivity())
            searchDialog.setHint("Search State")
            searchDialog.setOnItemClickListItem(object : OnSearchDialogListener {
                override fun onClick(item: SearchItem) {
                    viewModel.stateName.postValue(item.title)
                }
            })
        }

    }


    private fun validation() {
        if (firstName.text.toString().isEmpty()) {
            firstName.setError("Can't be blank")
            firstName.requestFocus()
        } else if (lastName.text.toString().isEmpty()) {
            lastName.setError("Can't be blank")
            lastName.requestFocus()
        } else if (address_1.text.toString().isEmpty()) {
            address_1.setError("Can't be blank")
            address_1.requestFocus()
        } else if (address_2.text.toString().isEmpty()) {
            address_2.setError("Can't be blank")
            address_2.requestFocus()
        } else if (cityName.text.toString().isEmpty()) {
            cityName.setError("Can't be blank")
            cityName.requestFocus()
        } else if (countryId.text.toString().isEmpty()) {
            countryId.setError("Country can't be blank")
            countryId.requestFocus()
        } else if (stateName.text.toString().isEmpty()) {
            stateName.setError("Sate can't be blank")
            stateName.requestFocus()
        } else if (postcode.text.toString().isEmpty()) {
            postcode.setError("Can't be blank")
            postcode.requestFocus()
        } else if (phoneNumber.text.toString().length < 10 && viewModel.view_type.value == View.VISIBLE) {
            phoneNumber.setError("Invalid Phone No")
            phoneNumber.requestFocus()
        } else if (!isEmailValid(emailAddress.text.toString()) && viewModel.view_type.value == View.VISIBLE) {
            emailAddress.setError("Invalid Email Id")
            emailAddress.requestFocus()
        } else {
            setOnBackResult(bundle.getString("result_key")!!, viewModel.getBundle())
        }
    }


}


