package com.csestateconnect.ui.home.deal_Frags


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.adapters.Deal_status_Adapter
import com.csestateconnect.databinding.FragmentDealDetailsBinding
import com.csestateconnect.db.data.GetDealStatus
import com.csestateconnect.db.data.deals.ListOfDeals
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.viewmodel.DealsViewModel
import kotlinx.android.synthetic.main.fragment_deal_details.*
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class DealDetailsFragment : Fragment() {
    lateinit var viewmodel : DealsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_deal_details, container, false)

        val binding: FragmentDealDetailsBinding = FragmentDealDetailsBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(DealsViewModel::class.java)
        binding.dealVM = viewmodel
        binding.lifecycleOwner = this

        viewmodel.getDealStatusData().observe(this, Observer<List<GetDealStatus>>{ dealStatus ->
            setUpDealStatus(dealStatus)
        } )

        viewmodel.dealsStatusColor.observe(this, Observer { colorString ->
            deal_status_dropdown.setBackgroundColor(Color.parseColor(colorString))
        })

        viewmodel.dealsDocChequeImage.observe(this, Observer {it ->
            if (it != null){
                Glide.with(this).load(it).into(deal_detail_cheque_image!!)
            }
        })



            val dealId = arguments?.get("DealsId").toString()

            if (dealId.isNotEmpty()) {
                if (dealId != "null") {
                    viewmodel.detailDealsId = dealId.toInt()

                    viewmodel.getAllDealsData()
                        .observe(this, Observer<List<ListOfDeals>> { dataList ->
                            viewmodel.getDealDetailByPreviousId(dataList)
                        })
                }
            }

        val id = arguments?.get("notifyId").toString()

        if(id.isNotEmpty()) {
            if (id != "null") {

                viewmodel.detailDealsId = id.toInt()
                runBlocking {
                    viewmodel.getListOfDeals()
                }

                viewmodel.getAllDealsData().observe(this, Observer<List<ListOfDeals>> { dataList ->
                    viewmodel.getDealDetailByPreviousId(dataList)
                })
            }
        }

        viewmodel.getDealsDetailsChequeData().observe(this, Observer { it ->
            if (it != null){
                viewmodel.loadChequeData(it)
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        deal_submit_button.setOnClickListener {
            findNavController().navigate(R.id.action_dealDetailsFragment_to_dealUploadDocFragment)
        }
    }

    fun setUpDealStatus(dealList : List<GetDealStatus>)
    {
        val recyclerViewAdapter = Deal_status_Adapter(dealList)
        { position ->

            viewmodel.layoutabove_dealstatus_visibility.value = View.VISIBLE
            viewmodel.deal_status_visibility.value = View.GONE

            deal_status_text.text = dealList.get(position).name
            deal_status_dropdown.setBackgroundColor(Color.parseColor(dealList.get(position).color))

            val projectId = Integer.parseInt(deal_id.text.toString())
            val leadStatusId = dealList[position].id

            viewmodel.updateDeal(projectId, leadStatusId)

        }

        deal_status_recyclerView.adapter = recyclerViewAdapter
        deal_status_recyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        deal_status_recyclerView.setHasFixedSize(true)
    }


}
