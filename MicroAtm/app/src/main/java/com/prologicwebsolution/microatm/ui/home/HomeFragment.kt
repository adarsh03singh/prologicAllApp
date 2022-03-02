package com.prologicwebsolution.microatm.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.adapter.HomeReclerviewAdaper
import com.prologicwebsolution.microatm.databinding.FragmentHomeBinding
import com.prologicwebsolution.microatm.ui.MainActivity
import com.prologicwebsolution.microatm.ui.aepes.AepsFragment
import com.prologicwebsolution.microatm.ui.dashboared.DashboardFragment
import com.prologicwebsolution.microatm.util.addFragment
import com.prologicwebsolution.microatm.util.shooterFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var viewmodel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val binding: FragmentHomeBinding = FragmentHomeBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(HomeViewModel::class.java)
        binding.homeViewModel = viewmodel
        binding.lifecycleOwner = this

        return view
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()
        //adding a layoutmanager
        home_recyclerView.layoutManager = GridLayoutManager(context, 2)


        //crating an arraylist to store serviceModel using the data class ServicesModel
        val serviceModel = ArrayList<ServicesModel>()
        //adding some dummy data to the list
        serviceModel.add(ServicesModel("MicroAtm", R.drawable.micro_atm_device_xxhdpi))
        serviceModel.add(ServicesModel("AEPS Balance Enquiry", R.drawable.ic_balance_enquiry))
        serviceModel.add(ServicesModel("Sale by card", R.drawable.ic_atm_icon))
        serviceModel.add(ServicesModel("AEPS Mini Statement", R.drawable.ic_balance_enquiry))
        serviceModel.add(ServicesModel("AEPS Cash Withdrawal", R.drawable.ic_cash_withdrawal))

        //creating our adapter
        val adapter = HomeReclerviewAdaper(serviceModel) {
            if (it.equals(0))
                addFragment(DashboardFragment())
            else if (it.equals(1))
                addFragment(AepsFragment(),
                    bundleOf("tnx_type" to "BE", "title" to serviceModel.get(it).name))
            else if (it.equals(3))
                addFragment(AepsFragment(),
                    bundleOf("tnx_type" to "MS", "title" to serviceModel.get(it).name))
            else if (it.equals(4))
                addFragment(AepsFragment(),
                    bundleOf("tnx_type" to "CW", "title" to serviceModel.get(it).name))
        }

        //now adding the adapter to recyclerview
        home_recyclerView.adapter = adapter

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHideHeader()
    }


    // //creating our Model
    data class ServicesModel(val name: String, val images: Int)

}