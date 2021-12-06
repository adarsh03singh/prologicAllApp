package com.prologic.strains.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.prologic.strains.R
import com.prologic.strains.databinding.CartItemBinding
import com.prologic.strains.utils.*
import com.prologic.strains.view.activity.LoginActivity
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.CartItemViewModel
import kotlinx.android.synthetic.main.cart_item.*


class CartItem : Fragment() {
    lateinit var viewModel: CartItemViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: CartItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.cart_item, container, false)
        viewModel = ViewModelProvider(this).get(CartItemViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()

        checkout.setOnClickListener {
            setButtonEnabled(checkout)
            if (viewModel.product_amt > 0) {

                val user_data = viewModel.sharedPreference.getUser()
                if (user_data == null) {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivityForResult(intent, 1002)
                } else {
                    val bundle = Bundle()
                    bundle.putDouble("product_amt", viewModel.product_amt)
                    bundle.putInt("product_qtn", viewModel.product_qtn)
                    val fragment = CheckOut()
                    fragment.arguments = bundle
                    addFragmentFadeAnim(fragment)
                }
            } else {
                AlertError.show(requireActivity(), "Please add products")
            }
        }

        shopNow.setOnClickListener {
            clearFragments()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002) {
            fragmentActivity = requireActivity()
            if (resultCode == FragmentActivity.RESULT_OK) {
                (activity as MainActivity).setValue()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setHeader()
        }
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader("My Cart", true, false, true, false)
    }

}


