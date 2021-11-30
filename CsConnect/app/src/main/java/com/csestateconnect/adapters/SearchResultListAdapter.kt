package com.csestateconnect.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.SearchResult
import com.csestateconnect.db.data.leads.ListOfLeads
import com.csestateconnect.db.data.projects.*
import kotlinx.android.synthetic.main.cs_assign_lead_items.view.*
import kotlinx.android.synthetic.main.search_result_items.view.*
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SearchResultListAdapter(val projectResultItems: LinkedList<SearchResult>) :
    RecyclerView.Adapter<SearchResultListAdapter.ViewHolder>() {

    var navController: NavController? = null

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {

        navController = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(R.layout.search_result_items,p0,false)
        return ViewHolder(v)
    }

    /*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            val mCurrentSearchResult = projectResultItems.get(position)

            if (mCurrentSearchResult.getType().equals("CITY")) {
                holder.projectName.visibility = View.GONE
                holder.projectLocation.visibility = View.GONE
                holder.logoItemView.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_location_on_black_24dp))
                holder.cityName.visibility = View.VISIBLE
                holder.cityName.text = mCurrentSearchResult.getName()

                val city_name = mCurrentSearchResult.getName()

                holder.searchResultParent.setOnClickListener {
                    val bundle = bundleOf("city_name" to city_name)
                    navController!!.navigate(R.id.action_searchFragment_to_navigation_projects, bundle)
                }


            } else if(mCurrentSearchResult.getType().equals("PROJECT")){
                holder.projectName.visibility = View.VISIBLE
                holder.projectLocation.visibility = View.VISIBLE

                holder.projectName.text = mCurrentSearchResult.getName()
                holder.projectLocation.text = mCurrentSearchResult.getLocation()
                holder.logoItemView.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_project))
                holder.cityName.visibility = View.GONE

                val project_id = mCurrentSearchResult.getId()

                holder.searchResultParent.setOnClickListener {
                    val sharedPreference =  holder.itemView.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("projectId",project_id.toString())
                    editor.apply()
//                    val bundle = bundleOf("projectId" to project_id)
                    navController!!.navigate(R.id.action_searchFragment_to_projectDetailFragment)
                }

            }
        } catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var projectName = itemView.search_result_project_name as TextView
        var projectLocation = itemView.search_result_project_location_name as TextView
        var logoItemView = itemView.search_result_image as ImageView
        var searchResultParent = itemView.search_result_parent as LinearLayout
        var cityName = itemView.search_result_city_name as TextView
    }

//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }

//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
}
