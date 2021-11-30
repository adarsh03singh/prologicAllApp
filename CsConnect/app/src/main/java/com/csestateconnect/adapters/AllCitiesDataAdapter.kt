package com.csestateconnect.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.utils.DateConverter
import kotlinx.android.synthetic.main.all_cities_items.view.*
import kotlinx.android.synthetic.main.cs_assign_lead_items.view.*
import kotlinx.android.synthetic.main.lead_items.view.*
import java.lang.Exception


class AllCitiesDataAdapter(val cityData: List<String>?,val itemClick: (String) -> Unit ) : RecyclerView.Adapter<AllCitiesDataAdapter.ViewHolder>() {

    var navController: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.all_cities_items,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = cityData!!.get(position)

            holder.cityName.text = name
        holder.cityName.setOnClickListener {
            itemClick(name)
        }

    }

    override fun getItemCount(): Int {
        var count: Int = 0
        if (cityData?.size != null){
            count = cityData.size
        }
        return count
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var cityName = itemView.city_recyclerText

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
