package com.prologic.strains.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.CheckOutBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.CheckOutViewModel
import kotlinx.android.synthetic.main.check_out.*


class CheckOut : Fragment() {
    lateinit var viewModel: CheckOutViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var binding: CheckOutBinding =
            DataBindingUtil.inflate(inflater, R.layout.check_out, container, false)
        viewModel = ViewModelProvider(this).get(CheckOutViewModel::class.java)
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
        (activity as MainActivity).setHeader("Checkout", true, false, true, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHeader()
        viewModel.product_amt = requireArguments().getDouble("product_amt", 0.0)
        viewModel.product_qtn = requireArguments().getInt("product_qtn", 0)
        viewModel.setView()
        discountLay.visibility = View.GONE
        viewModel.isLoaderVisible.observeForever { it ->
            if (it)
                DialogLoading.show(requireActivity())
            else
                DialogLoading.hide()
        }
        viewModel.isCouponApplied.observeForever {
            if (it) {
                applyCancel.setBackgroundResource(R.drawable.circle_red_button)
                applyCancel.text = "Cancel"
                couponCodeEt.isEnabled = false
                coupon_title.visibility = View.VISIBLE
            } else {
                applyCancel.setBackgroundResource(R.drawable.circle_green_button)
                applyCancel.text = "Apply"
                couponCodeEt.isEnabled = true
                coupon_title.visibility = View.GONE
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { it ->
            if (it.isNotEmpty())
                AlertError.show(requireActivity(), it)

        })
        viewModel.orderResponse.observeForever {
            if (it.order_key.isEmpty()) {
                AlertError.show(requireActivity(), "Order not created")
            } else {
                deleteCardData()
                var msg = ""
                msg += "Order Successfully Created<br>"
                msg += "Id : " + it.id + "<br>"
                msg += "Order Id : " + it.order_key
                SuccessAlert.showHtml(requireActivity(), msg, object : OnDialogCloseListener {
                    override fun onClick() {
                        clearFragments()
                        replaceFragment(HomePage())
                    }
                })
            }
        }

        applyCancel.setOnClickListener {
            if (applyCancel.text.toString().equals("Cancel")) {
                viewModel.couponItem.value = null
            } else
                if (couponCodeEt.text.length == 0) {
                    couponCodeEt.setError("Coupon Code can't be blank!")
                    couponCodeEt.requestFocus()
                } else {
                    viewModel.getCoupon()
                }
        }
        onBackResult()
        create_order.setOnClickListener {
            if (viewModel.checkValidation()) {
                viewModel.createOrderApi()
            }
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