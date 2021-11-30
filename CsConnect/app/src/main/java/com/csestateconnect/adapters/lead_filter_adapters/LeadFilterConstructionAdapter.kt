package com.csestateconnect.adapters.lead_filter_adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.GetCompletionStatus
import com.csestateconnect.ui.home.lead_Frags.LeadFilterFragment
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class LeadFilterConstructionAdapter(
    val completionData: List<GetCompletionStatus>
) : RecyclerView.Adapter<LeadFilterConstructionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

//        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.filter_checkbox_item,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = completionData.get(position).name
        holder.name.setOnClickListener {
            Log.i("tag", "click working")

            val bundle = bundleOf("leadFilter" to completionData.get(position)?.id)
            val fragment = LeadFilterFragment()
            fragment.arguments = bundle
//            navController!!.navigate(R.id.action_lead2TabFragment_to_leadDetailsFragment, bundle)
        }

    }


    override fun getItemCount(): Int {
       return completionData.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.filter_checkbox

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}