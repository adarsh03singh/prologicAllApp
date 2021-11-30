package com.csestateconnect.adapters.projectAdapters

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csestateconnect.R
import com.csestateconnect.db.data.projects.UnitPlan
import java.lang.Exception


class ProjectUnitPlanItemsAdapter(val unitPlans: List<UnitPlan?>?, val unitPlankey: String) :
    RecyclerView.Adapter<ProjectUnitPlanItemsAdapter.ViewHolder>() {


    var navcontroller: NavController? = null
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        navcontroller = Navigation.findNavController(p0)
        val v: View = LayoutInflater.from(p0.context)
            .inflate(com.csestateconnect.R.layout.units_plan_items,p0,false)

        return ViewHolder(v)
    }

/*class CustomViewHolder(val view:View):RecyclerView.ViewHolder(view)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    try {

            Glide.with(holder.itemView.context).load(unitPlans!!.get(position)!!.unitPlanImages!!.get(position)!!.imageUrl)
                .into(holder.unit_icon)

        holder.unit_property_type.text = unitPlans.get(position)!!. propertyType!!.name
        holder.unit_area.text = unitPlans.get(position)!!. superAreaView
        holder.unit_price_range.text = unitPlans.get(position)!!.lowCostView + " - " + unitPlans.get(position)!!.highCostView

        holder.unit_layout.setOnClickListener {
            val unitId = unitPlans.get(position)!!.id
            val preferences = holder.itemView.context.getSharedPreferences("PREFERENCE_NAME",
                MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("unitPlanId",unitId.toString())
            editor.putString("unitPlanKey",unitPlankey)
            editor.apply()

            navcontroller!!.navigate(R.id.action_projectDetailFragment_to_unitPlanDetailFragment)
        }

    }catch (e: Exception){
        e.printStackTrace()
    }


    }
    override fun getItemCount(): Int {
        var count: Int = 0


        try {

              count = unitPlans!!.size


        }catch (e: Exception){
          e.printStackTrace()
      }

        return count
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val unit_icon = itemView.findViewById<ImageView>(R.id.unit_plan_image)
        val unit_property_type = itemView.findViewById<TextView>(R.id.unit_plan_propertyType)
        val unit_area = itemView.findViewById<TextView>(R.id.unit_plan_area)
        val unit_price_range = itemView.findViewById<TextView>(R.id.unit_plan_price)
        val unit_layout = itemView.findViewById<LinearLayout>(R.id.unit_plan_layout)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

private fun Bundle.putParcelableArrayList(s: String, unitPlans: List<UnitPlan?>) {

}
