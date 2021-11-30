package com.csestateconnect.adapters.lead_filter_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.GetLeadStatus
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class LeadFilterStatusAdapter(val leadStatus: List<GetLeadStatus>, val filteredList: List<Int>?, val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<LeadFilterStatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

//        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.filter_checkbox_item, p0, false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var a = mutableListOf<Int>()
        if (!filteredList.isNullOrEmpty()){
            val leadStatusId = leadStatus.map { it.id }
            for (i in 0..filteredList.size - 1) {
                a.add(leadStatusId.indexOf(filteredList.get(i)))
            }
        }
        else {
            holder.lead_status.isChecked = false
        }

        if (a.contains(position)){
            holder.lead_status.isChecked = true
        }

        holder.lead_status.text = leadStatus.get(position).name
//        holder.lead_status.isChecked = false
        holder.lead_status.setOnClickListener {
                itemClick(leadStatus.get(position).id)
        }
    }

    override fun getItemCount(): Int {
        return leadStatus.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lead_status = itemView.filter_checkbox
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
