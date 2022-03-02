package com.prologicwebsolution.microatm.ui.commission

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
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.adapter.CommissionAdapter
import com.prologicwebsolution.microatm.adapter.TransactionDetailsAdapter
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.data.transactionData.Data
import com.prologicwebsolution.microatm.data.transactionData.GetTransactionEntity
import com.prologicwebsolution.microatm.databinding.FragmentCommissionBinding
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.util.shooterFragment
import kotlinx.android.synthetic.main.fragment_commission.*
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.util.*


class CommissionFragment : Fragment() {


    lateinit var viewmodel: CommissionViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_commission, container, false)
        val binding: FragmentCommissionBinding = FragmentCommissionBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(CommissionViewModel::class.java)
        binding.commissionViewModel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.filteredCommissionData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null ) {
                setUpCommisiondataInRecyclerView(listOf(it))
            }

        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        val startDatetext = view.findViewById<EditText>(R.id.startDate)
        val endDatetext = view.findViewById<EditText>(R.id.endDate)

        startDatetext.setOnClickListener {
            datePicker(startDatetext)
        }
        endDatetext.setOnClickListener {
            datePicker(endDatetext)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(true,true,"Commission")
    }

    // Commission data set in recyclerView
    fun setUpCommisiondataInRecyclerView(commissionist: List<GetTransactionEntity>) {
        var commissionItems: List<com.prologicwebsolution.microatm.data.transactionData.Data>? = null
        try {
            commissionItems = commissionist.get(0).data
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val transAdapter = CommissionAdapter(commissionItems)
        commission_recyclerView.adapter = transAdapter
        commission_recyclerView.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
        )
        commission_recyclerView.setHasFixedSize(true)
    }

    private fun datePicker(dateText: EditText) {
        val calender = Calendar.getInstance()
        val mYear = calender.get(Calendar.YEAR)
        val mMonth = calender.get(Calendar.MONTH)
        val mDay = calender.get(Calendar.DAY_OF_MONTH)
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
        datePickerDialog.getDatePicker().setMaxDate(calender.getTimeInMillis())
        datePickerDialog.show()
    }

}