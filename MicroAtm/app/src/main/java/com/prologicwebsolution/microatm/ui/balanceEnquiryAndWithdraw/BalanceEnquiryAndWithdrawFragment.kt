package com.prologicwebsolution.microatm.ui.balanceEnquiryAndWithdraw

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.adapter.BalanceEnquiryAdaper
import com.prologicwebsolution.microatm.databinding.FragmentBalanceEnquiryAndWithdrawBinding
import com.prologicwebsolution.microatm.ui.MainActivity

import com.prologicwebsolution.microatm.ui.commission.CommissionViewModel
import com.prologicwebsolution.microatm.util.shooterFragment
import kotlinx.android.synthetic.main.fragment_balance_enquiry_and_withdraw.*
import kotlinx.android.synthetic.main.fragment_withdrawl_status.*


class BalanceEnquiryAndWithdrawFragment : Fragment() {

    lateinit var viewmodel: BalanceEnquiryAndWithdrawViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_balance_enquiry_and_withdraw, container, false)
        val binding: FragmentBalanceEnquiryAndWithdrawBinding = FragmentBalanceEnquiryAndWithdrawBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(BalanceEnquiryAndWithdrawViewModel::class.java)
        binding.balanceEnquiryANDWithdrawViewModel = viewmodel
        binding.lifecycleOwner = this

        return view
    }


        @SuppressLint("WrongConstant")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setHeader()
            //adding a layoutmanager
            balanceEnquiry_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)


            //crating an arraylist to store users using the data class user
            val users = ArrayList<User>()
            //adding some dummy data to the list
            users.add(BalanceEnquiryAndWithdrawFragment.User("Realme narzo"))
            users.add(BalanceEnquiryAndWithdrawFragment.User("kdl 766767887"))
            users.add(BalanceEnquiryAndWithdrawFragment.User("hshnshjbdjcjdsnhkdch"))
            users.add(BalanceEnquiryAndWithdrawFragment.User("Moto g 876869"))

            //creating our adapter
            val adapter = BalanceEnquiryAdaper(users)

            //now adding the adapter to recyclerview
            balanceEnquiry_recycler_view.adapter = adapter

        }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(true,true,"Balance Enquiry")
    }
        // //creating our Model
        data class User(val name: String)
}