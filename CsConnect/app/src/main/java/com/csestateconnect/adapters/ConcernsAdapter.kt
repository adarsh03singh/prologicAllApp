package com.csestateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.listOfConcerns.Result
import com.csestateconnect.utils.DateConverter
import com.csestateconnect.utils.DatenTimeConverter
import kotlinx.android.synthetic.main.concern_tickets_list_items.view.*

class ConcernsAdapter(val concernList: List<Result>?) : RecyclerView.Adapter<ConcernsAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.concern_tickets_list_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.titleText.text = "${concernList?.get(position)?.heading}. ID# ${concernList?.get(position)?.id}"
        holder.dateText.text = DatenTimeConverter().dateConverter(concernList?.get(position)?.created_at!!)
        if (concernList?.get(position)?.closed!!){
            holder.statusText.text = "Status- Close"
        }
        else {
            holder.statusText.text = "Status- Open"
        }


        holder.itemView.setOnClickListener {
            val bundle = bundleOf("concernId" to concernList.get(position).id)
            navController!!.navigate(R.id.action_concernTicketsListFragment_to_concernTicketDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        if (concernList?.size != null){
            return concernList.size
        }
        return 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titleText = itemView.concernTitleText
        var dateText = itemView.concernDateText
        var statusText = itemView.concernStatusText

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
