package com.csestateconnect.adapters.lead_create_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import kotlinx.android.synthetic.main.lead_create_cross_recycler.view.*

class LeadSelectLocationAdapter(val cityName: MutableList<String>, val itemClick: (List<String>) -> Unit) : RecyclerView.Adapter<LeadSelectLocationAdapter.ViewHolder>() {

//    val detachHolder

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

//        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.lead_create_cross_recycler,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = cityName[position]
        holder.image.setOnClickListener {
            cityName.removeAt(position)
            notifyItemRemoved(position)

            val list = listOf(holder.name.text.toString())
            itemClick(list)

        }
    }

    override fun getItemCount(): Int {
        val count = cityName.size
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.cross_box_text
        var image = itemView.cross_box_image

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}