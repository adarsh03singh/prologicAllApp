package com.csestateconnect.ui.navdrawer.clients


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.ClientsListAdapter
import com.csestateconnect.adapters.FavoriteProjectsAdapter
import com.csestateconnect.databinding.FragmentClientsListBinding
import com.csestateconnect.databinding.FragmentFavoritesBinding
import com.csestateconnect.db.data.favouriteProject.FavouriteProject
import com.csestateconnect.db.data.favouriteProject.GetFavouriteProjectsEntity
import com.csestateconnect.db.data.listOfClients.ClientList
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.viewmodel.ClientsViewModel
import com.csestateconnect.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_clients_list.*
import kotlinx.android.synthetic.main.fragment_favorites.*


class ClientsListFragment : Fragment() {


    lateinit var viewModel: ClientsViewModel
    var clientRecyclerViewAdapter: ClientsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clients_list, container, false)

        val binding: FragmentClientsListBinding = FragmentClientsListBinding.bind(view)
        viewModel = ViewModelProviders.of(this).get(ClientsViewModel::class.java)
        binding.clientListViewmodel = viewModel
        binding.lifecycleOwner = this

        if(isNetworkConnected(context!!))
        {
            try {
                viewModel.callClientListAPIAndStore()

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        else
        {
            Toast.makeText(context,"No internet found.", Toast.LENGTH_LONG).show()
        }

        viewModel.getClientListFromDatabase().observe(this, Observer<ClientList> { cData ->


            try{

                val list = cData.results

                val cilData = list!!.map { it } as MutableList
                setUpClientListRecyclerView(cilData)
            }catch (e: Exception){
                e.printStackTrace()
            }
        })

        return view
    }

    fun setUpClientListRecyclerView(clList : MutableList<Result?>)
    {

        clientRecyclerViewAdapter = ClientsListAdapter(clList) {returnId ->
            if(!returnId.equals(null)) {
            viewModel.callRemoveClientsApi(returnId, view!!)
            }
        }
        clientsListRecyclerView.adapter = clientRecyclerViewAdapter
        clientsListRecyclerView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        clientsListRecyclerView.setHasFixedSize(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goToAddClient()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    fun goToAddClient(){
        parentLayout.setOnClickListener {
            view!!.findNavController().navigate(R.id.add_clientsFragment)
        }

    }
}
