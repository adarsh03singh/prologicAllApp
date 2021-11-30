package com.csestateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import kotlinx.android.synthetic.main.project_name_dialog.view.*
import kotlinx.android.synthetic.main.project_name_recycler.view.*

class CreateDealSelectProjectAdapter(val assignedData: List<String?>?, val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<CreateDealSelectProjectAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.project_name_recycler, p0, false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = assignedData?.get(position)

        holder.name.setOnClickListener {
            itemClick(assignedData?.get(position)!!)
        }

    }

    override fun getItemCount(): Int {
        var count = 0
        if (assignedData != null) {
//            count = assignedData.results!!.size
            count = assignedData.size
        } else {
            itemClick("$count")
        }
        return count
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.projectname
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
