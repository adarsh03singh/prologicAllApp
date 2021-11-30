package com.csestateconnect.ui.home.deal_Frags

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.DealsAdapter
import com.csestateconnect.databinding.FragmentDealFrag5Binding
import com.csestateconnect.db.data.deals.ListOfDeals
import com.csestateconnect.viewmodel.DealsViewModel
import kotlinx.android.synthetic.main.fragment_deal_frag5.*
import kotlinx.android.synthetic.main.fragment_deal_frag5.view.*
import kotlinx.android.synthetic.main.fragment_my_leads.*

class DealFrag5 : Fragment() {

    lateinit var viewModel: DealsViewModel
    var dealsAdapter: DealsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(com.csestateconnect.R.layout.fragment_deal_frag5, container, false)

        val binding: FragmentDealFrag5Binding = DataBindingUtil.inflate(
            inflater, com.csestateconnect.R.layout.fragment_deal_frag5, container, false
        )
        viewModel = ViewModelProviders.of(this.requireActivity()).get(DealsViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        view.goto_leads_button.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_comission_to_navigation_assigned_leads)
        }


        viewModel.getAllDealsData().observe(this, Observer<List<ListOfDeals>> { dealsData ->

            if (viewModel.showFilteredDeals.value == true){
                val dealData = viewModel.filteredDealData.value
                if (dealData != null ) {
                    nodeals_layout.visibility = View.GONE
                    deals_recycler_view.visibility = View.VISIBLE
                    setUpDealsRecyclerView(listOf(dealData))
                }
                else {
                    nodeals_layout.visibility = View.VISIBLE
                    deals_recycler_view.visibility = View.GONE
                }
            }
            else {
                if (dealsData.isNullOrEmpty()) {
                    nodeals_layout.visibility = View.VISIBLE
                    deals_recycler_view.visibility = View.GONE
                } else if (dealsData.get(0).results.isEmpty()) {
                    nodeals_layout.visibility = View.VISIBLE
                    deals_recycler_view.visibility = View.GONE
                } else {
                    nodeals_layout.visibility = View.GONE
                    deals_recycler_view.visibility = View.VISIBLE
                    setUpDealsRecyclerView(dealsData)
                }
            }
        })


        if (isNetworkConnected(context!!)) {
            try {
                viewModel.getListOfDeals()
                viewModel.getDealStatus_Api()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(
                context,
                "No internet found. Showing cached list in the view",
                Toast.LENGTH_LONG
            ).show()
        }

        return view
    }

    fun setUpDealsRecyclerView(dealsList: List<ListOfDeals>) {
        var dealItems: List<com.csestateconnect.db.data.deals.Result>? = null
        try {
            dealItems = dealsList.get(0).results
        } catch (e: Exception) {
            e.printStackTrace()
        }

        dealsAdapter = DealsAdapter(dealItems)
        deals_recycler_view.adapter = dealsAdapter
        deals_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        deals_recycler_view.setHasFixedSize(true)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
