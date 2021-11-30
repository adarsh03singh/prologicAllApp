package com.csestateconnect.ui.navdrawer


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.NotificationsAdapter
import com.csestateconnect.databinding.FragmentNotificationsBinding
import com.csestateconnect.db.data.notifications.Result
import com.csestateconnect.utils.Coroutines
import com.csestateconnect.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_notifications.*


/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : Fragment() {
    lateinit var viewmodel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        val binding: FragmentNotificationsBinding = FragmentNotificationsBinding.bind(view)
        viewmodel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.getNotificationsData().observe(this, Observer { data ->
//            val results = data.results
            if (data != null){
//            if (!results.isNullOrEmpty()) {
                val results = data.results
                noNotification_layout.visibility = View.GONE
                notification_recyclerView.visibility = View.VISIBLE
                setUpMyLeadsRecyclerView(results)
            }
            else {
                // show empty page
                noNotification_layout.visibility = View.VISIBLE
                notification_recyclerView.visibility = View.GONE
            }
        })

        if (isNetworkConnected(context!!)){
            Coroutines.io {
                val response = viewmodel.getListOfNotificationsApi()
                if (response.isSuccessful){
                    val results = response.body()?.results
                    if (!results.isNullOrEmpty()){
                        Coroutines.main {
                            setUpMyLeadsRecyclerView(results)
                        }
                    }
                    else {
                        noNotification_layout.visibility = View.VISIBLE
                        notification_recyclerView.visibility = View.GONE
                    }
                }
            }
        }

        return view
    }

    fun setUpMyLeadsRecyclerView(listData: List<Result>?) {

        val leadRecyclerViewAdapter = NotificationsAdapter(listData)
        notification_recyclerView.adapter = leadRecyclerViewAdapter
        notification_recyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        notification_recyclerView.setHasFixedSize(true)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
