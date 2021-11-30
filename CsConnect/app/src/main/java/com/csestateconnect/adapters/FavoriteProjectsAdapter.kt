package com.csestateconnect.adapters

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.favouriteProject.FavouriteProject
import com.csestateconnect.ui.navdrawer.FavoritesFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_project_item.view.*
import java.lang.Exception

class FavoriteProjectsAdapter(
    var favProjectList : MutableList<FavouriteProject?>, val itemClick: (Int) -> Unit)
    : RecyclerView.Adapter<FavoriteProjectsAdapter.ViewHolder>() {

    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.favorite_project_item,p0,false)

        return ViewHolder(v)
    }

    override fun getItemCount() = favProjectList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

           val  projectData = favProjectList.get(position)

        try {
        holder.name?.text = projectData!!.name
        holder.address?.text = projectData.location?.name + "," + projectData.city?.name
        try {
            if (!projectData.iconImage.isNullOrEmpty()) {
                Glide.with(holder.itemView.context).load(projectData.iconImage).into(holder.image)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        try {
            val reraValue = projectData.projectReraNumber
            if (reraValue != null) {
                holder.project_reraRegisteredTxtView.visibility = View.VISIBLE

            } else {
                holder.project_reraRegisteredTxtView.visibility = View.GONE
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        if(projectData.highCommission == true){
            holder.project_HighCommissionView.visibility = View.VISIBLE
        }else
            holder.project_HighCommissionView.visibility = View.GONE

        holder.price?.text =projectData.lowCostView + "-" + projectData.highCostView
        holder.status?.text = projectData.projectCompletionStatus!!.name
        try {
            holder.status?.setBackgroundColor(Color.parseColor(projectData.projectCompletionStatus!!.color!!))
        }catch (e: Exception){
            e.printStackTrace()
        }

        holder.favProjectParentLayout.setOnClickListener {
            val sharedPreference =  holder.itemView.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("projectId",projectData.id.toString())
            editor.apply()
            navcontroller!!.navigate(R.id.action_nav_favorites_to_projectDetailFragment)
        }

            if(isNetworkConnected(holder.itemView.context)) {
                holder.projectDeleteButton.setOnClickListener {

                    itemClick(projectData.id!!)
                    holder.projectDeleteButton.visibility = View.GONE
                    favProjectList.removeAt(position)
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
        val name = view.projectName
        val address = view.projectAddress
        var image = view.projectImage
        var projectDeleteButton = view.project_bookmark_id
        var project_HighCommissionView = view.high_commisiionBookmark
        val price = view.projectPrice
        val status = view.projectStatus
        val favProjectParentLayout = view.fav_projctMainlayout
        val project_reraRegisteredTxtView = view.favProject_reraRegisteredViewId

    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}