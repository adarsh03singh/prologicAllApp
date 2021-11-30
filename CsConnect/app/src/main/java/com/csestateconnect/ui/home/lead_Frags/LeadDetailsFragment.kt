package com.csestateconnect.ui.home.lead_Frags


import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.LeadDetailsActivityAdapter
import com.csestateconnect.adapters.Lead_Status_Adapter
import com.csestateconnect.databinding.FragmentLeadDetailsBinding
import com.csestateconnect.db.data.GetLeadStatus
import com.csestateconnect.db.data.leads.CreateLeadActivity
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.viewmodel.LeadsViewModel
import kotlinx.android.synthetic.main.fragment_lead_details.*
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass.
 */
class LeadDetailsFragment : Fragment() {

    lateinit var viewmodel : LeadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lead_details, container, false)

        val binding: FragmentLeadDetailsBinding = FragmentLeadDetailsBinding.bind(view)
        viewmodel = ViewModelProviders.of(this).get(LeadsViewModel::class.java)
        binding.leadsVM = viewmodel
        binding.lifecycleOwner = this

        viewmodel.lead_feedback_text.observe(this, Observer{ text ->
            if (text != null) {
                viewmodel.lead_feedback_nullcheck.value = true
            }
        })

        viewmodel.leadsStatusColor.observe(this, Observer { colorString ->
            lead_status_dropdown.setBackgroundColor(Color.parseColor(colorString))
        })

        viewmodel.getLeadStatusData().observe(this, Observer<List<GetLeadStatus>>{ leadStatus ->
            setUpLeadStatus(leadStatus)
        } )

//        viewmodel.leads_feedback_star.observe(this, Observer{ star ->
//            if (star != null){
//                feedback_ratingBar_view.rating = star.toFloat()
//            }
//        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val leadId = arguments?.get("LeadsId").toString()
        Log.i("tagid", leadId)

        if(leadId.isNotEmpty()) {
            if (leadId != "null") {
                viewmodel.detailLeadsIds = leadId.toInt()

                viewmodel.getAllLeadsData().observe(this, Observer<List<ListOfLeads>> { dataList ->
                    viewmodel.getLeadsDetailByPreviousId(dataList)
                })

                viewmodel.getLeadActivityData().observe(this, Observer { data ->
                    setUpLeadActivityRecyclerView(leadId.toInt(), data)
                })
            }
        }

        val id = arguments?.get("notifyId").toString()

        if(id.isNotEmpty()) {
            if (id != "null") {

                viewmodel.detailLeadsIds = id.toInt()
                runBlocking {
                    viewmodel.getListOfLeads()
                }

                viewmodel.getAllLeadsData().observe(this, Observer<List<ListOfLeads>> { dataList ->
                    viewmodel.getLeadsDetailByPreviousId(dataList)
                })

                viewmodel.getLeadActivityData().observe(this, Observer { data ->
                    setUpLeadActivityRecyclerView(id.toInt(), data)
                })
            }
        }

        viewmodel.leadActivityNotNull.observe(this, Observer { data ->
            if (!data.isNullOrEmpty()){
                setUpLeadActivityRecyclerView(viewmodel.leads_id.value!!, data )
            }
        })

    }

    fun setUpLeadStatus(dataList : List<GetLeadStatus>)
    {
        val leadRecyclerViewAdapter = Lead_Status_Adapter(dataList,context!!){ position ->

            viewmodel.layoutabove_leadstatus_visibility.value = View.VISIBLE
            viewmodel.lead_status_visibility.value = View.GONE

            lead_status_text.text = dataList.get(position).name
            lead_status_dropdown.setBackgroundColor(Color.parseColor(dataList.get(position).color))

            val projectId = Integer.parseInt(lead_id.text.toString())
            val leadStatusId = dataList[position].id

            viewmodel.updateLead(projectId, leadStatusId)

        }

        lead_status_recyclerView.adapter = leadRecyclerViewAdapter
        lead_status_recyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        lead_status_recyclerView.setHasFixedSize(true)
    }

    fun setUpLeadActivityRecyclerView(leadid : Int, dataList : List<CreateLeadActivity>?){

        if (!dataList.isNullOrEmpty()) {
            val data = dataList.filter { it.lead == leadid }
            val adapter = LeadDetailsActivityAdapter(leadid, data)

            activities_recyclerview.adapter = adapter
            activities_recyclerview.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
            activities_recyclerview.setHasFixedSize(true)
        }
        else {
            activities_recyclerview.visibility = View.GONE
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
