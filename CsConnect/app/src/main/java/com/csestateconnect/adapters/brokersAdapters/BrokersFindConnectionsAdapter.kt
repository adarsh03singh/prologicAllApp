package com.csestateconnect.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.utils.DateConverter
import kotlinx.android.synthetic.main.deal_items.view.*

class BrokersFindConnectionsAdapter() : RecyclerView.Adapter<BrokersFindConnectionsAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.brokers_find_connections_list,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 5
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
