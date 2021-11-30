package com.prologicwebsolution.microatm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.prologicwebsolution.microatm.R

class WalkthroughNumberFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walkthrough_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttongetOtp = view.findViewById<Button>(R.id.buttonGetOtp)
        buttongetOtp.setOnClickListener {
            view.findNavController().navigate(R.id.action_walkthroughNumberFragment_to_walkthroughOtpFragment)
        }
    }
}