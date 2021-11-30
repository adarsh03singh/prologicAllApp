package com.prologicwebsolution.microatm.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.data.transactionData.Data
import kotlinx.android.synthetic.main.commission_items.view.*
import kotlinx.android.synthetic.main.transaction_items.view.*
import kotlinx.android.synthetic.main.transaction_items.view.tvCardNumber
import kotlinx.android.synthetic.main.withdrawl_status_items.view.*

class WithdrawalStatusAdapter(val users: List<com.prologicwebsolution.microatm.data.payoutList.Data>?) : RecyclerView.Adapter<WithdrawalStatusAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.withdrawl_status_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.requestStatus.text = users!!.get(position).status
        holder.tvRequestTime.text = users.get(position).request_time
        holder.tvAmount.text = users.get(position).request_amount
        holder.tvAccountNo.text = users.get(position).account_no
        holder.tvProcessTime.text =   users.get(position).process_time
        holder.tvProcessingStatus.text =   users.get(position).response
        holder.tvProcessingCharge.text =  users.get(position).charge
    }

    override fun getItemCount(): Int {
        if (users?.size != null){
            return users.size
        }
        return 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var requestStatus = itemView.requestStatus
        var tvRequestTime = itemView.tvRequestTime
        var tvAmount = itemView.tvAmount
        var tvAccountNo = itemView.tvAccountNo
        var tvProcessTime = itemView.tvProcessTime
        var tvProcessingStatus = itemView.tvProcessingStatus
        var tvProcessingCharge = itemView.tvProcessingCharge

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
