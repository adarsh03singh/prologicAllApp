package com.prologicwebsolution.eventshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.ui.imageShare.ImageShareFragment


class TextSizeAdapter(val sizeList: ArrayList<ImageShareFragment.SizeModel>, val itemClick: (Int) -> Unit) : RecyclerView.Adapter<TextSizeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.size_list_items,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size = sizeList.get(position).textSize

        holder.cityName.text = size.toString()
        holder.cityName.setOnClickListener {
            itemClick(size)
        }

    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var cityName = itemView.findViewById(R.id.sizeText) as TextView

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}