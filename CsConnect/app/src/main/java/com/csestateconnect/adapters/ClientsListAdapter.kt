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
import kotlinx.android.synthetic.main.favorite_project_item.view.*
import java.lang.Exception

class ClientsListAdapter(
    var clientList : MutableList<Result?>, val itemClick: (Int) -> Unit)
    : RecyclerView.Adapter<ClientsListAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.clients_list_items,p0,false)

        return ViewHolder(v)
    }

    override fun getItemCount() = clientList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

           val  clData = clientList.get(position)

        try {
        holder.name?.text = clData!!.name
            holder.name.setOnClickListener {
                val returnId = clData!!.id
                val sharedPreference = holder.itemView.context!!.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putInt("clientList_id",returnId!!)
                editor.apply()
                navcontroller!!.navigate(R.id.clients_detailFragment)
//                itemClick(clData.id!!)
            }



            if(isNetworkConnected(holder.itemView.context)) {
                holder.clientDeleteButton.setOnClickListener {

                    itemClick(clData.id!!)
                    holder.clientDeleteButton.visibility = View.GONE
                    clientList.removeAt(position)
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
        val name = view.clients_name
//        val clientLayout = view.clientList_parentLayout
        val clientDeleteButton = view.delete_image
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}