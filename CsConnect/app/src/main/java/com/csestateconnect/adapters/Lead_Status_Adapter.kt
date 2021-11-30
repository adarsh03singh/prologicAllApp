package com.csestateconnect.adapters

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.GetLeadStatus
import kotlinx.android.synthetic.main.lead_deal_status_items.view.*
import java.lang.Exception


class Lead_Status_Adapter(val users: List<GetLeadStatus>, context: Context, val itemClick: (Int) -> Unit) : RecyclerView.Adapter<Lead_Status_Adapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.lead_deal_status_items,p0,false)

            v.setOnClickListener {

                try {
                    v.setBackgroundColor(Color.parseColor(users.get(viewType).color))
                    itemClick(viewType)
                    Handler().postDelayed({
                        //Do something after 100ms
                        v.setBackgroundColor(Color.parseColor("#ffffff"))
                    }, 500)
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }

        return ViewHolder(v)
    }


/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lead_status_item_view?.text = users.get(position).name
    }


    override fun getItemCount(): Int {
        return users.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var lead_status_item_view = itemView.status_text
    }


    override fun getItemId(position: Int): Long {
        if(TextUtils.isEmpty(users.get(position).name)){
            return position.toLong()
        }else
            return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
