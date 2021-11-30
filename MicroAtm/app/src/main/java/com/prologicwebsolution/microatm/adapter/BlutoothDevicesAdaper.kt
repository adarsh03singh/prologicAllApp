package com.prologicwebsolution.microatm.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.microatm.R
import com.prologicwebsolution.microatm.data.blutoothIItemsdata.BlutoothItemsData
import com.prologicwebsolution.microatm.data.transactionData.Data
import com.prologicwebsolution.microatm.ui.connectMicroAtm.ConnectMicroAtmFragment
import java.lang.Exception
import java.util.ArrayList

class BlutoothDevicesAdaper (val view: View, val myBluetoothDevices: ArrayList<BluetoothDevice>, val itemClick: (Int) -> Unit) : RecyclerView.Adapter<BlutoothDevicesAdaper.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlutoothDevicesAdaper.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.blutooth_paired_items, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: BlutoothDevicesAdaper.ViewHolder, position: Int) {

//        try {
            holder.blutooth_device_name.text = myBluetoothDevices[position].name
            holder.blutooth_device_ip_address.text = myBluetoothDevices[position].address

        holder.click.setOnClickListener { view: View? ->
            itemClick(position)
        }
//        }catch (e: Exception){
//            e.printStackTrace()
//        }


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return myBluetoothDevices.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                    val click = itemView.findViewById(R.id.click) as LinearLayoutCompat
            val blutooth_device_name = itemView.findViewById(R.id.blutooth_device_name) as TextView
            val blutooth_device_ip_address  = itemView.findViewById(R.id.blutooth_device_address) as TextView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

