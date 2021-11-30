package com.prologicwebsolution.microatm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.ui.balanceEnquiryAndWithdraw.BalanceEnquiryAndWithdrawFragment
import com.prologicwebsolution.microatm.ui.connectMicroAtm.ConnectMicroAtmFragment
import com.prologicwebsolution.microatm.ui.home.HomeFragment
import com.prologicwebsolution.microatm.ui.withdrawlStatus.WithdrawlStatusFragment

class HomeReclerviewAdaper (val userList: ArrayList<HomeFragment.ServicesModel>, val itemClick: (Int) -> Unit) : RecyclerView.Adapter<HomeReclerviewAdaper.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.home_items, parent, false)

        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindItems(userList[position])

        val data = userList[position]
        holder.name.text = data.name
        holder.icons.setImageResource(data.images)

        holder.parentLayout.setOnClickListener{
            itemClick(position)
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        fun bindItems(user: HomeFragment.ServicesModel) {
            val name = itemView.findViewById(R.id.home_itemtxtName) as TextView
            val icons = itemView.findViewById(R.id.home_icons) as ImageView
        val parentLayout = itemView.findViewById(R.id.homeParentLayout) as LinearLayoutCompat




//        }
    }
}

