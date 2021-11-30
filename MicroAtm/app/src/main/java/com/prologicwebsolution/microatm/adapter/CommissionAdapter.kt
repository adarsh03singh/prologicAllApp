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

class CommissionAdapter(val users: List<com.prologicwebsolution.microatm.data.transactionData.Data>?) : RecyclerView.Adapter<CommissionAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.commission_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCommissionTransactionAmount.text = users!!.get(position).txnamount
        holder.tvCommissionDate.text = users.get(position).date
        holder.tvCardNumber.text = users.get(position).cardno
        holder.tvRetailerCommission.text = users.get(position).retailer_commission
        holder.tvAmountCredited.text =  users.get(position).amount_credited
        holder.tvBalanceAmount.text =   users.get(position).amount
    }

    override fun getItemCount(): Int {
        if (users?.size != null){
            return users.size
        }
        return 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvCommissionDate = itemView.tvCommissionDate
        var tvCardNumber = itemView.tvCardNumber
        var tvCommissionTransactionAmount = itemView.tvCommissionTransactionAmount
        var tvRetailerCommission = itemView.tvRetailerCommission
        var tvAmountCredited = itemView.tvAmountCredited
        var tvBalanceAmount = itemView.tvBalanceAmount

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
