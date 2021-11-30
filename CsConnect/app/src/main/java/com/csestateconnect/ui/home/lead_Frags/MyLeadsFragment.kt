package com.csestateconnect.ui.home.lead_Frags


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.AllLeadsAdapter
import com.csestateconnect.databinding.FragmentMyLeadsBinding
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.viewmodel.LeadsViewModel
import kotlinx.android.synthetic.main.fragment_cs_assign_lead.*
import kotlinx.android.synthetic.main.fragment_my_leads.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 */
class MyLeadsFragment : Fragment() {

    lateinit var viewModel: LeadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_my_leads, container, false)

        val binding: FragmentMyLeadsBinding = FragmentMyLeadsBinding.bind(view)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(LeadsViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        if(isNetworkConnected(context!!))
        {
            viewModel.getListOfLeads()

        }
        else
        {
            Toast.makeText(context,"No internet found. Showing cached list in the view",Toast.LENGTH_LONG).show()
        }

//        viewModel.getAllLeadsData().observe(this, Observer<List<ListOfLeads>>{ leadsData ->
//            setUpMyLeadsRecyclerView(leadsData)
//
//        } )

        viewModel.getAllLeadsData().observe(this, Observer<List<ListOfLeads>> { leadsData ->

            var myLeadsResult: List<Result>? = null
            try{
                myLeadsResult = leadsData.get(0).results.filter { it.submitted }
            } catch (e: Exception){
                e.printStackTrace()
            }

            if (viewModel.showMyFilteredLeads.value == true){
                val submittedLead = viewModel.filteredLeadData.value?.results?.filter { it.submitted }
                if (!submittedLead.isNullOrEmpty()) {
                    noMyleads_layout.visibility = View.GONE
                    myleads_recycler_view.visibility = View.VISIBLE
                    setUpMyLeadsRecyclerView(submittedLead)
                }
                else {
                    noMyleads_layout.visibility = View.VISIBLE
                    myleads_recycler_view.visibility = View.GONE
                }
            }
            else {
                if (myLeadsResult.isNullOrEmpty()) {
                    noMyleads_layout.visibility = View.VISIBLE
                    myleads_recycler_view.visibility = View.GONE
                }
                else if (myLeadsResult.isEmpty()){
                    noMyleads_layout.visibility = View.VISIBLE
                    myleads_recycler_view.visibility = View.GONE
                }
                else {
                    noMyleads_layout.visibility = View.GONE
                    myleads_recycler_view.visibility = View.VISIBLE
                    setUpMyLeadsRecyclerView(myLeadsResult)
                }

            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        csAssignLeads.setOnClickListener {
            findNavController().navigate(R.id.action_lead2TabFragment_to_navigation_assigned_leads)
        }
    }

    fun setUpMyLeadsRecyclerView(myLeadsList : List<Result>) {

        val leadRecyclerViewAdapter = AllLeadsAdapter(myLeadsList)
        myleads_recycler_view.adapter = leadRecyclerViewAdapter
        myleads_recycler_view.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        myleads_recycler_view.setHasFixedSize(true)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
