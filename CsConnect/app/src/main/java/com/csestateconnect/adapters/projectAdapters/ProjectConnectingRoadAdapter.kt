package com.csestateconnect.adapters.projectAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.Distance
import java.lang.Exception


class ProjectConnectingRoadAdapter(val roadList: List<Distance?>) : RecyclerView.Adapter<ProjectConnectingRoadAdapter.ViewHolder>() {

    var count: Int = 0
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context)
            .inflate(com.csestateconnect.R.layout.project_connecting_roads_item,p0,false)

        return ViewHolder(
            v
        )
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//    val userData = users.get(0).results!!.get(position)
    try {
        holder.project_connecting_road_name.text = roadList.get(position)!!.road!!.name
        holder.project_connecting_road_distance.text = roadList.get(position)!!.distance

    }catch (e: Exception){
        e.printStackTrace()
    }

    }
    override fun getItemCount(): Int {
        var count: Int =0
      try {
          count  = roadList.size
      }catch (e: Exception){
          e.printStackTrace()
      }
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val project_connecting_road_name : TextView = itemView.findViewById(R.id.project_connecting_road_name_txt)
        val project_connecting_road_distance : TextView = itemView.findViewById(R.id.project_connecting_road_distance)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}