package com.prologicwebsolution.microatm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.data.transactionData.Data
import kotlinx.android.synthetic.main.transaction_items.view.*

class TransactionDetailsAdapter(val users: List<Data>?,  val itemClick: (Int) -> Unit) : RecyclerView.Adapter<TransactionDetailsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.transaction_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTransactionAmount.text = users!!.get(position).amount
        holder.tvTransactiondate.text = users.get(position).date
        holder.tvCardNumber.text = users.get(position).cardno
        holder.tvtransactionRemark.text = users.get(position).bankremarks
        holder.tvtransactionRRN.text =  users.get(position).rrn
        holder.tvTransactionInvoice.text =   users.get(position).invoicenumber

        holder.transactionParentlayout.setOnClickListener{
            itemClick(position)
        }
    }

    override fun getItemCount(): Int {
        if (users?.size != null){
            return users.size
        }
        return 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvTransactionAmount = itemView.tvTransactionAmount
        var tvTransactiondate = itemView.tvTransactiondate
        var tvCardNumber = itemView.tvCardNumber
        var tvtransactionRemark = itemView.tvtransactionRemark
        var tvtransactionRRN = itemView.tvtransactionRRN
        var tvTransactionInvoice = itemView.tvTransactionInvoice
        var transactionParentlayout = itemView.transactionParentlayout

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
