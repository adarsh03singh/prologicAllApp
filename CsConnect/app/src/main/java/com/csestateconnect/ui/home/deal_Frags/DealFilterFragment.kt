package com.csestateconnect.ui.home.deal_Frags


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.DealFilterStatusAdapter
import com.csestateconnect.adapters.lead_filter_adapters.LeadFilterStatusAdapter
import com.csestateconnect.databinding.FragmentDealFilterBinding
import com.csestateconnect.db.data.GetDealStatus
import com.csestateconnect.viewmodel.DealsViewModel
import kotlinx.android.synthetic.main.fragment_deal_filter.*
import kotlinx.android.synthetic.main.fragment_deal_filter.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DealFilterFragment : Fragment() {
    lateinit var viewmodel: DealsViewModel
    var dealRecyclerViewAdapter : DealFilterStatusAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deal_filter, container, false)
        val binding: FragmentDealFilterBinding = FragmentDealFilterBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(DealsViewModel::class.java)
        binding.dealVM = viewmodel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getDealStatusData().observe(this, androidx.lifecycle.Observer<List<GetDealStatus>> { dealStatus ->
            setUpDealFilterStatus(dealStatus)
        } )

        // status
        view.dealstatus_down_arrow.setOnClickListener {
            dealstatus_recycler_view_layout.visibility = View.VISIBLE
            dealstatus_up_arrow.visibility = View.VISIBLE
            dealstatus_down_arrow.visibility = View.GONE
        }

        view.dealstatus_up_arrow.setOnClickListener {
            dealstatus_recycler_view_layout.visibility = View.GONE
            dealstatus_down_arrow.visibility = View.VISIBLE
            dealstatus_up_arrow.visibility = View.GONE
        }

        // created start date
        view.dealstart_date_text.setOnClickListener {
            datePicker(view.dealstart_date_text)
        }

        // created end date
        view.dealend_date_text.setOnClickListener {
            datePicker(view.dealend_date_text)
        }

        // modified start date
        view.dealmodified_start_date.setOnClickListener {
            datePicker(view.dealmodified_start_date)
        }

        // modified end date
        view.dealmodified_end_date.setOnClickListener {
            datePicker(view.dealmodified_end_date)
        }

//        viewmodel.selecteddealsId.observe(this, androidx.lifecycle.Observer { it ->
//            if (it.isNullOrEmpty()){
//                dealRecyclerViewAdapter?.notifyDataSetChanged()
//            }
//        })

        viewmodel.clearButtonClicked.observe(this, androidx.lifecycle.Observer { it ->
            if (it){
                dealRecyclerViewAdapter?.notifyDataSetChanged()
            }
        })

    }

    // Deal Status Adapter
    fun setUpDealFilterStatus(dealStatus: List<GetDealStatus>) {
        val listDeal = viewmodel.listDeal

        dealRecyclerViewAdapter =
            DealFilterStatusAdapter(dealStatus, viewmodel.selecteddealsId.value) { selectedDealId ->
                if (!listDeal.contains(selectedDealId)) {
                    listDeal.add(selectedDealId)
                    viewmodel.selecteddealsId.value = listDeal
                }

            }

        dealstatus_recycler_view.adapter = dealRecyclerViewAdapter
        dealstatus_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        dealstatus_recycler_view.setHasFixedSize(true)
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
//                    timePicker()
                }
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

}
