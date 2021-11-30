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
import com.csestateconnect.adapters.AssignedLeadsAdapter
import com.csestateconnect.databinding.FragmentCsAssignLeadBinding
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.viewmodel.LeadsViewModel
import kotlinx.android.synthetic.main.fragment_cs_assign_lead.*
import kotlinx.android.synthetic.main.fragment_my_leads.*
import java.lang.Exception


class CsAssignLeadFragment : Fragment() {

    lateinit var viewModel: LeadsViewModel
    var assignedLeadsAdapter: AssignedLeadsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cs_assign_lead, container, false)
        val binding: FragmentCsAssignLeadBinding = FragmentCsAssignLeadBinding.bind(view)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(LeadsViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        if (isNetworkConnected(context!!)) {
            try {
                viewModel.getListOfLeads()
                viewModel.getCountries()
                viewModel.getLeadStatus_Api()
                viewModel.getCompletionStatus_Api()
                viewModel.getPreferredproperty_Api()
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

        viewModel.getAllLeadsData().observe(this, Observer<List<ListOfLeads>> { leadsData ->

            var assignedLeads : List<Result>? = null
            try {
                assignedLeads = leadsData.get(0).results.filter { it.assigned }
            }
            catch (e: Exception){
                e.printStackTrace()
            }

            if (viewModel.showFilteredLeads.value == true){
                val assignedLead = viewModel.filteredLeadData.value?.results?.filter { it.assigned }
                if (!assignedLead.isNullOrEmpty()) {
                    noassignleads_layout.visibility = View.GONE
                    csAssign_recycler_view.visibility = View.VISIBLE
                    setUpAssignedRecyclerView(assignedLead)
                }
                else {
                    noassignleads_layout.visibility = View.VISIBLE
                    csAssign_recycler_view.visibility = View.GONE
                }
            }
            else {
                if (assignedLeads.isNullOrEmpty()) {
                    noassignleads_layout.visibility = View.VISIBLE
                    csAssign_recycler_view.visibility = View.GONE
                }
                else if (assignedLeads.isEmpty()){
                    noassignleads_layout.visibility = View.VISIBLE
                    csAssign_recycler_view.visibility = View.GONE
                }
                else {
                    noassignleads_layout.visibility = View.GONE
                    csAssign_recycler_view.visibility = View.VISIBLE
                    setUpAssignedRecyclerView(assignedLeads)
                }

            }
        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // My leads fragment
        myLeads.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_assigned_leads_to_lead2TabFragment)
        }

//        val getLeadButton: Button = getView()!!.findViewById(R.id.get_leads_button)
//        getLeadButton.setOnClickListener {
//            createActivitiesDialog()
//        }
    }

    fun setUpAssignedRecyclerView(leadsData: List<Result>) {

        assignedLeadsAdapter = AssignedLeadsAdapter(leadsData)
        csAssign_recycler_view.adapter = assignedLeadsAdapter
        csAssign_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        csAssign_recycler_view.setHasFixedSize(true)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
