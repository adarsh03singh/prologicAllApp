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
import com.prologicwebsolution.microatm.data.wallet.WalletEntity
import kotlinx.android.synthetic.main.transaction_items.view.*
import kotlinx.android.synthetic.main.wallet_balance_items.view.*

class WalletBalanceAdapter(val users: List<WalletEntity>?) : RecyclerView.Adapter<WalletBalanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.wallet_balance_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvWalletbalance.text = users!!.get(0).balance

    }

    override fun getItemCount(): Int {
        if (users?.size != null){
            return users.size
        }
        return 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvWalletbalance = itemView.tvWalletbalance

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
