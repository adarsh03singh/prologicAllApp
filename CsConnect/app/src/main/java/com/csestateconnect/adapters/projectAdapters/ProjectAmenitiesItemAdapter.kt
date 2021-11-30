package com.csestateconnect.adapters.projectAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.ProjectAmenity
import java.lang.Exception


class ProjectAmenitiesItemAdapter(val amenities: List<ProjectAmenity?>?) : RecyclerView.Adapter<ProjectAmenitiesItemAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.amenities_items,p0,false)

        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    try {

            Glide.with(holder.itemView.context).load(amenities!!.get(position)!!.iconImage)
                .into(holder.amenities_icon)

        holder.amenities_name.text = amenities.get(position)!!. name

    }catch (e: Exception){
        e.printStackTrace()
    }


    }
    override fun getItemCount(): Int {
        var count: Int = 0


        try {

              count = amenities!!.size


        }catch (e: Exception){
          e.printStackTrace()
      }

        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val amenities_icon = itemView.findViewById<ImageView>(R.id.amenities_icon)
        val amenities_name = itemView.findViewById<TextView>(R.id.amenities_name)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}