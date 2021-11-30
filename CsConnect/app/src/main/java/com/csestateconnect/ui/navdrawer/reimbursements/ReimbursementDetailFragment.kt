package com.csestateconnect.ui.navdrawer.reimbursements

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
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
import com.csestateconnect.R
import com.csestateconnect.adapters.ClientDocListAdapter
import com.csestateconnect.adapters.ReimburseDocListAdapter
import com.csestateconnect.databinding.FragmentClientsDetailBinding
import com.csestateconnect.databinding.FragmentReimbursementDetailBinding
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.reimbursements.reimburseList.GetReimbursementListEntity
import com.csestateconnect.db.data.reimbursements.reimburseList.Result
import com.csestateconnect.db.data.reimbursements.reimbursementDocs.GetReimburseDocListEntity
import com.csestateconnect.viewmodel.ClientsViewModel
import com.csestateconnect.viewmodel.ReimbursementViewModel
import kotlinx.android.synthetic.main.fragment_clients_detail.*
import kotlinx.android.synthetic.main.fragment_clients_detail.document_upload
import kotlinx.android.synthetic.main.fragment_reimbursement_detail.*
import kotlinx.android.synthetic.main.fragment_reimbursement_list.*

class ReimbursementDetailFragment : Fragment() {

    var reimbursePreviousId: Int? = null
    lateinit var viewModel: ReimbursementViewModel
    var path: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reimbursement_detail, container, false)

        val binding: FragmentReimbursementDetailBinding = FragmentReimbursementDetailBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ReimbursementViewModel::class.java)
        binding.reimbursementDetailViewmodel = viewModel
        binding.lifecycleOwner = this

        if(isNetworkConnected(context!!))
        {
            try {
                viewModel.callReimburseListAPIAndStore()
                viewModel.callReimburseDocListAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
        }

        viewModel.getReimburseDocListFromDatabase().observe(this, Observer<GetReimburseDocListEntity> { docData ->

            try{
                for (i in 0..docData.results!!.size - 1 ) {

                    val reimburseResultData = docData.results.toTypedArray()
                    val resId = reimburseResultData.get(i).id

                    if (resId == reimbursePreviousId) {

                        val reimburseDocList = reimburseResultData.groupBy({ it!!.id}, {it})
                        val rembsData =  reimburseResultData.get(resId!!)
                        setUpReimburseListRecyclerView(listOf(rembsData)!!)
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        })


        viewModel.getReimburseListFromDatabase().observe(this, Observer<GetReimbursementListEntity> { rData ->
            try {
                for (i in 0..rData.results!!.size - 1 ) {

                    val resList = rData.results[i]

                    if (resList!!.id == reimbursePreviousId) {
                        viewModel.reimburseDetails.value = resList
                        viewModel.clientRecentId.value = reimbursePreviousId
                    }
                }
            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }
            viewModel.getProfile()

        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        getting previews reimburseList_id
        val sharedPref: SharedPreferences = context!!.getSharedPreferences("PREFERENCE_NAME", 0)
        reimbursePreviousId = sharedPref.getInt("reimburseList_id", 0)

        //  uploading pdf files
        document_upload.setOnClickListener {
            onclickToAddDocumentFrag()
        }
    }

    fun setUpReimburseListRecyclerView(reimurseList : List<com.csestateconnect.db.data.reimbursements.reimbursementDocs.Result?>)
    {

        val  reimburseRecyclerViewAdapter =  ReimburseDocListAdapter(reimurseList) { docId ->
            if(!docId.equals(null)) {
//                viewModel.callRemoveClientsDocsApi(docId,view!!)
            }

        }


        reimburseDocuments_recycler_view.adapter = reimburseRecyclerViewAdapter
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
            context, // Context
            LinearLayoutManager.VERTICAL, false
        )
        reimburseDocuments_recycler_view.layoutManager = linearLayoutManager
        reimburseDocuments_recycler_view.setHasFixedSize(true)

    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
    fun onclickToAddDocumentFrag() {
        view!!.findNavController().navigate(R.id.createReimbureDocumentFragment)

    }

}