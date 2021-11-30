package com.csestateconnect.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.leads.Result
import com.csestateconnect.utils.DateConverter
import kotlinx.android.synthetic.main.lead_items.view.*

class AllLeadsAdapter(val users: List<Result>?) : RecyclerView.Adapter<AllLeadsAdapter.ViewHolder>() {

    var navController: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.lead_items,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = users!!.get(position)
        holder.lead_client_name.text = userData.name
        if (userData.min_budget_view != null || userData.max_budget_view != null) {
            holder.lead_budget.text = userData.min_budget_view + " - " + userData.max_budget_view
        }
        else {
            holder.lead_budget.text = "N/A"
        }
        holder.lead_status.text = userData.lead_status.name
        holder.leadStatusColor.setBackgroundColor(Color.parseColor(userData.lead_status.color))
        holder.lead_location.text = userData.location?.name ?: "N/A"
        holder.allLeadsDate.text = DateConverter().dateConverter(userData.created_at)

        if (userData.preferred_property_type?.size != 0) {
            val list = userData.preferred_property_type!!.map { it!!.name }
            holder.lead_property_pref.text = list.joinToString()
        }
        else {
            holder.lead_property_pref.text = "N/A"
        }
        if (userData.preferred_status?.size != 0) {
            val list = userData.preferred_status!!.map { it!!.name }
            holder.lead_construction_status.text = list.joinToString()
        }
        else {
            holder.lead_construction_status.text = "N/A"
        }

        val num = userData.phone_number
        holder.lead_call_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: $num")
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf("LeadsId" to userData.id)
            navController!!.navigate(R.id.action_lead2TabFragment_to_leadDetailsFragment, bundle)
        }

    }


    override fun getItemCount(): Int {
        var count: Int = 0
        try {
            if (users?.size != null){
                count = users.size
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var lead_client_name = itemView.all_lead_client_name
        var lead_budget = itemView.all_lead_budget
        var lead_status = itemView.all_lead_status
        var leadStatusColor = itemView.allLeadStatusColor
        var lead_location = itemView.all_lead_location
        var lead_property_pref = itemView.all_lead_property_pref
        var lead_construction_status = itemView.all_lead_construction_status
        var lead_call_button = itemView.all_lead_call_button
        var allLeadsDate = itemView.alllead_date
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}