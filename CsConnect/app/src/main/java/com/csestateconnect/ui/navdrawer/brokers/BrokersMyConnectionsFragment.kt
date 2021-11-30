package com.csestateconnect.ui.navdrawer.brokers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csestateconnect.R


class BrokersMyConnectionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_brokers_my_connections, container, false)

        return view
    }


}
