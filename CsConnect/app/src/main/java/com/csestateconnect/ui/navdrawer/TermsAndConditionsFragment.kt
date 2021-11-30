package com.csestateconnect.ui.navdrawer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.csestateconnect.R
import kotlinx.android.synthetic.main.app_bar_home_acticity.*

/**
 * A simple [Fragment] subclass.
 */
class TermsAndConditionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false)

//        setSupportActionBar(toolbar)
//        getSupportActionBar()!!.setTitle("Terms and Conditions")
//
//
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        return view
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        if(item?.itemId == android.R.id.home){
//            finish()
//        }
//        return super.onOptionsItemSelected(item)
//
//    }
}
