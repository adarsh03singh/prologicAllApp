package com.csestateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.leads.CreateLeadActivity
import com.csestateconnect.utils.DatenTimeConverter
import kotlinx.android.synthetic.main.lead_activities_items.view.*
import kotlinx.android.synthetic.main.lead_activities_items.view.activity_date
import kotlinx.android.synthetic.main.lead_create_activity_dialog_item.view.*
import kotlinx.android.synthetic.main.lead_deal_status_items.view.*


class LeadDetailsActivityAdapter(
    val leadId: Int, val data: List<CreateLeadActivity>
//                                 , val itemClick: (Int) -> Unit
) : RecyclerView.Adapter<LeadDetailsActivityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.lead_activities_items,p0,false)

        return ViewHolder(v)
    }


    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = (position + 1).toString()
        holder.date.text = DatenTimeConverter().dateConverter(data.get(position).date)
        holder.description.text = data.get(position).short_description

    }


    override fun getItemCount(): Int {
        return data.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var number = itemView.activity_number
        var date = itemView.activity_date
        var description = itemView.activity_description
    }


    override fun getItemId(position: Int): Long {
            return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
