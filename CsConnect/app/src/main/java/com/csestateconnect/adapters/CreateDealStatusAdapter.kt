package com.csestateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.GetDealStatus
import kotlinx.android.synthetic.main.filter_checkbox_item.view.*

class CreateDealStatusAdapter(val dealStatus: List<GetDealStatus>, val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<CreateDealStatusAdapter.ViewHolder>() {

    var selectedPosition = -1

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0?.context)
            .inflate(R.layout.filter_checkbox_item, p0, false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deal_status.text = dealStatus.get(position).name
        holder.deal_status.isChecked = (selectedPosition == position)

        holder.deal_status.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            itemClick(dealStatus.get(position).id)
        }

    }

    override fun getItemCount(): Int {
        return dealStatus.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deal_status = itemView.filter_checkbox
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
