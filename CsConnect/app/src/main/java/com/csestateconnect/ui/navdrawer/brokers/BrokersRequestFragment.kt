package com.csestateconnect.ui.navdrawer.brokers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csestateconnect.R


/**
 * A simple [Fragment] subclass.
 */
class BrokersRequestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_brokers_request, container, false)

        return view
    }


}
