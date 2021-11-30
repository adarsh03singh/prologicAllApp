package com.csestateconnect.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.utils.DateConverter
import kotlinx.android.synthetic.main.deal_items.view.*

class DealsAdapter(val users: List<com.csestateconnect.db.data.deals.Result>?) : RecyclerView.Adapter<DealsAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.deal_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.dealName.text = users!!.get(position).lead.name
        holder.dealStatus.text = users.get(position).deal_status.name
        holder.dealStatusColor.setBackgroundColor(Color.parseColor(users.get(position).deal_status.color))
        holder.dealProject.text = users.get(position).project.name
        holder.dealId.text = "#" + users.get(position).id.toString()
        holder.dealDate.text = DateConverter().dateConverter(
            users.get(position).created_at)
        holder.dealLocation.text = users.get(position).lead.location?.name ?: "N/A"

        if (holder.dealStatus.text == "Closed"){
            holder.amount_view_layout1.visibility = View.VISIBLE
            holder.amount_view_layout2.visibility = View.GONE

            holder.dealTotalAmount.text = users.get(position).commission_amount_total_view
            holder.dealpayableAmount.text = users.get(position).commission_amount_payable_view ?: "N/A"
            holder.dealDueDate.text = users.get(position).days_left_for_payment.toString() + "Days"
        }
        else {
            holder.amount_view_layout1.visibility = View.GONE
            holder.amount_view_layout2.visibility = View.VISIBLE

            holder.dealTotalAmount2.text = users.get(position).commission_amount_total_view
            holder.dealPayableAmount2.text = users.get(position).commission_amount_payable_view ?: "N/A"
        }

        holder.itemView.setOnClickListener {
            val bundle = bundleOf("DealsId" to users.get(position).id)
            navController!!.navigate(R.id.action_navigation_comission_to_dealDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        if (users?.size != null){
            return users.size
        }
        return 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var dealName = itemView.deal_client_name
        var dealStatus = itemView.deal_status
        var dealStatusColor = itemView.dealStatusColor
        var dealProject = itemView.deal_project_name
        var dealId = itemView.deal_id
        var dealDate = itemView.deal_date
        var dealLocation = itemView.deal_location

        val dealTotalAmount = itemView.deal_total_amount
        val dealpayableAmount = itemView.deal_payable_amount
        var dealDueDate = itemView.deal_due_date

        var dealTotalAmount2 = itemView.deal_total_amount2
        var dealPayableAmount2 = itemView.deal_payable_amount2

        val amount_view_layout1 = itemView.Amount_view_layout1
        val amount_view_layout2 = itemView.Amount_view_layout2

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
