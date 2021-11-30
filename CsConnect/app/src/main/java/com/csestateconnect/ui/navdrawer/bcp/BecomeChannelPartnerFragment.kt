package com.csestateconnect.ui.navdrawer.bcp


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

import com.csestateconnect.R
import kotlinx.android.synthetic.main.app_bar_home_acticity.*

/**
 * A simple [Fragment] subclass.
 */
class BecomeChannelPartnerFragment : Fragment() {

    lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_become_channel_partner, container, false)

        button = view.findViewById(R.id.get_start_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        button.setOnClickListener {
//            val intent = Intent(context, VerifyChannelPartnersActivity::class.java)
//            startActivity(intent)
//        }

        button.setOnClickListener {
            findNavController().navigate(R.id.action_nav_bcp_to_personalInfoFragment)
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        if(item?.itemId == android.R.id.home){
//            finish()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}
