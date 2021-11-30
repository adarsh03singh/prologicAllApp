package com.csestateconnect.adapters.projectAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import java.lang.Exception


class ProjectCommissionValuesAdapter(val commissionValues: Array<String>) : RecyclerView.Adapter<ProjectCommissionValuesAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.project_commission_items,p0,false)

        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    try {
           val a = commissionValues.get(position).split(":")

        holder.commission_key.text = a[0].replace("{", "").replace("\"", "")
        holder.commission_value.text = a[1].replace("}", "").replace("\"", "")

    }catch (e: Exception){
        e.printStackTrace()
    }


    }
    override fun getItemCount(): Int {
        var count: Int = 0


        try {

              count = commissionValues.size


        }catch (e: Exception){
          e.printStackTrace()
      }

        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val commission_key = itemView.findViewById<TextView>(R.id.commission_keys)
        val commission_value = itemView.findViewById<TextView>(R.id.commission_values)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}