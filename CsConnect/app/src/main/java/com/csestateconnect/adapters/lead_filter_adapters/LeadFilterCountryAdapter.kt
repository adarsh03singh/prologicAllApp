package com.csestateconnect.adapters.lead_filter_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.countries.Countries
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class LeadFilterCountryAdapter(val countryData: List<Countries>) : RecyclerView.Adapter<LeadFilterCountryAdapter.ViewHolder>() {

    val cityData = countryData.get(0).cities

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

//        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.filter_checkbox_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.country_setCity.text = cityData.get(position).name

    }


    override fun getItemCount(): Int {
        var count: Int = 0
        try {
            for (i in 1.. cityData.size)
            {
                count++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var country_setCity = itemView.filter_checkbox

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}