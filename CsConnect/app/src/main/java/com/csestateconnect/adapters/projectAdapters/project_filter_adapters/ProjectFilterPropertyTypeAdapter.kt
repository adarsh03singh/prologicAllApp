package com.csestateconnect.adapters.projectAdapters.project_filter_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.BucketXXXXXX
import com.csestateconnect.db.data.projects.Location
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class ProjectFilterPropertyTypeAdapter(
    val propertyTypeName: ArrayList<BucketXXXXXX?>, val itemClick: (String) -> Unit
) : RecyclerView.Adapter<ProjectFilterPropertyTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

//        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.filter_checkbox_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val b = propertyTypeName[position]!!.key
        holder.name.text = b
        holder.name.isChecked = false
        holder.name.setOnClickListener {

            itemClick(propertyTypeName.get(position)!!.key!!)
        }

    }


    override fun getItemCount(): Int {
       return propertyTypeName.size
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