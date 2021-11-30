package com.csestateconnect.ui.home


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.csestateconnect.R
import com.csestateconnect.adapters.ConcernsAdapter
import com.csestateconnect.adapters.DealsAdapter
import com.csestateconnect.databinding.FragmentConcernTicketsListBinding
import com.csestateconnect.db.data.listOfConcerns.GetListOfConcerns
import com.csestateconnect.db.data.listOfConcerns.Result
import com.csestateconnect.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_concern_tickets_list.*
import kotlinx.android.synthetic.main.fragment_deal_frag5.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ConcernTicketsListFragment : Fragment() {
    lateinit var viewmodel: HomeViewModel
    var concernAdapter: ConcernsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_concern_tickets_list, container, false)

        val binding: FragmentConcernTicketsListBinding = FragmentConcernTicketsListBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(HomeViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        if (isNetworkConnected(context!!)) {
            try {
                viewmodel.getListOfConcerns()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(
                context,
                "No internet found. Showing cached list in the view",
                Toast.LENGTH_LONG
            ).show()
        }

        viewmodel.getallConcernsData().observe(this, Observer { concernData ->
            if (concernData.results.isNullOrEmpty()) {
                noTickets_Layout.visibility = View.VISIBLE
                ticket_recyclerview.visibility = View.GONE
            }
            else if (concernData.results.isEmpty()){
                noTickets_Layout.visibility = View.VISIBLE
                ticket_recyclerview.visibility = View.GONE
            }
            else {
                noTickets_Layout.visibility = View.GONE
                ticket_recyclerview.visibility = View.VISIBLE
                setUpConcernsRecyclerView(concernData)
            }
        })

        return view
    }

    fun setUpConcernsRecyclerView(concernsdata: GetListOfConcerns) {
        var dealItems: List<Result>? = null
        try {
            dealItems = concernsdata.results
        } catch (e: Exception) {
            e.printStackTrace()
        }

        concernAdapter = ConcernsAdapter(dealItems)
        ticket_recyclerview.adapter = concernAdapter
        ticket_recyclerview.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        ticket_recyclerview.setHasFixedSize(true)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
