package com.csestateconnect.ui.navdrawer.clients


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.csestateconnect.R
import com.csestateconnect.adapters.ClientDocListAdapter
import com.csestateconnect.databinding.FragmentClientsDetailBinding
import com.csestateconnect.db.data.clientDoc.GetClientDocListEntity
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.viewmodel.ClientsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_clients_detail.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class ClientsDetailFragment : Fragment() {
    var previousId: Int? = null
    lateinit var viewModel: ClientsViewModel
    var path: Uri? = null

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clients_detail, container, false)

        val binding: FragmentClientsDetailBinding = FragmentClientsDetailBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)
        binding.clientDetailViewmodel = viewModel
        binding.lifecycleOwner = this

            if(isNetworkConnected(context!!))
            {
                try {
                    viewModel.callClientListAPIAndStore()
                    viewModel.callClientDocListAPIAndStore()

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            else
            {
                Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
            }

            viewModel.getClientDocListFromDatabase().observe(this, Observer<GetClientDocListEntity> { docData ->

                try{
                    for (i in 0..docData.results!!.size - 1 ) {

                        val cilData = docData.results.toTypedArray()
                        val resId = cilData.get(i)!!.brokerClient

                        if (resId == previousId) {

                            val clientDocList = cilData.groupBy({ it!!.brokerClient}, {it})
                          val d =  clientDocList.get(resId)
                            setUpClientListRecyclerView(d!!)
                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            })

        viewModel.getClientListFromDatabase().observe(this, Observer<ClientList> { cData ->
            try {
                for (i in 0..cData.results!!.size - 1 ) {

                    val resList = cData.results[i]

                    if (resList!!.id == previousId) {
                        viewModel.clDetails.value = resList
                        viewModel.clientRecentId.value = previousId
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
//        getting previews client id
        val sharedPref: SharedPreferences = context!!.getSharedPreferences("PREFERENCE_NAME", 0)
         previousId = sharedPref.getInt("clientList_id", 0)

        //  uploading pdf files
        document_upload.setOnClickListener {
            onclickToAddDocumentFrag()
        }
    }



    fun setUpClientListRecyclerView(clList : List<com.csestateconnect.db.data.clientDoc.Result?>)
    {

       val clientRecyclerViewAdapter = ClientDocListAdapter(clList) { docId ->
           if(!docId.equals(null)) {
           viewModel.callRemoveClientsDocsApi(docId,view!!)
           }

        }


        documents_recycler_view.adapter = clientRecyclerViewAdapter
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
            context, // Context
            LinearLayoutManager.VERTICAL, false
        )
        documents_recycler_view.layoutManager = linearLayoutManager
        documents_recycler_view.setHasFixedSize(true)

    }
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun onclickToAddDocumentFrag() {
        view!!.findNavController().navigate(R.id.addDocument)

    }

}
