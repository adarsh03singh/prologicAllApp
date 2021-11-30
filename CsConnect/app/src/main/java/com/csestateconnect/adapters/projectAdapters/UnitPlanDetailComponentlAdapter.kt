package com.csestateconnect.adapters.projectAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import kotlinx.android.synthetic.main.lead_create_cross_recycler.view.*
import kotlinx.android.synthetic.main.unit_plan_details_recycler_view_item.view.*

class UnitPlanDetailComponentlAdapter(val cityName: MutableList<String?>) : RecyclerView.Adapter<UnitPlanDetailComponentlAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.unit_plan_details_recycler_view_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.componentName.text = cityName[position]
        holder.componentPosition.text = position.toString()
    }

    override fun getItemCount(): Int {
        val count = cityName.size
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var componentName = itemView.unit_component_name
        var componentPosition = itemView.unit_item_position

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}