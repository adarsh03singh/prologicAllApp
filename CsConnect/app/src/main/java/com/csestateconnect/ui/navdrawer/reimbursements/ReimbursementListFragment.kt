package com.csestateconnect.ui.navdrawer.reimbursements

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.ClientsListAdapter
import com.csestateconnect.adapters.ReimbursementListAdapter
import com.csestateconnect.databinding.FragmentCreateReimbursementBinding
import com.csestateconnect.databinding.FragmentReimbursementListBinding
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.viewmodel.ReimbursementViewModel
import kotlinx.android.synthetic.main.fragment_clients_list.*
import kotlinx.android.synthetic.main.fragment_clients_list.parentLayout
import kotlinx.android.synthetic.main.fragment_reimbursement_list.*

class ReimbursementListFragment : Fragment() {

    lateinit var viewModel: ReimbursementViewModel
    var reimburseRecyclerViewAdapter: ReimbursementListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reimbursement_list, container, false)

        val binding: FragmentReimbursementListBinding = FragmentReimbursementListBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ReimbursementViewModel::class.java)
        binding.reimburseListViewmodel = viewModel
        binding.lifecycleOwner = this

        if(isNetworkConnected(context!!))
        {
            try {
                viewModel.callReimburseListAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
        }


        viewModel.getReimburseListFromDatabase().observe(this, Observer<GetReimbursementListEntity> { cData ->


            try{

                val list = cData.results

                val cilData = list!!.map { it } as MutableList
                setUpReimburseListRecyclerView(cilData)
            }catch (e: Exception){
                e.printStackTrace()
            }
        })

        return  view
    }
    fun setUpReimburseListRecyclerView(rList : MutableList<com.csestateconnect.db.data.reimbursements.reimburseList.Result>)
    {

        reimburseRecyclerViewAdapter = ReimbursementListAdapter(rList) {returnId ->
            if(!returnId.equals(null)) {
                viewModel.callRemoveReimburseApi(returnId, view!!)
            }
        }
        reimbursentsListRecyclerView.adapter = reimburseRecyclerViewAdapter
        reimbursentsListRecyclerView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        reimbursentsListRecyclerView.setHasFixedSize(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            goToCreateReimbursement()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun goToCreateReimbursement(){
        goToCreateReimburse_button.setOnClickListener {
            view!!.findNavController().navigate(R.id.createReimbursementFragment)
        }

    }

}