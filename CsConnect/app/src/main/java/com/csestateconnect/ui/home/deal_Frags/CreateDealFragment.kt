package com.csestateconnect.ui.home.deal_Frags


import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.DecimalDigitsInputFilter
import com.csestateconnect.R
import com.csestateconnect.adapters.CreateDealStatusAdapter
import com.csestateconnect.databinding.FragmentCreateDealBinding
import com.csestateconnect.db.data.GetDealStatus
import com.csestateconnect.db.data.projects.Projectsdata
import com.csestateconnect.viewmodel.DealsViewModel
import kotlinx.android.synthetic.main.fragment_create_deal.*
import kotlinx.android.synthetic.main.fragment_create_deal.view.*

/**
 * A simple [Fragment] subclass.
 */
class CreateDealFragment : Fragment() {
    lateinit var viewmodel: DealsViewModel
    var dealRecyclerViewAdapter: CreateDealStatusAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_deal, container, false)
        val binding: FragmentCreateDealBinding = FragmentCreateDealBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(DealsViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.getDealStatus_Api()

        viewmodel.createDealLeadId = Integer.parseInt(arguments?.get("leadIDforCreateDeal").toString())

        view.sold_area_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        view.comm_rate_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        view.comm_percent_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(3, 2)))

        viewmodel.getDealStatusData()
            .observe(this, androidx.lifecycle.Observer<List<GetDealStatus>> { dealStatus ->
                setUpDealFilterStatus(dealStatus)
            })

//        viewmodel.getAssignedProjects().observe(this, Observer { it ->
//            viewmodel.assignedProjectsData = it
//
//        })

        viewmodel.getAllProjectData().observe(this, Observer<List<Projectsdata>> { it ->
            viewmodel.projectsData = it
        })

        // KYC AND BANK ACCOUNT DETAILS UPDATED
//        viewmodel.kycUpdatedBool.observe(this, Observer { it ->
//            if (it){
//                dealKycLayout.backgroundTintList = ContextCompat.getColorStateList((context!!), R.color.Green)
//                dealKycText.text = "KYC Updated"
//                dealKycImage.setImageResource(R.drawable.ic_check_circle)
//            }
//        })
//
//        viewmodel.bankaccountUpdatedBool.observe(this, Observer { it ->
//            if (it){
//                dealBankLayout.backgroundTintList = ContextCompat.getColorStateList((context!!), R.color.Green)
//                dealBankText.text = "Bank Account Updated"
//                dealBankImage.setImageResource(R.drawable.ic_check_circle)
//            }
//        })

        view.deal_status_drop_close.setOnClickListener {
            deal_status_recycler_view_layout.visibility = View.GONE
            deal_status_drop_down.visibility = View.VISIBLE
            deal_status_drop_close.visibility = View.GONE
        }

        view.deal_status_drop_down.setOnClickListener {
            deal_status_recycler_view_layout.visibility = View.VISIBLE
            deal_status_drop_close.visibility = View.VISIBLE
            deal_status_drop_down.visibility = View.GONE
        }

//        view.project_name_drop_close.setOnClickListener {
//            project_name_recycler_view_layout.visibility = View.GONE
//            project_name_drop_close.visibility = View.GONE
//            project_name_drop_down.visibility = View.VISIBLE
//        }
//
//        view.project_name_drop_down.setOnClickListener {
//            project_name_recycler_view_layout.visibility = View.VISIBLE
//            project_name_drop_down.visibility = View.GONE
//            project_name_drop_close.visibility = View.VISIBLE
//        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpBudgetSpinner()
        super.onViewCreated(view, savedInstanceState)
    }

    fun setUpBudgetSpinner() {
        val budgetCurrencyList = listOf("Cr", "Lac", "K", "INR")

        // Total Commission
        val totalCommissionSpinner = view!!.findViewById<Spinner>(R.id.total_spinner)
        if (totalCommissionSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_dropdown_item, budgetCurrencyList
            )
            totalCommissionSpinner.adapter = adapter
        }

        totalCommissionSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                total_comm_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2)))
                viewmodel.totalCommissionCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Payable Commission
        val payableCommissionSpinner = view!!.findViewById<Spinner>(R.id.payable_spinner)
        if (payableCommissionSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_dropdown_item, budgetCurrencyList
            )
            payableCommissionSpinner.adapter = adapter
        }

        payableCommissionSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                pay_comm_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2)))
                viewmodel.payableCommissionCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Paid Commission
        val paidCommissionSpinner = view!!.findViewById<Spinner>(R.id.paid_spinner)
        if (paidCommissionSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_dropdown_item, budgetCurrencyList
            )
            paidCommissionSpinner.adapter = adapter
        }

        paidCommissionSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                paid_comm_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2)))
                viewmodel.paidCommissionCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Unpaid Commission
        val unpaidCommissionSpinner = view!!.findViewById<Spinner>(R.id.unpaid_spinner)
        if (unpaidCommissionSpinner != null) {
            val adapter = ArrayAdapter(
                view!!.context,
                android.R.layout.simple_spinner_dropdown_item, budgetCurrencyList
            )
            unpaidCommissionSpinner.adapter = adapter
        }

        unpaidCommissionSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                unpaid_comm_text.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(2, 2)))
                viewmodel.unpaidCommissionCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }


    // Deal Status Adapter
    fun setUpDealFilterStatus(dealStatus: List<GetDealStatus>) {

        dealRecyclerViewAdapter = CreateDealStatusAdapter(dealStatus) { selectedDealId ->
            viewmodel.createDealDealStatusId.value = selectedDealId
        }
        dealRecyclerViewAdapter?.notifyDataSetChanged()
        createdeal_status_recycler_view.adapter = dealRecyclerViewAdapter
        createdeal_status_recycler_view.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        createdeal_status_recycler_view.setHasFixedSize(true)

    }

}

/*z

api hit on lead details
check database on create deal
create data var(with data or null)

send data var to recycler to load data
open dialog box

 */
