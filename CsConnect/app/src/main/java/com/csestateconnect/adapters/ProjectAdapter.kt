package com.csestateconnect.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.BhkType
import com.csestateconnect.db.data.projects.Result
import com.csestateconnect.db.data.projects.Rooms
import java.lang.Exception


class ProjectAdapter(val projectList: List<Result?>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {


    var navcontroller: NavController? = null
    var count: Int = 0
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(com.csestateconnect.R.layout.project_items,p0,false)

        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val projectData = projectList.get(position)
    holder.projectName.text = projectData!!.name
    if (!projectData.iconImage.equals(null)) {
        Glide.with(holder.itemView.context).load(projectData.iconImage).into(holder.iconImageView)
    }
    holder.projectPrice.text = projectData.lowCostView + "-" + projectData.highCostView
    holder.projectLocation.text = projectData.location?.name + "," + projectData.city?.name
    holder.projectStatus.text = projectData.projectCompletionStatus!!.name
    try {
        holder.projectStatus?.setBackgroundColor(Color.parseColor(projectData.projectCompletionStatus!!.color!!))
    }catch (e: Exception){//Color.parseColor(users.get(viewType).color
        e.printStackTrace()
    }

    if(projectData.highCommission == true){
        holder.project_HighCommissionView.visibility = View.VISIBLE
    }else
        holder.project_HighCommissionView.visibility = View.GONE

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

    try {

        var bhkTypeDataWithDuplicate = ArrayList<Int>()
        var propertyTypeDataWithDuplicate = ArrayList<String>()

        var bhkTypeData: List<Int>? = null
        var propertyTypeData :List<String>? = null



        for (j in 0..projectData.projectTowers!!.size - 1 ) {

             projectData.projectTowers.get(j)!!.unitPlans!!.forEach { unitPlanData ->
                 if (unitPlanData != null) {

                     bhkTypeDataWithDuplicate.add(unitPlanData.bhkType!!.rooms!!)
                     propertyTypeDataWithDuplicate.add(unitPlanData.propertyType!!.name!!)


                     bhkTypeData = bhkTypeDataWithDuplicate.distinct()
                     propertyTypeData = propertyTypeDataWithDuplicate.distinct()

                 }
             }
            }


        if (!bhkTypeData.isNullOrEmpty()){
            holder.projectBhkType.visibility = View.VISIBLE
            holder.projectBhkType.text = bhkTypeData!!.joinToString()+"BHK" + " / " + propertyTypeData!!.joinToString()
        }else
            holder.projectBhkType.visibility = View.INVISIBLE
    }catch (e: Exception){
        e.printStackTrace()
    }



    holder.projectParentlayout.setOnClickListener {

        val sharedPreference =  holder.itemView.context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("projectId",projectData.id.toString())
        editor.apply()
//        val bundle = bundleOf("projectId" to projectData.id)
        navcontroller!!.navigate(R.id.action_navigation_projects_to_projectDetailFragment)

    }
    }
    override fun getItemCount(): Int {
        var count: Int =0
      try {
          count  = projectList.size
      }catch (e: Exception){
          e.printStackTrace()
      }
        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val projectName : TextView = itemView.findViewById(R.id.project_name)
        val iconImageView : ImageView = itemView.findViewById(R.id.project_image_icon)
        val projectPrice : TextView = itemView.findViewById(R.id.project_price_lowToHigh)
        val projectLocation : TextView = itemView.findViewById(R.id.prjoect_location)
        val projectStatus : TextView = itemView.findViewById(R.id.project_status)
        val projectBhkType  = itemView.findViewById<TextView>(R.id.project_bhkType)
        val projectParentlayout : LinearLayout = itemView.findViewById(R.id.project_parentLayout)
        val project_reraRegisteredTxtView  = itemView.findViewById<TextView>(R.id.reraRegisteredViewId)
        val project_HighCommissionView  = itemView.findViewById<ImageView>(R.id.highCommisionViewId)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}