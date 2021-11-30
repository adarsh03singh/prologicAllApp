package com.csestateconnect.adapters.projectAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.ProjectWowFactor
import java.lang.Exception


class ProjectWowFactorAdapter(val wowFactorList: List<ProjectWowFactor?>?) : RecyclerView.Adapter<ProjectWowFactorAdapter.ViewHolder>() {

    var count: Int = 0
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context)
            .inflate(com.csestateconnect.R.layout.project_wow_factor_item,p0,false)

        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    try {
        holder.project_wowFactorTxt.text = wowFactorList!!.get(position)!!.name

    }catch (e: Exception){
        e.printStackTrace()
    }


    }
    override fun getItemCount(): Int {
        var count: Int =0
      try {
          count  = wowFactorList!!.size
      }catch (e: Exception){
          e.printStackTrace()
      }
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val project_wowFactorTxt: TextView = itemView.findViewById(R.id.wow_factors_txt)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}