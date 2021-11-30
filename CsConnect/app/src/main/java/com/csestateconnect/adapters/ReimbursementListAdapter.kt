package com.csestateconnect.adapters

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.favouriteProject.FavouriteProject
import com.csestateconnect.db.data.listOfClients.Result
import com.csestateconnect.ui.navdrawer.FavoritesFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.clients_list_items.view.*
import kotlinx.android.synthetic.main.clients_list_items.view.delete_image
import kotlinx.android.synthetic.main.favorite_project_item.view.*
import kotlinx.android.synthetic.main.reimbursement_list_items.view.*
import java.lang.Exception

class ReimbursementListAdapter(
    var reimburseList : MutableList<com.csestateconnect.db.data.reimbursements.reimburseList.Result>, val itemClick: (Int) -> Unit)
    : RecyclerView.Adapter<ReimbursementListAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.reimbursement_list_items,p0,false)

        return ViewHolder(v)
    }

    override fun getItemCount() = reimburseList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

           val  reimburseData = reimburseList.get(position)

        try {
        holder.amount?.text = reimburseData!!.amount
            holder.type?.text = reimburseData!!.type!!.name

            holder.reimburseParentLayout.setOnClickListener {
                val returnId = reimburseData!!.id
                val sharedPreference = holder.itemView.context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putInt("reimburseList_id",returnId!!)
                editor.apply()
                navcontroller!!.navigate(R.id.reimbursementDetailFragment)
//                itemClick(clData.id!!)
            }



            if(isNetworkConnected(holder.itemView.context)) {
                holder.reimburseDeleteButton.setOnClickListener {

                    itemClick(reimburseData.id!!)
                    holder.reimburseDeleteButton.visibility = View.GONE
                    reimburseList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, getItemCount())

                }
            }else{
                Snackbar.make(holder.itemView,"No internet found.", Snackbar.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount = view.reimburse_amount
        val type = view.reimburse_type
        val reimburseParentLayout = view.reimburseList_parentLayout
        val reimburseDeleteButton = view.delete_image
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}