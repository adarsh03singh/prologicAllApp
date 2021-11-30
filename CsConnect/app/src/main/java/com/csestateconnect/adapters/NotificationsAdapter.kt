package com.csestateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.csestateconnect.R
import com.csestateconnect.db.data.notifications.Result
import kotlinx.android.synthetic.main.notifications_list_items.view.*

class NotificationsAdapter(val data: List<Result>?) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.notifications_list_items,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text = data?.get(position)?.title
        holder.descriptionText.text = data?.get(position)?.body

        holder.itemView.setOnClickListener {
            val id = data?.get(position)?.record_id
            if (id != null && id != 0) {
                val action = data!!.get(position).click_action

                when (action) {
                    "LEAD_CREATE" -> {
                        val bundle = bundleOf("notifyId" to id)
                        navController!!.navigate(R.id.leadDetailsFragment, bundle)
                    }

                    "DEAL_CREATE" -> {
                        val bundle = bundleOf("notifyId" to id)
                        navController!!.navigate(R.id.dealDetailsFragment, bundle)
                    }

                    "LEADREQUEST_UPDATE" -> {
//                        val bundle = bundleOf("notifyId" to id)
                        navController!!.navigate(R.id.navigation_assigned_leads)
                    }

                    "CONCERN_UPDATE" -> {
                        val bundle = bundleOf("notifyId" to id)
                        navController!!.navigate(R.id.concernTicketDetailsFragment, bundle)
                    }

                    "CONCERNCOMMENT_CREATE" -> {
                        val bundle = bundleOf("notifyId" to id)
                        navController!!.navigate(R.id.concernTicketDetailsFragment, bundle)
                    }

                    "USER_UPDATE" -> {
                        navController!!.navigate(R.id.nav_get_touch)
                    }

                    "BANKACCOUNT_UPDATE" -> {
                        navController!!.navigate(R.id.header_view)
                    }

                    "KYC_UPDATE" -> {
                        navController!!.navigate(R.id.header_view)
                    }

                    "COMMISSIONSLAB_CREATE" -> {
                        navController!!.navigate(R.id.projectDetailFragment)
                    }

                    else -> {
                        navController!!.navigate(R.id.navigation_home)
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (!data.isNullOrEmpty()) data.size else 0
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titleText = itemView.notification_list_title
        var dateText = itemView.notification_list_date
        var descriptionText = itemView.notification_list_description

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}