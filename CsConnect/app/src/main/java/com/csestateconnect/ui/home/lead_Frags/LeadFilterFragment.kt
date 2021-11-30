package com.csestateconnect.ui.home.lead_Frags


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.lead_filter_adapters.LeadFilterStatusAdapter
import com.csestateconnect.databinding.FragmentLeadFilterBinding
import com.csestateconnect.db.data.GetLeadStatus
import com.csestateconnect.viewmodel.LeadsViewModel
import kotlinx.android.synthetic.main.fragment_lead_filter.*
import kotlinx.android.synthetic.main.fragment_lead_filter.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LeadFilterFragment : Fragment() {
    lateinit var viewmodel: LeadsViewModel
    var leadRecyclerViewAdapter : LeadFilterStatusAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lead_filter, container, false)
        val binding: FragmentLeadFilterBinding = FragmentLeadFilterBinding.bind(view)
//        val binding : FragmentLeadFilterBinding = FragmentLeadFilterBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(LeadsViewModel::class.java)
        binding.leadfilterViewmodel = viewmodel
//        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val leadType = arguments?.getString("leadFilter")
        if (leadType == "assigned"){
            viewmodel.leadType.value = 1
        } else {
            viewmodel.leadType.value = 2
        }

        viewmodel.getLeadStatusData().observe(this, androidx.lifecycle.Observer<List<GetLeadStatus>> { leadStatus ->
            setUpLeadFilterStatus(leadStatus)
        } )

        viewmodel.clearButtonClicked.observe(this, androidx.lifecycle.Observer<Boolean> { clear ->
            if (clear)
            leadRecyclerViewAdapter?.notifyDataSetChanged()
        } )

        // TODO - Filter changes are to be kept same unless clear button is pressed.


        // status
        view.status_down_arrow.setOnClickListener {
            status_recycler_view_layout.visibility = View.VISIBLE
            status_up_arrow.visibility = View.VISIBLE
            status_down_arrow.visibility = View.GONE
        }

        view.status_up_arrow.setOnClickListener {
            status_recycler_view_layout.visibility = View.GONE
            status_down_arrow.visibility = View.VISIBLE
            status_up_arrow.visibility = View.GONE
        }

        // created start date
        view.start_date_text.setOnClickListener {
            datePicker(view.start_date_text)
        }

        // created end date
        view.end_date_text.setOnClickListener {
            datePicker(view.end_date_text)
        }

        // modified start date
        view.modified_start_date.setOnClickListener {
            datePicker(view.modified_start_date)
        }

        // modified end date
        view.modified_end_date.setOnClickListener {
            datePicker(view.modified_end_date)
        }

        view.clearButtonLeads.setOnClickListener {
            start_date_text.setText("")
            end_date_text.setText("")
            modified_start_date.setText("")
            modified_end_date.setText("")
            viewmodel.selectedleadsId.value = null
            viewmodel.clearButtonClicked.value = true
        }

        super.onViewCreated(view, savedInstanceState)
    }

    // Lead Status Adapter
    fun setUpLeadFilterStatus(leadStatus: List<GetLeadStatus>)
    {
        val listLead = viewmodel.listLead

        leadRecyclerViewAdapter = LeadFilterStatusAdapter(leadStatus, viewmodel.selectedleadsId.value){selectedLeadId ->
            if (!listLead.contains(selectedLeadId)) {
                listLead.add(selectedLeadId)
                viewmodel.selectedleadsId.value = listLead
            }
            else {
                listLead.remove(selectedLeadId)
            }
        }
        status_recycler_view.adapter = leadRecyclerViewAdapter
        status_recycler_view.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        status_recycler_view.setHasFixedSize(true)

    }

    private fun datePicker(dateText: EditText) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker,
                    year: Int,
                    monthOfYear: Int,
                    dayOfMonth: Int
                ) {
//                    dateText.setText((monthOfYear + 1).toString() + "-" + dayOfMonth + "-" + year)
                    dateText.setText(year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth)
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

}
