package com.csestateconnect.adapters.projectAdapters.project_filter_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.BucketXX
import com.csestateconnect.db.data.projects.BucketXXXXXXXX
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class ProjectFilterProjectStatusAdapter(
    val projectStatusList: List<BucketXXXXXXXX?>, val statusFilteredList: List<String>?, val itemClick: (String) -> Unit
) : RecyclerView.Adapter<ProjectFilterProjectStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.filter_checkbox_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mStatusFlist = mutableListOf<Int>()
        if (!statusFilteredList.isNullOrEmpty()){
            val statusKey = projectStatusList.map { it!!.key }
            for (i in 0..statusFilteredList.size - 1) {
                mStatusFlist.add(statusKey.indexOf(statusFilteredList.get(i)))
            }
        }
        else {
            holder.name.isChecked = false
        }

        if (mStatusFlist.contains(position)){
            holder.name.isChecked =  true
        }
        holder.name.text = projectStatusList.get(position)!!.key
        holder.name.setOnClickListener {
            itemClick(projectStatusList.get(position)!!.key!!)
        }

//        holder.name.text = projectStatusList.get(position)!!.key
//        holder.name.isChecked = false
//
//        holder.name.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton, isChecked:Boolean) {
//                itemClick(projectStatusList.get(position)!!.key!!)
////                filterAdapterCallback!!.onStatusCheckedCallback(position, isChecked)
//            }
//        })

    }


    override fun getItemCount(): Int {
       return projectStatusList.size
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