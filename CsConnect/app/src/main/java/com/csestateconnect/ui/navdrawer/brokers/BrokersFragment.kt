package com.csestateconnect.ui.navdrawer.brokers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.adapters.BrokersFindConnectionsAdapter
import com.csestateconnect.adapters.DealsAdapter
import com.csestateconnect.db.data.deals.ListOfDeals
import kotlinx.android.synthetic.main.fragment_brokers.*
import kotlinx.android.synthetic.main.fragment_brokers.view.*


/**
 * A simple [Fragment] subclass.
 */
class BrokersFragment : Fragment() {
    var brokersAdapter: BrokersFindConnectionsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_brokers, container, false)

        setUpDealsRecyclerView(view)

        return view
    }


    fun setUpDealsRecyclerView(view: View) {
        brokersAdapter = BrokersFindConnectionsAdapter()
        view.brokers_findConnections_rv.adapter = brokersAdapter
        view.brokers_findConnections_rv.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        view.brokers_findConnections_rv.setHasFixedSize(true)
    }
}
