package com.csestateconnect.adapters.projectAdapters.project_filter_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.BucketXXXX
import com.csestateconnect.db.data.projects.Facets
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*
import java.util.*
import kotlin.collections.ArrayList


class ProjectFilterLocationAdapter(
    val locationList: ArrayList<BucketXXXX?>,val locatioFilteredList: List<String>?,val itemClick: (String) -> Unit
) : RecyclerView.Adapter<ProjectFilterLocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.filter_checkbox_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mLocationFlist = mutableListOf<Int>()
        if (!locatioFilteredList.isNullOrEmpty()){
            val locationKey = locationList.map { it!!.key }
            for (i in 0..locatioFilteredList.size - 1) {
                mLocationFlist.add(locationKey.indexOf(locatioFilteredList.get(i)))
            }
        }
        else {
            holder.name.isChecked = false
        }

        if (mLocationFlist.contains(position)){
            holder.name.isChecked =  true
        }
        holder.name.text = locationList.get(position)!!.key
        holder.name.setOnClickListener {
            itemClick(locationList.get(position)!!.key!!)
        }


    }

    override fun getItemCount(): Int {
       return locationList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.filter_checkbox

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}