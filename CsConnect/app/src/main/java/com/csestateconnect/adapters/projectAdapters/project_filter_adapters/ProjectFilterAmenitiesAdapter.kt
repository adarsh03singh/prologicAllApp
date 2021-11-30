package com.csestateconnect.adapters.projectAdapters.project_filter_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.Bucket
import com.csestateconnect.db.data.projects.BucketXXXXXXXX
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class ProjectFilterAmenitiesAdapter(
    val amenitiesData: ArrayList<Bucket?>, val amenityFilteredList: List<String>?, val itemClick: (String) -> Unit
) : RecyclerView.Adapter<ProjectFilterAmenitiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.filter_checkbox_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mAmenityFlist = mutableListOf<Int>()
        if (!amenityFilteredList.isNullOrEmpty()) {
            val amenityKey = amenitiesData.map { it!!.key }
            for (i in 0..amenityFilteredList.size - 1) {
                mAmenityFlist.add(amenityKey.indexOf(amenityFilteredList.get(i)))
            }
        } else {
            holder.name.isChecked = false
        }

        if (mAmenityFlist.contains(position)) {
            holder.name.isChecked = true
        }
        holder.name.text = amenitiesData.get(position)!!.key
        holder.name.setOnClickListener {
            itemClick(amenitiesData.get(position)!!.key!!)
        }
    }


    override fun getItemCount(): Int {
       return amenitiesData.size
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