package com.prologicwebsolution.microatm.ui.withdrawlStatus

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
import com.prologicwebsolution.microatm.adapter.WithdrawalStatusAdapter
import com.prologicwebsolution.microatm.data.payoutList.PayoutListEntity
import com.prologicwebsolution.microatm.databinding.FragmentWithdrawlStatusBinding
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.util.shooterFragment
import kotlinx.android.synthetic.main.fragment_commission.*
import kotlinx.android.synthetic.main.fragment_connect_micro_atm.*
import kotlinx.android.synthetic.main.fragment_withdrawl_status.*
import java.util.*


class WithdrawlStatusFragment : Fragment() {

    lateinit var viewmodel: WithdrawlStatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_withdrawl_status, container, false)
        val binding: FragmentWithdrawlStatusBinding = FragmentWithdrawlStatusBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(WithdrawlStatusViewModel::class.java)
        binding.withdrawlStatusViewModel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.filteredCommissionData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null ) {
                setUpWithdrawStatusInRecyclerView(listOf(it))
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
        (activity as MainActivity).setHeader(true,true,"Withdraw Status")
    }

    // Withdrawl status data set in recyclerView
    fun setUpWithdrawStatusInRecyclerView(payoutList: List<PayoutListEntity>) {
        var withdrwStatusData: List<com.prologicwebsolution.microatm.data.payoutList.Data>? = null
        try {
            withdrwStatusData = payoutList.get(0).data
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val transAdapter = WithdrawalStatusAdapter(withdrwStatusData)
        withdrawStatus_recyclerView.adapter = transAdapter
        withdrawStatus_recyclerView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        withdrawStatus_recyclerView.setHasFixedSize(true)
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

 /*   @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //adding a layoutmanager
        withdrawStatus_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)


        //crating an arraylist to store users using the data class user
        val users = ArrayList<User>()
        //adding some dummy data to the list
        users.add(User("Settled"))
        users.add(User("Failed"))
        users.add(User("Processing"))
        users.add(User("Failed"))
        users.add(User("Processing"))

        //creating our adapter
        val adapter = WithdrawStatusAdaper(users)

        //now adding the adapter to recyclerview
        withdrawStatus_recyclerView.adapter = adapter

    }
    // //creating our Model
    data class User(val name: String)*/

}