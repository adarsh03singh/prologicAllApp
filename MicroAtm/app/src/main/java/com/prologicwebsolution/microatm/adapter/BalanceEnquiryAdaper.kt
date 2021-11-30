package com.prologicwebsolution.microatm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.ui.balanceEnquiryAndWithdraw.BalanceEnquiryAndWithdrawFragment
import com.prologicwebsolution.microatm.ui.connectMicroAtm.ConnectMicroAtmFragment
import com.prologicwebsolution.microatm.ui.withdrawlStatus.WithdrawlStatusFragment

class BalanceEnquiryAdaper (val userList: ArrayList<BalanceEnquiryAndWithdrawFragment.User>) : RecyclerView.Adapter<BalanceEnquiryAdaper.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.balance_enquiry_items, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: BalanceEnquiryAndWithdrawFragment.User) {
            val blutooth_device_name = itemView.findViewById(R.id.blutooth_device_name) as TextView

            blutooth_device_name.text = user.name

        }
    }
}

