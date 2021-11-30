package com.prologicwebsolution.microatm.ui.withdrawl

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.databinding.FragmentWithdrawlBinding
import kotlinx.android.synthetic.main.fragment_withdrawl.*


class WithdrawlFragment : Fragment() {

    lateinit var viewmodel: WithdrawlViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_withdrawl, container, false)
        val binding: FragmentWithdrawlBinding = FragmentWithdrawlBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(WithdrawlViewModel::class.java)
        binding.withdrawlViewModel = viewmodel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        impsLayout.setOnClickListener {
            impsLayout.setBackgroundResource(R.drawable.withdraw_amount_blue_background)
            neftLayout.setBackgroundResource(R.drawable.withdraw_amount_gray_background)
            tvNeft.setTextColor(getResources().getColor(R.color.black))
            tvImps.setTextColor(getResources().getColor(R.color.blue))
//            neftradiobutton.buttonTintList = myColorStateList
//            impsRadioButton.buttonTintList = myColorStateList
            neftradiobutton.isChecked = false
            impsRadioButton.isChecked = true

            if(impsRadioButton.isChecked){
                viewmodel.modeypeValue.value = "NPN"
            }


        }

        neftLayout.setOnClickListener {
            neftLayout.setBackgroundResource(R.drawable.withdraw_amount_blue_background)
            impsLayout.setBackgroundResource(R.drawable.withdraw_amount_gray_background)
            tvNeft.setTextColor(getResources().getColor(R.color.blue))
            tvImps.setTextColor(getResources().getColor(R.color.black))
//            neftradiobutton.buttonTintList = myColorStateList
//            impsRadioButton.buttonTintList = myColorStateList
            impsRadioButton.isChecked = false
            neftradiobutton.isChecked = true

            if(neftradiobutton.isChecked){
                viewmodel.modeypeValue.value = "LPN"
            }

        }

    }
}