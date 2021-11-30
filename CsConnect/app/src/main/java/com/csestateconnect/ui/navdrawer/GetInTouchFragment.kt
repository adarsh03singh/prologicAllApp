package com.csestateconnect.ui.navdrawer


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.csestateconnect.R
import com.csestateconnect.databinding.FragmentGetInTouchBinding
import com.csestateconnect.databinding.FragmentMyLeadsBinding
import com.csestateconnect.viewmodel.HomeViewModel
import com.csestateconnect.viewmodel.LeadsViewModel
import kotlinx.android.synthetic.main.app_bar_home_acticity.*
import kotlinx.android.synthetic.main.fragment_get_in_touch.*

/**
 * A simple [Fragment] subclass.
 */
class GetInTouchFragment : Fragment() {
    lateinit var viewmodel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_in_touch, container, false)

        val binding: FragmentGetInTouchBinding = FragmentGetInTouchBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.activity!!).get(HomeViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        issue_down_arrow.setOnClickListener{
            issue_down_arrow.visibility = View.GONE
            issues_layout.visibility = View.VISIBLE
            issue_up_arrow.visibility = View.VISIBLE
        }
        issue_up_arrow.setOnClickListener{
            issue_down_arrow.visibility = View.VISIBLE
            issues_layout.visibility = View.GONE
            issue_up_arrow.visibility = View.GONE
        }

        view_tickets_button.setOnClickListener {
            findNavController().navigate(R.id.action_nav_get_touch_to_concernTicketsListFragment)
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
