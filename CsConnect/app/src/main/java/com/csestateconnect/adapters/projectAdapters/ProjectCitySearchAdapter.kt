package com.csestateconnect.adapters.projectAdapters

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.*
import java.lang.Exception


class ProjectCitySearchAdapter(val projectCityList: Map<String?,List<Result?>>, val itemClick: (Int) -> Unit ) : RecyclerView.Adapter<ProjectCitySearchAdapter.ViewHolder>() {


    var navcontroller: NavController? = null
    var count: Int = 0
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.project_search_location_item,p0,false)

        v.setOnClickListener {
            itemClick(viewType)
        }

        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val cityName = projectCityList.map{it.key}

    holder.cityName.text = cityName.get(position)
    
    }
    override fun getItemCount(): Int {
        var count: Int =0
      try {
          count  = projectCityList.size
      }catch (e: Exception){
          e.printStackTrace()
      }
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cityName : TextView = itemView.findViewById(R.id.project_city_recyclerText)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}