package com.prologic.laughinggoat.view.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prologic.laughinggoat.R
import com.prologic.laughinggoat.databinding.CheckOutBinding
import com.prologic.laughinggoat.model.create_order.OrderResponse
import com.prologic.laughinggoat.utils.*
import com.prologic.laughinggoat.view.activity.MainActivity
import com.prologic.laughinggoat.view.activity.SquareActivity
import com.prologic.laughinggoat.viewmodel.CheckOutViewModel
import kotlinx.android.synthetic.main.check_out.*
import sqip.Callback
import sqip.CardEntry
import sqip.CardEntryActivityResult


class CheckOut : Fragment() {
    lateinit var viewModel: CheckOutViewModel
    lateinit var binding: CheckOutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.check_out, container, false)
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
        viewModel.setView(binding)
        discountLay.visibility = View.GONE
        viewModel.isLoaderVisible.observeForever { it ->
            if (it)
                DialogLoading.show(requireActivity())
            else
                DialogLoading.hide()

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
                val payment_amount = viewModel.total_amt * 100
                val intent = Intent(requireActivity(), SquareActivity::class.java)
                intent.putExtra("payment_amount", payment_amount.toInt())
                startActivityForResult(intent, 1001)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
               // showToast("Successfully Payment")
                viewModel.createOrderApi()
            } else {
                AlertError.show(requireActivity(), "Payment Cancelled")
            }
        }
    }

}